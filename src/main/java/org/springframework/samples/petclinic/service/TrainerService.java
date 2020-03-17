package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.repository.springdatajpa.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainerService {

	//Lo siguiente se tiene que cambiar para que se pueda
	//usar correctamente en los tests
	
	@Autowired
	private TrainerRepository	trainerRepository;
	
	//This next method helps us find all trainers in the database
	@Transactional
	public Iterable<Trainer> findAll() {
		Iterable<Trainer> res;
		res = trainerRepository.findAll();
		return res;
	}
	
	//This next method helps us find a specific trainer given an id
	@Transactional
	public Optional<Trainer> findTrainerById(int trainerId) {
		Optional<Trainer> res;
		res = trainerRepository.findById(trainerId);
		return res;
	}
  
	@Transactional
	public void deleteTrainer (Trainer trainer) {
		this.trainerRepository.delete(trainer);
	}
	
	@Transactional
	public void saveTrainer (Trainer trainer) {
		this.trainerRepository.save(trainer);
	}
	
}
