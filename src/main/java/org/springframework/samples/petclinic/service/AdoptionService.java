package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.repository.AdoptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdoptionService {
	
	@Autowired
	private AdoptionRepository repository;
	
	@Transactional
	public void saveAdoption(Adoption adoption) {
		repository.save(adoption);
	}
	
	@Transactional(readOnly = true)
	public Collection<Adoption> findManyAll() {
		return repository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Collection<Adoption> findByPetId(Integer petId) {
		return repository.findByPetId(petId);
	}
	
}
