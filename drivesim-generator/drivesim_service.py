from brisque import BRISQUE
from flask import Flask, request, jsonify
from threading import Thread, Lock
from queue import Queue
import base64
from io import BytesIO
from PIL import Image
import numpy as np
import cv2
import drivesim_generator
import time

app = Flask(__name__)

# Queue to hold incoming requests
request_queue = Queue()

# Dictionary to hold processed results
processed_results = {}

# Set to hold user_ids of requests currently in processing
processing_requests = set()

brisque = BRISQUE(url=False)

# Mutex to protect shared resources
mutex = Lock()

def process_image(image_np, num_frames, angle, speed, model_size, crop_image):
    # Generate frames (this is where your frame generation logic goes)
    generated_frames = drivesim_generator.generate_frames(image_np, angle, speed, num_frames, model_size, crop_image)
    return generated_frames

def worker():
    while True:
        data = request_queue.get()

        user_id = data.get('userId')
        frames_to_generate = data.get('framesToGenerate')
        angle = data.get('angle')
        speed = data.get('speed')
        image_base64 = data.get('imageBase64')
        model_size = data.get('modelSize')
        crop_image = data.get('cropImage')

        with mutex:
            processing_requests.add(user_id)

        # Decode the image
        image_data = base64.b64decode(image_base64)
        image = Image.open(BytesIO(image_data))

        # Convert PIL image to NumPy array
        image_np = np.array(image)
        #cv2.imshow("display", image_np)
        #cv2.waitKey()
        # Process the image and generate frames
        start_time = time.time()
        processed_frames = process_image(image_np, frames_to_generate, angle, speed, model_size, crop_image)
        end_time = time.time()

        # Calculate elapsed time in seconds
        elapsed_time = end_time - start_time

        brisque_scores = []

        for frame in processed_frames:
            brisque_score = brisque.score(frame)
            print(brisque_score)
            brisque_scores.append(brisque_score)
            #cv2.imshow("display", frame)
            #cv2.waitKey()

        with mutex:
            # Store the results (processed_frames is a list of NumPy images)
            processed_results[user_id] = {
                "framesGenerated": frames_to_generate,
                "frames": processed_frames,
                "brisqueScores": brisque_scores,
                "modelSize": model_size,
                "generationTime": elapsed_time,
                "message": "Frames generated successfully"
            }
            processing_requests.remove(user_id)

        request_queue.task_done()

@app.route('/process_request', methods=['POST'])
def process_request():
    data = request.get_json()
    user_id = data.get('userId')

    if user_id in processing_requests:
        # If the user is already being processed, return a message indicating that
        return jsonify({"message": "Request for this user is already being processed", "userId": user_id}), 409

    # Add request data to the queue
    request_queue.put(data)

    # Return an immediate response
    return jsonify({"message": "Request received", "userId": user_id})

@app.route('/get_result/<user_id>', methods=['GET'])
def get_result(user_id):
    user_id = int(user_id)
    with mutex:
        if user_id in processed_results:
            processed_result = processed_results.pop(user_id)
            # Get the list of OpenCV frames
            frames = processed_result['frames']

            # Convert each OpenCV frame to base64
            frames_base64 = []
            for frame in frames:
                # Convert each frame to RGB format
                pil_image = Image.fromarray(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))

                # Save the frame to a BytesIO buffer
                buffer = BytesIO()
                pil_image.save(buffer, format="PNG")
                buffer.seek(0)

                # Encode the buffer content (PNG image) to base64
                frame_base64 = base64.b64encode(buffer.getvalue()).decode('utf-8')
                frames_base64.append(frame_base64)

            # Send the list of base64-encoded frames in the JSON response
            return jsonify({
                "message": "Frames generated successfully",
                "userId": user_id,
                "framesGenerated": len(frames) - 1,
                "framesBase64": frames_base64,
                "modelSize": processed_result["modelSize"],
                "generationTime": processed_result["generationTime"],
                "brisqueScores": processed_result["brisqueScores"]
            })

        elif user_id in processing_requests:
            return jsonify({"message": "Processing not complete", "userId": user_id}), 202
        else:
            return jsonify({"message": "No processing request found for this userId", "userId": user_id}), 404

if __name__ == '__main__':
    # Start the worker thread
    worker_thread = Thread(target=worker, daemon=True)
    worker_thread.start()

    app.run(host='0.0.0.0', port=5000)
