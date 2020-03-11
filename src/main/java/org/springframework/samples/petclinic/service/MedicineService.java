package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.MedicineRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicineService {
	
	@Autowired
	private MedicineRepository repository;

}
