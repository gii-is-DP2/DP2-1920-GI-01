
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.repository.InterventionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterventionService {

	@Autowired
	private InterventionRepository interventionRepository;


	@Transactional(readOnly = true)
	public Optional<Intervention> findInterventionById(final int interventionId) throws DataAccessException {
		return this.interventionRepository.findById(interventionId);
	}

	@Transactional
	public void saveIntervention(final Intervention intervention) throws DataAccessException {
		this.interventionRepository.save(intervention);
	}

	@Transactional
	public void delete(final Intervention intervention) throws DataAccessException {
		this.interventionRepository.delete(intervention);
	}

}
