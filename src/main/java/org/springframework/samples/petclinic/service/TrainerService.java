package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.repository.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainerService {

	//Lo siguiente se tiene que cambiar para que se pueda
	//usar correctamente en los tests
	
	@Autowired
	private TrainerRepository	trainerRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;
	
	//This next method helps us find all trainers in the database
	@Transactional
	public Iterable<Trainer> findAll() {
		Iterable<Trainer> res;
		res = trainerRepository.findAll();
		return res;
	}
	
	//This method allows us to find a trainer given an id
	@Transactional
	public Optional<Trainer> findTrainerById(int trainerId) {
		Optional<Trainer> res;
		res = trainerRepository.findById(trainerId);
		return res;
	}
	
	@Transactional
	public Optional<Trainer> findTrainerByUsername(String username) {
		Optional<Trainer> res;
		res = this.trainerRepository.findByUsername(username);
		return res;
	}
  
	//This method allows us to delete a given trainer
	@Transactional
	public void deleteTrainer (Trainer trainer) {
		this.trainerRepository.delete(trainer);
	}
	
	//This method allows us to save a given trainer
	@Transactional
	public void saveTrainer (Trainer trainer) {
		this.trainerRepository.save(trainer);
		this.userService.saveUser(trainer.getUser());
		this.authoritiesService.saveAuthorities(trainer.getUser().getUsername(), "trainer");
	}
	
}
