package com.christian.application;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
	List<Flight> findByAirline(String airline);
	
	@Query("select distinct f.airline from Flight f")
	List<String> findDistinctAirline();
}
