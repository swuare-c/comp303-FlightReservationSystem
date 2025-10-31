package com.christian.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReservationController {
	@Autowired
	private ResRepository reservationRepository;
	@Autowired
	private FlightRepository flightRepository;
	@Autowired
	private PassengerRepository passengerRepository;
	
	//Reservation Page
	@GetMapping("/reservation")
	public String reservation(@RequestParam(required = false) String airline, HttpSession session, Model m) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		
		if(p == null)
			return "redirect:/signin";
		
		m.addAttribute("passenger", p);
		
		if(airline != null) {
			List<Flight> flights = flightRepository.findByAirline(airline);
			m.addAttribute("flights", flights);
		}
		
		List<String> airlines = flightRepository.findDistinctAirline();
		m.addAttribute("airlines", airlines);
		return "reservation";
	}
	
	//Creating a Reservation
	 @PostMapping("/reservation")
	    public String createReservation(
	            @RequestParam String airline,
	            @RequestParam String origin,
	            @RequestParam String destination,
	            @RequestParam String departureDate,
	            @RequestParam String departureTime,
	            @RequestParam String arrivalTime,
	            @RequestParam int no_of_passengers,
	            HttpSession session,
	            Model m) {

	        Passenger p = (Passenger) session.getAttribute("passenger");
	        if (p == null)
	            return "redirect:/signin";
	        
	        Reservation existing = reservationRepository.findByPassenger(p);
	        if(existing != null) {
	        	reservationRepository.delete(existing);
	        	reservationRepository.flush();
	        }
	        	
	        Flight flight = new Flight();
	        flight.setAirline(airline);
	        flight.setOrigin(origin);
	        flight.setDestination(destination);
	        flight.setDeparture_time(LocalTime.parse(departureTime));
	        flight.setArrival_time(LocalTime.parse(arrivalTime));
	        flight.setPrice(no_of_passengers * 150);
	        
	        Passenger passenger = passengerRepository.findById(p.getPassenger_id()).orElse(null);

	        Reservation reservation = new Reservation();
	        reservation.setPassenger(passenger);
	        reservation.setFlight(flight);
	        reservation.setBooking_date(LocalDate.now());
	        reservation.setDeparture_date(LocalDate.parse(departureDate));
	        reservation.setNo_of_passengers(no_of_passengers);
	        reservation.setTotal_price(flight.getPrice() + 50);
	        reservation.setStatus("Pending");
	        p.setReservation(reservation);

	        flightRepository.save(flight);
	        reservationRepository.save(reservation);
	        passengerRepository.save(p);
	        session.setAttribute("reservation", reservation);

	        return "redirect:/checkout";
	    }
	
	//Update Reservation Page
	@GetMapping("/reservationupdate")
	public String update(HttpSession session, Model m) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		if(p == null)
			return "redirect:/signin";
		Reservation r = reservationRepository.findById(p.getReservation().getReservation_id()).orElse(null);
		m.addAttribute("reservation", r);
		return "reservationupdate";
	}
	
	//Update Reservation Details
	@PostMapping("/reservationupdate")
	public String edit(@RequestParam int reservation_id,
			@RequestParam LocalDate booking_date,
			@RequestParam LocalDate departure_date,
			@RequestParam int no_of_passengers,
			Model m,
			HttpSession session) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		
		
		Reservation r = reservationRepository.findById(reservation_id).orElse(null);
		if(r != null && r.getPassenger().getPassenger_id() == p.getPassenger_id()) {
			Flight f = flightRepository.findById(r.getFlight()).orElse(null);
			r.setBooking_date(booking_date);
			r.setDeparture_date(departure_date);
			r.setNo_of_passengers(no_of_passengers);
			
			reservationRepository.save(r);
			m.addAttribute("reservation", r);
			m.addAttribute("flight", f);
			
			return "redirect:/home";
		}
		else
			return "reservationupdate";
		
	}
	
	//View Reservation Details
	@GetMapping("/reservationdetails")
	public String view(HttpSession session, Model m) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		if(p == null)
			return "redirect:/signin";
		Reservation r = reservationRepository.findById(p.getReservation().getReservation_id()).orElse(null);
		Flight f = r.getActualFlight();
		
		m.addAttribute("flight", f);
		m.addAttribute("reservation", r);
		return "reservationdetails";
	}
	
	//Cancel Reservation only if it's more than 10 days before
	@GetMapping("/reservationdelete")
	public String cancel(HttpSession session, Model m) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		if(p == null)
			return "redirect:/signin";
		Reservation r = reservationRepository.findById(p.getReservation().getReservation_id()).orElse(null);
		
		
		LocalDate today = LocalDate.now();
		LocalDate departureDate = r.getDeparture_date();
		
		if (today.isBefore(departureDate.minusDays(10))) {
			r.setStatus("Cancelled");
			reservationRepository.save(r);
			m.addAttribute("message", "Reservation Cancelled");
		}
		else {
			m.addAttribute("message", "Cannot Cancel Reservation within 10 days of departure");
		}
		
		m.addAttribute("passenger", p);
		m.addAttribute("reservation", r);
		return "home";
	}
	
	@PostMapping("/checkout")
	public String processPayment
	(
			@RequestParam String cardNumber,
			@RequestParam String cardholderName,
			@RequestParam String expiryDate,
			@RequestParam String cvv,
			@RequestParam String paymentMethod,
			Model m,
			HttpSession session
			) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		if(p == null)
			return "redirect:/signin";
		Reservation r = reservationRepository.findById(p.getReservation().getReservation_id()).orElse(null);
		r.setStatus("Booked");
		Flight f = r.getActualFlight();

		m.addAttribute("flight", f);
		m.addAttribute("reservation", r);
		reservationRepository.save(r);
		return "redirect:/paymentconfirmation";
	}
	
	@GetMapping("/checkout")
	public String confirmation(HttpSession session, Model m) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		if(p == null)
			return "redirect:/signin";
		Reservation r = reservationRepository.findById(p.getReservation().getReservation_id()).orElse(null);
		Flight f = r.getActualFlight();

		m.addAttribute("flight", f);
		m.addAttribute("reservation", r);
		return "checkout";
	}
	
	@GetMapping("/paymentconfirmation")
	public String paymentconfirmation(HttpSession session, Model m) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		if(p == null)
			return "redirect:/signin";
		Reservation r = reservationRepository.findById(p.getReservation().getReservation_id()).orElse(null);
		Flight f = r.getActualFlight();
		
		m.addAttribute("flight", f);
		m.addAttribute("reservation", r);
		return "paymentconfirmation";
	}
}
