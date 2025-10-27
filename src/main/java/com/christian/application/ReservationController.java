package com.christian.application;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String reservation() {
		return "reservation";
	}
	
	//Creating a Reservation
	@PostMapping("/reservation/{id}")
	public String bookReservation
	(
			@PathVariable int reservation_id,
			@RequestParam int passenger_id,
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
			@RequestParam double price
			) {
		Flight flight = new Flight(flight_id, airline_name, departure_time, arrival_time, origin, destination, price);
		flightRepository.save(flight);
		
		Passenger passenger = passengerRepository.findById(passenger_id).orElse(null);
		
		Reservation reservation = new Reservation(reservation_id, passenger, flight, booking_date, departure_date, no_of_passengers, total_price, status);
		reservationRepository.save(reservation);
		
		return "reservationdetails";
	}
	
	//Update Reservation Details
	@GetMapping("/reservationupdate/{id}")
	public String update(@PathVariable int id, Model m) {
		Reservation r = reservationRepository.findById(id).orElse(null);
		m.addAttribute("reservation", r);
		return "reservationupdate";
	}
	
	//View Reservation Details
	@GetMapping("/reservationdetails/{id}")
	public String view(@PathVariable int id, Model m) {
		Reservation r = reservationRepository.findById(id).orElse(null);
		m.addAttribute("reservation", r);
		return "reservationdetails";
	}
	
	//Cancel Reservation only if it's more than 10 days before
	@GetMapping("/reservationdelete/{id}")
	public String cancel(@PathVariable int id, Model m) {
		Reservation r = reservationRepository.findById(id).orElse(null);
		
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
}
