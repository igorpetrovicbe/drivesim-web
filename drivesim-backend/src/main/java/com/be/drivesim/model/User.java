package com.be.drivesim.model;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.be.drivesim.dto.UserRegisterDTO;

@Entity
@Table(name = "user_table")
public class User {
	@Id
	@SequenceGenerator(name = "user_sequence_generator", sequenceName = "user_sequence", initialValue = 100)
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator="user_sequence_generator")
	protected Integer id;
	
	@Column(name = "email")
	private String email;
	@Column(name = "password")
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"))
	private Set<Role> roles = new HashSet<>();
	
	boolean activated = false;
	
	@NotEmpty(message = "First name cannot be empty")
	@Size(min = 1, message = "First name must be at least 1 character long")
	@Column(name = "first_name")
	private String firstName;

	@NotEmpty(message = "Last name cannot be empty")
	@Size(min = 1, message = "Last name must be at least 1 character long")
	@Column(name = "last_name")
	private String lastName;
	
	private int gems = 0;
	
	public User() {}
	
	public User(UserRegisterDTO userDTO) {
		this.email = userDTO.getEmail();
		this.password = userDTO.getPassword(); //Enkripcija ovde?
		this.firstName = userDTO.getFirstName();
		this.lastName = userDTO.getLastName();
	}
	
	public Integer getId() {
		return id;
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
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
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
	
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + "]";
	}
}