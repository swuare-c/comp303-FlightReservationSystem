package com.christian.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PassengerController {
	@Autowired
	private PassengerRepository passRepository;
}
