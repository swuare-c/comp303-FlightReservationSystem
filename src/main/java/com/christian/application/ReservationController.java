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
	@GetMapping("/reservation/{id}")
	public String reservation(@PathVariable int id, Model m) {
		Passenger p = passengerRepository.findById(id).orElse(null);
		m.addAttribute("passenger", p);
		return "reservation";
	}
	
	//Creating a Reservation
	@PostMapping("/reservation/{id}")
	public String bookReservation
	(
			@RequestParam int reservation_id,
			@PathVariable("id") int passenger_id,
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
			Model m
			) {
		Flight flight = flightRepository.findById(flight_id)
				.orElseGet(() -> flightRepository.save(
						new Flight(flight_id, airline_name, departure_time, arrival_time, origin, destination, price))
						);
		
		Passenger passenger = passengerRepository.findById(passenger_id).orElse(null);
		
		Reservation reservation = new Reservation(reservation_id, passenger, flight, booking_date, departure_date, no_of_passengers, total_price, status);
		flightRepository.save(flight);
		reservationRepository.save(reservation);
		m.addAttribute("reservation",reservation);
		m.addAttribute("flight", flight);
		m.addAttribute("passenger", passenger);
		
		return "checkout";
	}
	
	//Update Reservation Details
	@GetMapping("/reservationupdate/{id}")
	public String update(@PathVariable int id, Model m) {
		Reservation r = reservationRepository.findById(id).orElse(null);
		m.addAttribute("reservation", r);
		return "reservationupdate";
	}
	
	@PostMapping("/reservationupdate/{id}")
	public String edit(@PathVariable("id") int reservation_id,
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
			@RequestParam double price,
			Model m) {
		Reservation r = reservationRepository.findById(reservation_id).orElse(null);
		if(r != null) {
			Flight f = flightRepository.findById(r.getFlight()).orElse(null);
			r.setBooking_date(booking_date);
			r.setDeparture_date(departure_date);
			r.setNo_of_passengers(no_of_passengers);
			f.setArrival_time(arrival_time);
			f.setDeparture_time(departure_time);
			
			reservationRepository.save(r);
			flightRepository.save(f);
			m.addAttribute("reservation", r);
			m.addAttribute("flight", f);
			
			return "reservation";
		}
		else
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
	
	@PostMapping("/checkout/{id}")
	public String processPayment
	(
			@PathVariable int id,
			@RequestParam String cardNumber,
			@RequestParam String cardholderName,
			@RequestParam LocalDate expiryDate,
			@RequestParam String cvv,
			@RequestParam String paymentMethod
			) {
		Reservation r = reservationRepository.findById(id).orElse(null);
		r.setStatus("Booked");
		reservationRepository.save(r);
		return "paymentconfirmation";
	}
	
	@GetMapping("/paymentconfirmation/{id}")
	public String confirmation(@PathVariable int id, Model m) {
		Reservation r = reservationRepository.findById(id).orElse(null);
		m.addAttribute("reservation", r);
		return "paymentconfirmation";
	}
}
