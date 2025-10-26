package com.christian.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
			return "signup";
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
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model m) {
		Passenger p = passRepository.findById(id).orElse(null);
		m.addAttribute("passenger", p);
		return "edit";
	}
	
	@PostMapping("/update/{id}")
	public String update(
			@PathVariable int passenger_id,
			@RequestParam String email,
			@RequestParam String password,
			@RequestParam String firstname,
			@RequestParam String lastname,
			@RequestParam String address,
			@RequestParam String city,
			@RequestParam String postalcode) {
		Passenger p = passRepository.findById(passenger_id).orElse(null);
		if(p != null) {
			p.setEmail(email);
			p.setPassword(password);
			p.setFirstname(firstname);
			p.setLastname(lastname);
			p.setAddress(address);
			p.setCity(city);
			p.setPostalcode(postalcode);
			
			passRepository.save(p);
		}
		return "reservation";
	}
	
	@GetMapping("/view/{id}")
	public String view(@PathVariable int id, Model m) {
		Passenger p = passRepository.findById(id).orElse(null);
		m.addAttribute("passenger", p);
		return "view";
	}
}
