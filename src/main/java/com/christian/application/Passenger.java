package com.christian.application;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;

@Entity
@Table(name="passenger")
public class Passenger {
	@Id
	private int passenger_id;
	@NotBlank(message="Email is mandatory")
	private String email;
	@NotBlank(message="Password is mandatory")
	private String password;
	@NotBlank(message="First Name is mandatory")
	private String firstname;
	@NotBlank(message="Last Name is mandatory")
	private String lastname;
	@NotBlank(message="Address is mandatory")
	private String address;
	@NotBlank(message="City is mandatory")
	private String city;
	@NotBlank(message="Postal Code is mandatory")
	private String postalcode;
	
	//Default Constructor
	public Passenger() {
		super();
	}

	//Getters and Setters
	public int getPassenger_id() {
		return passenger_id;
	}

	public void setPassenger_id(int passenger_id) {
		this.passenger_id = passenger_id;
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

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	
	
	
}
