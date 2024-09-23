import psycopg2
import matplotlib.pyplot as plt

# Database connection details
db_params = {
    'dbname': 'drivesim_db',
    'user': 'admin',
    'password': 'ftn',
    'host': 'localhost',
    'port': '5432'
}


# Function to fetch generation data for a specific model size
def fetch_generation_data(model_size):
    conn = psycopg2.connect(**db_params)
    cursor = conn.cursor()

    # Query for generation data filtered by model size
    cursor.execute(f"""
        SELECT date_time, generation_time, frames_generated 
        FROM generation 
        WHERE model_size = '{model_size}'
        ORDER BY date_time
    """)
    generation_data = cursor.fetchall()

    # Close connection
    cursor.close()
    conn.close()

    return generation_data


# Function to fetch BRISQUE data for a specific model size
def fetch_brisque_data(model_size):
    conn = psycopg2.connect(**db_params)
    cursor = conn.cursor()

    # Query to get BRISQUE score data for the first 5 frame indices and filter by model size
    cursor.execute(f"""
        SELECT g.date_time, b.index, b.value
        FROM brisque_score b
        JOIN generation g ON g.id = b.generation_id
        WHERE b.index < 5 AND g.model_size = '{model_size}'
        ORDER BY g.date_time, b.index
    """)
    data = cursor.fetchall()

    # Close connection
    cursor.close()
    conn.close()

    # Extract and organize data
    brisque_dict = {}
    for row in data:
        date_time, frame_index, brisque_score = row
        if frame_index not in brisque_dict:
            brisque_dict[frame_index] = {'dates': [], 'brisque_score': []}
        brisque_dict[frame_index]['dates'].append(date_time)
        brisque_dict[frame_index]['brisque_score'].append(brisque_score)

    return brisque_dict


# Set up the figure and axes for subplots
fig, axs = plt.subplots(3, 2, figsize=(15, 15))

# Model sizes to iterate through
model_sizes = ['SMALL', 'MEDIUM', 'LARGE']

# Plot generation time for each model size
for i, model_size in enumerate(model_sizes):
    generation_data = fetch_generation_data(model_size)

    # Extract data for generation time plot
    dates = [row[0] for row in generation_data]
    gen_time_per_frame = [row[1] / row[2] for row in generation_data]  # generationTime / framesGenerated

    # Plot generation time
    axs[i, 0].plot(dates, gen_time_per_frame, label="Generation Time per Frame", color='b', marker='o')
    axs[i, 0].set_xlabel("Time")
    axs[i, 0].set_ylabel("Generation Time per Frame")
    axs[i, 0].set_title(f"Generation Time per Frame Over Time ({model_size})")
    axs[i, 0].tick_params(axis='x', rotation=45)
    axs[i, 0].grid(True)

    # Fetch and plot BRISQUE data for the model size
    brisque_data = fetch_brisque_data(model_size)

    for index in range(5):
        if index in brisque_data:
            axs[i, 1].plot(brisque_data[index]['dates'], brisque_data[index]['brisque_score'], label=f"Index {index}", marker='o')

    axs[i, 1].set_xlabel("Time")
    axs[i, 1].set_ylabel("BRISQUE Score")
    axs[i, 1].set_title(f"BRISQUE Score per Frame Index Over Time ({model_size})")
    axs[i, 1].tick_params(axis='x', rotation=45)
    axs[i, 1].grid(True)

    axs[i, 1].legend()

# Adjust layout and show the plot
plt.tight_layout()
plt.legend(loc='upper right')
plt.show()
