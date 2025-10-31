package com.christian.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
		return "Index";
	}
	
	//Sign Up Page
	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("passenger", new Passenger());
		return "signup";
	}
	
	//Login Page
	@GetMapping("/signin")
	public String signin(Model m) {
		m.addAttribute("passenger", new Passenger());
		return "signin";
	}
	
	//Register user
	@PostMapping("/register")
	public String register
	(@ModelAttribute Passenger p, HttpSession session, Model m) {
		//If email does not exist in database, Register user
		if(!passRepository.existsByEmailIgnoreCase(p.getEmail())) {
			passRepository.save(p);
			session.setAttribute("passenger", p);
			return "redirect:/reservation";
		}
		else if(passRepository.existsByEmailIgnoreCase(p.getEmail())) {
			m.addAttribute("error", "Account exists with email");
			return "signup";
		}
		else {
			m.addAttribute("error", "Error registering Account");
			return "signup";
		}
	}
	
	//Sign In
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model m) {
		Passenger passenger = passRepository.findByEmailIgnoreCase(email);
		if(passenger != null && passenger.getPassword().equals(password)) {
			session.setAttribute("passenger", passenger);
			return "reservation";
		}
		else {
			m.addAttribute("error", "Invalid email or password");
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
	public String update(@ModelAttribute Passenger pass, HttpSession session) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		if(p != null) {
			p.setEmail(pass.getEmail());
			p.setPassword(pass.getPassword());
			p.setFirstname(pass.getFirstname());
			p.setLastname(pass.getLastname());
			p.setAddress(pass.getAddress());
			p.setCity(pass.getCity());
			p.setPostalcode(pass.getPostalcode());
			
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
