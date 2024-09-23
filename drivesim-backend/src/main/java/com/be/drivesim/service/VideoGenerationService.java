package com.be.drivesim.service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import com.be.drivesim.dto.GeneratorRequestDTO;
import com.be.drivesim.dto.VideoGenerationRequestDTO;
import com.be.drivesim.model.BrisqueScore;
import com.be.drivesim.model.Generation;
import com.be.drivesim.model.User;
import com.be.drivesim.repository.BrisqueScoreRepository;
import com.be.drivesim.repository.GenerationRepository;
import com.be.drivesim.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VideoGenerationService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GenerationRepository generationRepository;
    
    @Autowired
    private BrisqueScoreRepository brisqueScoreRepository;

    private RestTemplate restTemplate = new RestTemplate();

    private static final String PYTHON_GENERATION_URL = "http://localhost:5000/process_request";
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateVideoFrames(Integer userId, int framesToGenerate, byte[] imageBytes, float angle, float speed, String modelSize, boolean cropImage) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        int gemsRequired = framesToGenerate;

        if (user.getGems() >= gemsRequired) {
            // Convert the image to Base64
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

            System.out.println(modelSize);
            // Prepare request payload with the image
            GeneratorRequestDTO request = new GeneratorRequestDTO(userId, framesToGenerate, angle, speed, imageBase64, modelSize, cropImage);

            try {
                // Send request to the Python service
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Type", "application/json");
                HttpEntity<GeneratorRequestDTO> entity = new HttpEntity<>(request, headers);

                ResponseEntity<String> response = restTemplate.exchange(
                		PYTHON_GENERATION_URL,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

                // Check if the response is successful (status code 2xx)
                if (response.getStatusCode().is2xxSuccessful()) {
                    return response.getBody(); // return the response body from Python service
                } else {
                    return "Failed to generate video frames. Please try again.";
                }
            } catch (RestClientException e) {
                // Handle exceptions, e.g., network issues, or non-2xx responses
                if (e.getMessage().contains("409"))
                    return "Request by this user is already being processed";
                else
                	return "Error communicating with the video generation service: " + e.getMessage();
            }
        } else {
            return "Not enough gems";
        }
    }
    
    public Object getResult(User user) {
        Integer userId = user.getId();
        String url = "http://localhost:5000/get_result/" + userId;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // Check the response status
            if (response.getStatusCode().value() == 200) {
                // Successful response
                String responseBody = response.getBody();

                // Parse the JSON response to a Map
                Map<String, Object> resultMap = objectMapper.readValue(responseBody, Map.class);
                int framesGenerated = (int) resultMap.get("framesGenerated");
                String modelSize = (String) resultMap.get("modelSize");
                List<Double> brisqueScoresFloat = (List<Double>) resultMap.get("brisqueScores");
                Double generationTime = (Double) resultMap.get("generationTime");
                float generationTimeFloat = (float) (generationTime.doubleValue());

                // Calculate gems required based on framesGenerated
                int gemsRequired = calculateGemsRequired(framesGenerated, modelSize);
                user.setGems(user.getGems() - gemsRequired);
                userRepository.save(user);
                
                Generation generation = new Generation(LocalDateTime.now(), user, modelSize, framesGenerated, generationTimeFloat);
                generationRepository.save(generation);
                
                for (int i = 0; i < brisqueScoresFloat.size(); i++) {
					BrisqueScore brisqueScore = new BrisqueScore((float) brisqueScoresFloat.get(i).doubleValue(), generation, i);
					brisqueScoreRepository.save(brisqueScore);
                }

                return resultMap;
            } else if (response.getStatusCode().value() == 202) {
                // Processing not complete
                return "Processing not complete. Try again later.";
            } else {
                // Other error responses
                return "Unexpected error occurred: " + response.getStatusCode();
            }
        } catch (RestClientException e) {
            // Handle HTTP errors (like 404)
            if (e.getMessage().contains("404")) {
                return "No requests are being processed. No results to show.";
            } else {
                e.printStackTrace();
                return "Error occurred while fetching results.";
            }
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return "Error occurred while fetching results.";
        }
    }

    private int calculateGemsRequired(int framesGenerated, String modelSize) {
        int cost;
        int smallMultiplyer = 1;
        int mediumMultiplyer = 3;
        int largeMultiplyer = 12;
        System.out.println("Calculated for size: " + modelSize);
        if (modelSize == "SMALL")
        	cost = framesGenerated * smallMultiplyer;
        else if (modelSize == "MEDIUM")
        	cost = framesGenerated * mediumMultiplyer;
        else
        	cost = framesGenerated * largeMultiplyer;
        
        return cost;
    }
}

