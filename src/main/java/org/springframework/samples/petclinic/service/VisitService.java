package org.springframework.samples.petclinic.service;

import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitService {

	@Autowired
	private VisitRepository visitRepository;
	
	@Transactional(readOnly = true)
	public Optional<Visit> findVisitById(int visitId) throws DataAccessException{
		return visitRepository.findById(visitId);
	}
	
//	@Transactional
//	public void saveVisit(Visit visit) throws DataAccessException{
//		this.visitRepository.save(visit);
//	}
	
	@Transactional
	public void delete(Visit visit) throws DataAccessException{
		this.visitRepository.delete(visit);
	}
	
}
