package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TrainerServiceTests {

	@Autowired
	protected TrainerService trainerService;
	
	@Test
	void shouldFindTrainerWithCorrectId() {
		Optional<Trainer> trainer1 = this.trainerService.findTrainerById(1);
		if(trainer1.isPresent()) {
			assertThat(trainer1.get().getName().startsWith("John"));
			assertThat(trainer1.get().getSurname().startsWith("Doe"));
		}
	}
	
	@Test
	void shouldFindAllTrainers() {
		Collection<Trainer> trainers = (Collection<Trainer>) this.trainerService.findAll();
		
		Trainer trainer1 = EntityUtils.getById(trainers, Trainer.class, 1);
		assertThat(trainer1.getName().startsWith("John"));
		Trainer trainer2 = EntityUtils.getById(trainers, Trainer.class, 2);
		assertThat(trainer2.getName().startsWith("Jane"));
	}
	
}
