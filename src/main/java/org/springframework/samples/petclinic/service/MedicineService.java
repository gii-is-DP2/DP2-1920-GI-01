package org.springframework.samples.petclinic.service;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.MedicineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicineService {
	
	@Autowired
	private MedicineRepository repository;
	
	@Transactional
	@CacheEvict(cacheNames = {"petTypes", "medicines"}, allEntries = true)
	public void saveMedicine(Medicine medicine) {
		repository.save(medicine);
	}
	
	@Transactional(readOnly = true)
	public Medicine findMedicineById(Integer id) {
		return repository.findById(id).orElse(null);
	}
	
	@Transactional
	@CacheEvict(cacheNames = {"petTypes", "medicines"}, allEntries = true)
	public void deleteMedicine(Medicine medicine) {
		repository.delete(medicine);
	}
	
	@Transactional(readOnly = true)
	@Cacheable("medicines")
	public Collection<Medicine> findManyMedicineByName(String name) {
		return repository.findByNameContaining(name);
	}
	
	@Transactional(readOnly = true)
	public Collection<Medicine> findManyAll() {
		return repository.findAll();
	}
	
	@Transactional(readOnly = true)
	@Cacheable("medicines")
	public Collection<Medicine> findByPetType(PetType petType) {
		return repository.findByPetType(petType);
	}

}
