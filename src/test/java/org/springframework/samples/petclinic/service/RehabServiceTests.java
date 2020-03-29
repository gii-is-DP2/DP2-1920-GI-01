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
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class RehabServiceTests {

	@Autowired
	protected RehabService rehabService;
	
	@Test
	void shouldNotFindRehabWithIncorrectId() {
		Optional<Rehab> noRehab = this.rehabService.findRehabById(-1);
		
		assertThat(noRehab.isPresent()).isEqualTo(false);
	}
	
	@Test
	void shouldFindRehabWithCorrectId() {
		Optional<Rehab> rehab = this.rehabService.findRehabById(1);
		
		assertThat(rehab.isPresent()).isEqualTo(true);
		assertThat(rehab.get().getDescription()).startsWith("Rehab session 1");
	}
	
	@Test
	void shouldInsertRehab() {
		Rehab rehab = new Rehab();
		rehab.setDate(LocalDate.of(2020, Month.SEPTEMBER, 9));
		rehab.setTime(2);
		rehab.setDescription("Test");
		Pet pet = new Pet();
		pet.setName("petTestName");
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		rehab.setPet(pet);
		
		this.rehabService.saveRehab(rehab);
		
		assertThat(rehab.getId()).isNotEqualTo(0);
	}
	
	@Test
	void shouldNotInsertRehabWithDescriptionEmpty() {
		ConstraintViolationException exception;
		Rehab rehab = new Rehab();
		
		rehab.setDescription("");
		rehab.setTime(2);
		rehab.setDate(LocalDate.of(2020, Month.DECEMBER, 24));
		Pet pet = new Pet();
		pet.setName("petTestName");
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		rehab.setPet(pet);

		exception = assertThrows(ConstraintViolationException.class, () -> this.rehabService.saveRehab(rehab));
		assertThat(exception.getMessage()).contains("Validation failed");
	}
	
	@Test
	void shouldUpdateRehabDescription() {
		Optional<Rehab> rehab = this.rehabService.findRehabById(1);
		String oldDescription, newDescription;
		
		assertThat(rehab.isPresent()).isEqualTo(true);
		oldDescription = rehab.get().getDescription();
		newDescription = oldDescription + " Test";
		rehab.get().setDescription(newDescription);
		
		this.rehabService.saveRehab(rehab.get());
		
		rehab = this.rehabService.findRehabById(1);
		assertThat(rehab.isPresent()).isEqualTo(true);
		assertThat(rehab.get().getDescription()).isEqualTo(newDescription);
	}
	
	@Test
	void shouldDeleteRehab() {
		Optional<Rehab> rehab = this.rehabService.findRehabById(1);
		
		assertThat(rehab.isPresent()).isEqualTo(true);
		this.rehabService.delete(rehab.get());
		
		rehab = this.rehabService.findRehabById(1);
		assertThat(rehab.isPresent()).isEqualTo(false);
	}
	
}
