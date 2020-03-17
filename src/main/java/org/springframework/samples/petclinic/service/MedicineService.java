package org.springframework.samples.petclinic.service;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.repository.MedicineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicineService {
	
	@Autowired
	private MedicineRepository repository;
	
	@Transactional
	public void saveMedicine(Medicine medicine) {
		repository.save(medicine);
	}
	
	@Transactional(readOnly = true)
	public Medicine findMedicineById(Integer id) {
		return repository.findById(id).orElse(null);
	}
	
	@Transactional
	public void deleteMedicine(Medicine medicine) {
		repository.delete(medicine);
	}
	
	@Transactional(readOnly = true)
	public Collection<Medicine> findManyMedicineByName(String name) {
		return repository.findByNameContaining(name);
	}

}
