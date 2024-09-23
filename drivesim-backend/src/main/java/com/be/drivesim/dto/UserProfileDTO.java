package com.be.drivesim.dto;

import com.be.drivesim.model.User;

public class UserProfileDTO {
	private String firstName;
	private String lastName;
	private int gems;
	
	public UserProfileDTO() {}
	
	public UserProfileDTO(String firstName, String lastName, int gems) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gems = gems;
	}
	public UserProfileDTO(User user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.gems = user.getGems();
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

	public int getGems() {
		return gems;
	}

	public void setGems(int gems) {
		this.gems = gems;
	}
}
