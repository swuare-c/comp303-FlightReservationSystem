package com.christian.application;

import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="flight")
public class Flight {
	@Id
	private int flight_id;
	private String airline;
	private LocalTime departure_time;
	private LocalTime arrival_time;
	private String origin;
	
}
