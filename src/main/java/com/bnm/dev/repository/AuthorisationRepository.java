package com.bnm.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bnm.dev.model.Authorisation;

public interface AuthorisationRepository  extends JpaRepository<Authorisation, Long> {
	List<Authorisation> findByStatus(String status);
	Authorisation findById(Integer id);
}
