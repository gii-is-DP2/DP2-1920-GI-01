package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.repository.RehabRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RehabService {


	private RehabRepository rehabRepository;
	
	@Transactional(readOnly = true)
	public Optional<Rehab> findRehabById(int rehabId) throws DataAccessException{
		return rehabRepository.findById(rehabId);
	}
	
	@Transactional
	public void saveRehab(Rehab rehab) throws DataAccessException{
		this.rehabRepository.save(rehab);
	}
	
	
	@Transactional
	public void delete(Rehab rehab) throws DataAccessException{
		this.rehabRepository.delete(rehab);
	}
	}
