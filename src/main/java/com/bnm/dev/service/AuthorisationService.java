package com.bnm.dev.service;

import java.util.List;

import com.bnm.dev.dto.UserRegistrationDto;
import com.bnm.dev.model.Authorisation;
import com.bnm.dev.model.User;

public interface AuthorisationService {
	Authorisation save(Authorisation authorisation);
	
	Authorisation loadAuthorisationById(Integer id);
	
	List<Authorisation> getAuthorisationsInProgress(String status);
}
