package com.christian.application;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ResRepository extends JpaRepository<Reservation, Integer> {
	Reservation findByPassenger(Passenger passenger);
}
