package com.christian.application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public String reservation(@RequestParam String airline, HttpSession session, Model m) {
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
	public String bookReservation
	(
			@RequestParam int reservation_id,
			@RequestParam int flight_id,
			@RequestParam LocalDate booking_date,
			@RequestParam LocalDate departure_date,
			@RequestParam int no_of_passengers,
			@RequestParam double total_price,
			@RequestParam String status,
			@RequestParam String airline_name,
			@RequestParam LocalTime departure_time,
			@RequestParam LocalTime arrival_time,
			@RequestParam String origin,
			@RequestParam String destination,
			@RequestParam double price,
			Model m,
			HttpSession session
			) {
		Flight flight = flightRepository.findById(flight_id).orElse(null);
		
		Passenger passenger = (Passenger) session.getAttribute("passenger");
		
		Reservation reservation = new Reservation(reservation_id, passenger, flight, booking_date, departure_date, no_of_passengers, total_price, status);
		passenger.setReservation(reservation);
		reservationRepository.save(reservation);
		passengerRepository.save(passenger);
		
		m.addAttribute("reservation",reservation);
		m.addAttribute("flight", flight);
		m.addAttribute("passenger", passenger);
		
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
			@RequestParam double total_price,
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
			
			return "redirect:/reservation";
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
		
		m.addAttribute("reservation", r);
		return "reservationdetails";
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
		reservationRepository.save(r);
		return "redirect:/paymentconfirmation";
	}
	
	@GetMapping("/checkout")
	public String confirmation(HttpSession session, Model m) {
		Passenger p = (Passenger) session.getAttribute("passenger");
		if(p == null)
			return "redirect:/signin";
		Reservation r = reservationRepository.findById(p.getReservation().getReservation_id()).orElse(null);

		m.addAttribute("reservation", r);
		return "paymentconfirmation";
	}
}
