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
		authRepo.save(authorisation);
		return authorisation;
	}

	@Override
	public Authorisation loadAuthorisationById(Long id) {
		Authorisation auth = authRepo.findById(id).get();
		
		return auth;
	}

	@Override
	public List<Authorisation> getAuthorisationsInProgress(String status) {
		List<Authorisation> auths = authRepo.findByStatus(status);
		
		return auths;
	}

	@Override
	public Authorisation loadAuthByTransactionID(String transactionId) {
		Authorisation auth = authRepo.findByTransactionID(transactionId);
		return auth;
	}

}
