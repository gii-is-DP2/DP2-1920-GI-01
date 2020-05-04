package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat; 
import static org.junit.jupiter.api.Assertions.assertThrows; 

import java.util.Collection;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import generatedAssertions.customAssertions.TrainerAssert;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TrainerServiceTests {

	@Autowired
	protected TrainerService trainerService;
	
	// US-020 Unregistered user can see trainers --------------------------------------------------------------------
	
	@Test
	void shouldNotFindTrainerWithIncorrectId() {
		Optional<Trainer> noTrainer;
		
		noTrainer = this.trainerService.findTrainerById(-1);
		
		assertThat(noTrainer.isPresent()).isEqualTo(false);
	}
	
	@Test
	void shouldFindTrainerWithCorrectId() {
		Optional<Trainer> trainer1;
		
		trainer1 = this.trainerService.findTrainerById(1);
		
		assertThat(trainer1.isPresent()).isEqualTo(true);
		assertThat(trainer1.get().getFirstName()).startsWith("John");
		assertThat(trainer1.get().getLastName()).startsWith("Doe");
		
		User user = new User();
		user.setUsername("trainer1");
		user.setPassword("tr41n3r");
		user.setEnabled(true);
		
		// Custom assertions --------------------------------------------------
		
		TrainerAssert.assertThat(trainer1.get()).hasPhone("34 111111111");
		TrainerAssert.assertThat(trainer1.get()).hasEmail("acme@mail.com");
		TrainerAssert.assertThat(trainer1.get()).hasUser(user);
	}
	
	@Test
	void shouldFindAllTrainers() {
		Collection<Trainer> trainers;
		Trainer trainer1;
		
		trainers = (Collection<Trainer>) this.trainerService.findAll();
		trainer1 = EntityUtils.getById(trainers, Trainer.class, 1);
		
		assertThat(trainer1.getFirstName()).startsWith("John");
		
		
	}
	
	// US-019 Administrator manages trainers -----------------------------------------------------------------------
	
	@Test
	@Transactional
	void shouldInsertTrainer() {
		Collection<Trainer> trainers;
		int found;
		Trainer trainer = new Trainer();
		User user = new User();
		
		trainers = (Collection<Trainer>) this.trainerService.findAll();
		found = trainers.size();
		
		trainer.setFirstName("testFirstName");
		trainer.setLastName("testLastName");
		trainer.setEmail("email@test.com");
		trainer.setPhone("34 999999999");
		
		user.setUsername("testTrainerUsername");
		user.setPassword("testTrainerPassword");
		user.setEnabled(true);
		trainer.setUser(user);
		
		this.trainerService.saveTrainer(trainer);
		
		assertThat(trainer.getId()).isNotEqualTo(0);
		trainers = (Collection<Trainer>) this.trainerService.findAll();
		assertThat(trainers.size()).isEqualTo(found + 1);
		
	}
	
	@Test
	@Transactional
	void shouldNotInsertTrainerWithFirstNameBlank() {
		Trainer trainer = new Trainer();
		User user = new User();
		Exception exception;
		
		trainer.setFirstName("");
		trainer.setLastName("testLastName");
		trainer.setEmail("email@test.com");
		trainer.setPhone("34 999999999");
		
		user.setUsername("testTrainerUsername");
		user.setPassword("testTrainerPassword");
		user.setEnabled(true);
		trainer.setUser(user);
		
		exception = assertThrows(Exception.class, () -> this.trainerService.saveTrainer(trainer));
		//assertThat(exception.getMessage()).contains("Validation failed");
	}
	
	@Test
	@Transactional
	void shouldUpdateTrainerFirstName() throws Exception {
		Optional<Trainer> trainer1;
		String oldFirstName, newFirstName;
		
		trainer1 = this.trainerService.findTrainerById(1);
		
		assertThat(trainer1.isPresent()).isEqualTo(true);
		oldFirstName = trainer1.get().getFirstName();
		newFirstName = oldFirstName + "Test";
		trainer1.get().setFirstName(newFirstName);
		
		this.trainerService.saveTrainer(trainer1.get());
		trainer1 = this.trainerService.findTrainerById(1);
		
		assertThat(trainer1.isPresent()).isEqualTo(true);
		assertThat(trainer1.get().getFirstName()).isEqualTo(newFirstName);
		
	}
	
	@Test
	@Transactional
	void shouldDeleteTrainerWithCorrectId() {
		Optional<Trainer> trainer1 = this.trainerService.findTrainerById(1);
		
		assertThat(trainer1.isPresent()).isEqualTo(true);
		this.trainerService.deleteTrainer(trainer1.get());
	
		trainer1 = this.trainerService.findTrainerById(1);
		assertThat(trainer1.isPresent()).isEqualTo(false);
	}
	
}
