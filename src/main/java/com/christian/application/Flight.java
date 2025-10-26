package com.christian.application;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="flight")
public class Flight {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int flight_id;
	@NotBlank(message="Must Select Airline")
	private String airline;
	@NotNull(message="Select Departure Time")
	private LocalTime departure_time;
	@NotNull(message="Select Arrival Time")
	private LocalTime arrival_time;
	@NotBlank(message="Select Origin")
	private String origin;
	@NotBlank(message="Select Destination")
	private String destination;
	private double price;
	
	public Flight() {
		super();
	}

	public int getFlight_id() {
		return flight_id;
	}

	public void setFlight_id(int flight_id) {
		this.flight_id = flight_id;
	}

	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	public LocalTime getDeparture_time() {
		return departure_time;
	}

	public void setDeparture_time(LocalTime departure_time) {
		this.departure_time = departure_time;
	}

	public LocalTime getArrival_time() {
		return arrival_time;
	}

	public void setArrival_time(LocalTime arrival_time) {
		this.arrival_time = arrival_time;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
