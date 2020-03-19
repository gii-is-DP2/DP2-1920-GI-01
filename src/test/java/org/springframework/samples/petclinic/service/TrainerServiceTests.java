package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat; 

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TrainerServiceTests {

	@Autowired
	protected TrainerService trainerService;
	
	@Test
	void shouldNotFindTrainerWithIncorrectId() {
		Optional<Trainer> noTrainer = this.trainerService.findTrainerById(-1);
		assertThat(!noTrainer.isPresent());
	}
	
	@Test
	void shouldFindTrainerWithCorrectId() {
		Optional<Trainer> trainer1 = this.trainerService.findTrainerById(1);
		if(trainer1.isPresent()) {
			assertThat(trainer1.get().getFirstName().startsWith("John"));
			assertThat(trainer1.get().getLastName().startsWith("Doe"));
		}
	}
	
	@Test
	void shouldFindAllTrainers() {
		Collection<Trainer> trainers = (Collection<Trainer>) this.trainerService.findAll();
		
		Trainer trainer1 = EntityUtils.getById(trainers, Trainer.class, 1);
		assertThat(trainer1.getFirstName().startsWith("John"));
	}
	
	@Test
	@Transactional
	void shouldInsertTrainer() {
		Collection<Trainer> trainers = (Collection<Trainer>) this.trainerService.findAll();
		int found = trainers.size();
		
		Trainer trainer = new Trainer();
		trainer.setFirstName("testFirstName");
		trainer.setLastName("testLastName");
		trainer.setEmail("email@test.com");
		trainer.setPhone("999999999");
		
		User user = new User();
		user.setUsername("testTrainerUsername");
		user.setPassword("testTrainerPassword");
		user.setEnabled(true);
		trainer.setUser(user);
		
		try {
			this.trainerService.saveTrainer(trainer);
		} catch (Exception e) {
			Logger.getLogger(TrainerServiceTests.class.getName()).log(Level.SEVERE, null, e);
		}
		
		assertThat(trainer.getId()).isNotEqualTo(0);
		trainers = (Collection<Trainer>) this.trainerService.findAll();
		assertThat(trainers.size()).isEqualTo(found + 1);
		
	}
	
	@Test
	@Transactional
	void shouldUpdateTrainerName() throws Exception {
		Optional<Trainer> trainer1 = this.trainerService.findTrainerById(1);
		String oldName, newName;
		if(trainer1.isPresent()) {
			oldName = trainer1.get().getFirstName();
			newName = oldName + "Test";
			trainer1.get().setFirstName(newName);
			this.trainerService.saveTrainer(trainer1.get());
			
			trainer1 = this.trainerService.findTrainerById(1);
			if(trainer1.isPresent()) {
				assertThat(trainer1.get().getFirstName()).isEqualTo(newName);
			}
		}
	}
	
	@Test
	@Transactional
	void shouldDeleteTrainer() {
		Optional<Trainer> trainer1 = this.trainerService.findTrainerById(1);
		if(trainer1.isPresent()) {
			this.trainerService.deleteTrainer(trainer1.get());
		}
		
		trainer1 = this.trainerService.findTrainerById(1);
		assertThat(!trainer1.isPresent());
		
	}
	
}
