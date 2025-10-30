package com.christian.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class PassengerController {
	@Autowired
	private PassengerRepository passRepository;
	
	//Home page
	@GetMapping("/index")
	public String home() {
		return "index";
	}
	
	//Sign Up Page
	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}
	
	//Login Page
	@GetMapping("/signin")
	public String signin() {
		return "signin";
	}
	
	//Register user
	@PostMapping("/register")
	public String register
	(
			@RequestParam int passenger_id,
			@RequestParam String email,
			@RequestParam String password,
			@RequestParam String firstname,
			@RequestParam String lastname,
			@RequestParam String address,
			@RequestParam String city,
			@RequestParam String postalcode,
			HttpSession session
			) {
		//If email does not exist in database, Register user
		if(!passRepository.existsByEmailIgnoreCase(email)) {
			Passenger p = new Passenger(passenger_id, email, password, firstname, lastname, address, city, postalcode);
			passRepository.save(p);
			session.setAttribute("passenger", p);
			return "redirect:/reservation";
		}
		else {
			return "signup";
		}
	}
	
	//Sign In
	@GetMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
		Passenger passenger = passRepository.findByEmailIgnoreCase(email);
		if(passenger != null && passenger.getPassword().equals(password)) {
			session.setAttribute("passenger", passenger);
			return "redirect:/reservation";
		}
		else {
			return "signin";
		}
	}
	
	@GetMapping("/edit")
	public String edit(Model m, HttpSession session) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		if(p == null)
			return "redirect:/signin";
		m.addAttribute("passenger", p);
		return "edit";
	}
	
	@PostMapping("/update")
	public String update(
			@RequestParam String email,
			@RequestParam String password,
			@RequestParam String firstname,
			@RequestParam String lastname,
			@RequestParam String address,
			@RequestParam String city,
			@RequestParam String postalcode,
			HttpSession session) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		if(p != null) {
			p.setEmail(email);
			p.setPassword(password);
			p.setFirstname(firstname);
			p.setLastname(lastname);
			p.setAddress(address);
			p.setCity(city);
			p.setPostalcode(postalcode);
			
			passRepository.save(p);
			session.setAttribute("passenger", p);
		}
		return "redirect:/reservation";
	}
	
	@GetMapping("/view")
	public String view(Model m, HttpSession session) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		if(p == null)
			return "redirect:/signin";
		m.addAttribute("passenger", p);
		return "view";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/index";
	}
}
