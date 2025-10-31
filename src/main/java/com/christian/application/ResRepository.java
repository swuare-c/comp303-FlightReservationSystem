package com.christian.application;

import org.springframework.data.repository.CrudRepository;

public interface ResRepository extends CrudRepository<Reservation, Integer> {
	Reservation findByPassenger(Passenger passenger);
}
