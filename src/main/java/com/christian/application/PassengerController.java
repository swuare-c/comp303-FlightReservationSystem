package com.christian.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PassengerController {
	@Autowired
	private PassengerRepository passRepository;
	
	//Home page
	@GetMapping("/index")
	public String home() {
		return "index";
	}
	
	//Register user
	@GetMapping("/signup")
	public String signUp
	(
			@RequestParam int passenger_id,
			@RequestParam String email,
			@RequestParam String password,
			@RequestParam String firstname,
			@RequestParam String lastname,
			@RequestParam String address,
			@RequestParam String city,
			@RequestParam String postalcode
			) {
		//If email does not exist in database, Register user
		if(!passRepository.existsByEmailIgnoreCase(email)) {
			Passenger p = new Passenger(passenger_id, email, password, firstname, lastname, address, city, postalcode);
			passRepository.save(p);
			return "reservation";
		}
		else {
			return "signip";
		}
	}
	
	//Sign In
	@GetMapping("/signin")
	public String signIn(@RequestParam String email, @RequestParam String password) {
		Passenger passenger = passRepository.findByEmailIgnoreCase(email);
		if(passenger != null && passenger.getPassword().equals(password)) {
			return "reservation";
		}
		else {
			return "signin";
		}
	}
}
