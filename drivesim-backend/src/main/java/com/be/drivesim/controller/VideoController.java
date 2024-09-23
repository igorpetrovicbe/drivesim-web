package com.be.drivesim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import com.be.drivesim.dto.VideoGenerationRequestDTO;
import com.be.drivesim.model.User;
import com.be.drivesim.service.UserService;
import com.be.drivesim.service.VideoGenerationService;
import org.springframework.validation.BindingResult;

import java.io.IOException;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private VideoGenerationService videoGenerationService;
    
    @Autowired
    private UserService userService;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @PostMapping("/generate")
    @Secured("ROLE_USER")
    public ResponseEntity<?> generateVideo(@Valid @RequestBody VideoGenerationRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
        	return ResponseEntity.status(400).body(result.getAllErrors().get(0).getDefaultMessage());
        }

        // Decode the base64-encoded image
        byte[] imageBytes;
        try {
            imageBytes = java.util.Base64.getDecoder().decode(request.getImage()); // Assuming image is sent as base64 in request DTO
        } catch (IllegalArgumentException e) {
        	return ResponseEntity.status(400).body("Invalid base64 image data.");
        }

        if (imageBytes.length > MAX_FILE_SIZE) {
        	return ResponseEntity.status(400).body("Image file size exceeds the maximum allowed limit of 5MB.");
        }
        
        if (!request.getModelSize().equals("SMALL") && !request.getModelSize().equals("MEDIUM") && !request.getModelSize().equals("LARGE")) {
        	return ResponseEntity.status(400).body("Invalid model size.");
        }

        try {
            // Pass the imageBytes to your service for further processing
        	String response = videoGenerationService.generateVideoFrames(
                    getCurrentUser().getId(),
                    request.getFramesToGenerate(),
                    imageBytes,
                    request.getAngle(),
                    request.getSpeed(),
                    request.getModelSize(),
                    request.getCropImage()
                );
        	if (response.contains("Request by this user is already being processed"))
        		return ResponseEntity.status(409).body(response);
        	else
        		return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error processing the request: " + e.getMessage());
        }
    }
    
    
    @GetMapping("/get_result")
    @Secured("ROLE_USER")
    public ResponseEntity<?> getResult() {
        User user = getCurrentUser();
        Object result = videoGenerationService.getResult(user);

        if (result instanceof String) {
            String message = (String) result;
            if (message.contains("Processing not complete. Try again later.")) {
                return ResponseEntity.status(202).body(message);
            } else if (message.contains("No requests are being processed. No results to show.")) {
                return ResponseEntity.status(404).body(message);
            } else {
                return ResponseEntity.status(500).body("Unexpected error: " + message);
            }
        } else if (result != null) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
	private User getCurrentUser() {
		String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.findByEmail(currentUserEmail);
	}
}
