package com.christian.application;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FlightRepository extends CrudRepository<Flight, Integer> {
	List<Flight> findByAirline(String airline);
	
	@Query("select distinct f.airline from Flight f")
	List<String> findDistinctAirline();
}
