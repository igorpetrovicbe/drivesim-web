package com.be.drivesim.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserRegisterDTO {
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "First name cannot be empty")
    @Size(min = 1, message = "First name must be at least 1 character long")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 1, message = "Last name must be at least 1 character long")
    private String lastName;
	
	public UserRegisterDTO() {}
	
	public UserRegisterDTO(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
