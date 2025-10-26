package com.christian.application;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="reservation")
public class Reservation {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int reservation_id;
	@OneToOne(mappedBy="passenger")
	private int passenger_id;
	@OneToOne(mappedBy="flight")
	private int flight_id;
	@NotNull(message="Select Booking Date")
	@Future
	private LocalDate booking_date;
	@NotNull(message="Select Departure Date")
	@Future
	private LocalDate departure_date;
	@NotNull(message="Enter Number of Passengers")
	@Size(max=9)
	private int no_of_passengers;
	private double total_price;
	private String status;
	
	public Reservation(int reservation_id, int passenger_id, int flight_id,
			@NotNull(message = "Select Booking Date") @Future LocalDate booking_date,
			@NotNull(message = "Select Departure Date") @Future LocalDate departure_date,
			@NotNull(message = "Enter Number of Passengers") @Size(max = 9) int no_of_passengers, double total_price,
			String status) {
		super();
		this.reservation_id = reservation_id;
		this.passenger_id = passenger_id;
		this.flight_id = flight_id;
		this.booking_date = booking_date;
		this.departure_date = departure_date;
		this.no_of_passengers = no_of_passengers;
		this.total_price = total_price;
		this.status = status;
	}

	public Reservation() {
	}

	public int getReservation_id() {
		return reservation_id;
	}

	public void setReservation_id(int reservation_id) {
		this.reservation_id = reservation_id;
	}

	public int getPassenger_id() {
		return passenger_id;
	}

	public void setPassenger_id(int passenger_id) {
		this.passenger_id = passenger_id;
	}

	public int getFlight_id() {
		return flight_id;
	}

	public void setFlight_id(int flight_id) {
		this.flight_id = flight_id;
	}

	public LocalDate getBooking_date() {
		return booking_date;
	}

	public void setBooking_date(LocalDate booking_date) {
		this.booking_date = booking_date;
	}

	public LocalDate getDeparture_date() {
		return departure_date;
	}

	public void setDeparture_date(LocalDate departure_date) {
		this.departure_date = departure_date;
	}

	public int getNo_of_passengers() {
		return no_of_passengers;
	}

	public void setNo_of_passengers(int no_of_passengers) {
		this.no_of_passengers = no_of_passengers;
	}

	public double getTotal_price() {
		return total_price;
	}

	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
