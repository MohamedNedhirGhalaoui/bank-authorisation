package com.bnm.dev.service;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.bnm.dev.dto.UserRegistrationDto;
import com.bnm.dev.model.User;

public interface UserService extends UserDetailsService {

	User save(UserRegistrationDto registrationDto);
	
	User loadUserByEmail(String email);
	
	List<User> getAll();
}
