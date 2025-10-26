package com.christian.application;

import org.springframework.data.repository.CrudRepository;

public interface PassengerRepository extends CrudRepository<Passenger, Integer> {
	//Find by Email
	Passenger findByEmailIgnoreCase(String email);
	boolean existsByEmailIgnoreCase(String email);
}
