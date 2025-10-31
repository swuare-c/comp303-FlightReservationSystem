package com.christian.application;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
	//Find by Email
	Passenger findByEmailIgnoreCase(String email);
	boolean existsByEmailIgnoreCase(String email);
}
