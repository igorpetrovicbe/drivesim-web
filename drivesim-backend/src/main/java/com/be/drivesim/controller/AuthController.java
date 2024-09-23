package com.be.drivesim.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.be.drivesim.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import com.be.drivesim.model.User;
import com.be.drivesim.jwt.JwtResponse;
import com.be.drivesim.repository.RoleRepository;
import com.be.drivesim.repository.UserRepository;
import com.be.drivesim.dto.LoginDTO;
import com.be.drivesim.jwt.JwtUtils;
import com.be.drivesim.model.UserDetailsImpl;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	JwtUtils jwtUtils;
	@Autowired
	UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginDTO loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);


		if(user == null || !user.isActivated())
			return new ResponseEntity<>(
				      "User not authenticated or does not exist!", 
				      HttpStatus.NOT_FOUND);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getEmail(), 
												 roles));
	}
}
