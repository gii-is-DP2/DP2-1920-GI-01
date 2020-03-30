package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows; 

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class VisitServiceTests {

	@Autowired
	protected VisitService visitService;
	
	@Autowired
	protected PetService petService;
	
	@Test
	void shouldNotFindVisitWithIncorrectId() {
		Optional<Visit> noVisit = this.visitService.findVisitById(-1);
		
		assertThat(noVisit.isPresent()).isEqualTo(false);
	}
	
	@Test
	void shouldFindVisitWithCorrectId() {
		Optional<Visit> visit = this.visitService.findVisitById(1);
		
		assertThat(visit.isPresent()).isEqualTo(true);
		assertThat(visit.get().getDescription()).startsWith("rabies shot");
	}
	
	@Test
	void shouldInsertVisit() {
		Visit visit = new Visit();
		visit.setDescription("testDescription");
		visit.setDate(LocalDate.of(2020, Month.DECEMBER, 24));
		Pet pet = new Pet();
		pet.setName("petTestName");
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		visit.setPet(pet);
		
		this.petService.saveVisit(visit);
		
		assertThat(visit.getId()).isNotEqualTo(0);
	}
	
	@Test
	void shouldNotInsertVisitWithDescriptionEmpty() {
		ConstraintViolationException exception;
		Visit visit = new Visit();
		
		visit.setDescription("");
		visit.setDate(LocalDate.of(2020, Month.DECEMBER, 24));
		Pet pet = new Pet();
		pet.setName("petTestName");
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		visit.setPet(pet);

		exception = assertThrows(ConstraintViolationException.class, () -> this.petService.saveVisit(visit));
		assertThat(exception.getMessage()).contains("Validation failed");
	}
	
	@Test
	void shouldUpdateVisitDescription() {
		Optional<Visit> visit1 = this.visitService.findVisitById(1);
		String oldDescription, newDescription;
		
		assertThat(visit1.isPresent()).isEqualTo(true);
		oldDescription = visit1.get().getDescription();
		newDescription = oldDescription + " Test";
		visit1.get().setDescription(newDescription);
		
		this.petService.saveVisit(visit1.get());
		
		visit1 = this.visitService.findVisitById(1);
		assertThat(visit1.isPresent()).isEqualTo(true);
		assertThat(visit1.get().getDescription()).isEqualTo(newDescription);
	}
	
	@Test
	@Transactional
	void shouldDeleteVisit() {
		Optional<Visit> visit1 = this.visitService.findVisitById(1);
		
		assertThat(visit1.isPresent()).isEqualTo(true);
		this.visitService.delete(visit1.get());
		
		visit1 = this.visitService.findVisitById(1);
		assertThat(visit1.isPresent()).isEqualTo(false);
	}
	
}
