package com.be.drivesim.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.be.drivesim.dto.UserProfileDTO;
import com.be.drivesim.dto.UserRegisterDTO;
import com.be.drivesim.model.Role;
import com.be.drivesim.model.User;
import com.be.drivesim.service.UserService;
import com.be.drivesim.repository.TokenRepository;
import com.be.drivesim.model.VerificationToken;
import com.be.drivesim.event.OnRegistrationCompleteEvent;
import com.be.drivesim.jwt.JwtUtils;


@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	TokenRepository tokenRepository;
	
	@Autowired
	ApplicationEventPublisher eventPublisher;
		

	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody UserRegisterDTO signUpRequest, HttpServletRequest request)
	{
		if (userService.findByEmail(signUpRequest.getEmail()) != null) {
			return ResponseEntity
					.badRequest()
					.body("Email already taken!");
		}
		signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));
		//signUpRequest.setPassword(signUpRequest.getPassword());
		User registeredUser = new User(signUpRequest);
		
		userService.registerUser(registeredUser);
		String appUrl = request.getContextPath();

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser,
          request.getLocale(), appUrl));
		return new ResponseEntity<>(
			      "Registration successful!",
			      HttpStatus.OK);
	}
	
	@GetMapping("/registration_confirm")
	public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
		
	    VerificationToken verificationToken = tokenRepository.findByToken(token).orElse(null);
	    if (verificationToken == null) {
	    	return new ResponseEntity<>(
				      "Verification token not found!",
				      HttpStatus.NOT_FOUND);
	    }
	    
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	    	return new ResponseEntity<>(
				      "Verification token expired!",
				      HttpStatus.BAD_REQUEST);
	    }
	    
	    user.setActivated(true);
	    userService.save(user);
	    return new ResponseEntity<>(
			      "Successful verification!",
			      HttpStatus.OK);
	}
	
	@GetMapping("/profile")
	@Secured("ROLE_USER")
	public ResponseEntity<UserProfileDTO> getUserProfile() {
		User user = getCurrentUser();
		UserProfileDTO userDTO = new UserProfileDTO(user);
		
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}
	
	@GetMapping("/test")
	public ResponseEntity<String> test() {
		String[] passwords = {"dunja123", "nikola123", "marko123", "milica123", "ivan123", "ana123", "luka123", "jovana123", "stefan123"};
		for(String s : passwords)
			System.out.println(encoder.encode(s));
		//socketService.processMsg("I AM HERE");
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	private User getCurrentUser() {
		String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.findByEmail(currentUserEmail);
	}
	
}