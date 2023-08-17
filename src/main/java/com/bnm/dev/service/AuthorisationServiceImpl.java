package com.bnm.dev.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bnm.dev.model.Authorisation;
import com.bnm.dev.repository.AuthorisationRepository;

@Service
public class AuthorisationServiceImpl implements AuthorisationService {
	private AuthorisationRepository authRepo;
	
	public AuthorisationServiceImpl(AuthorisationRepository authRepo) {
		super();
		this.authRepo = authRepo;
	}
	
	@Override
	public Authorisation save(Authorisation authorisation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Authorisation loadAuthorisationById(Integer id) {
		Authorisation auth = authRepo.findById(id);
		
		return auth;
	}

	@Override
	public List<Authorisation> getAuthorisationsInProgress(String status) {
		List<Authorisation> auths = authRepo.findByStatus(status);
		
		return auths;
	}

}
