package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

public class AdoptionValidatorTests extends ValidatorTests {

	@Test
	void shouldValidateWhenCorrect() {
		Adoption adoption;
		LocalDate date;
		Owner owner;
		Pet pet;
		Validator validator;
		Set<ConstraintViolation<Adoption>> constraintViolations;
		
		validator = createValidator();
		date = LocalDate.now();
		owner = new Owner();
		pet = new Pet();
		adoption = new Adoption();
		
		adoption.setDate(date);
		adoption.setOwner(owner);
		adoption.setPet(pet);
		
		constraintViolations = validator.validate(adoption);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
	}
	
	@Test
	void shouldNotValidateWhenDateNull() {
		Adoption adoption;
		Owner owner;
		Pet pet;
		Validator validator;
		Set<ConstraintViolation<Adoption>> constraintViolations;
		ConstraintViolation<Adoption> violation;
		
		validator = createValidator();
		owner = new Owner();
		pet = new Pet();
		adoption = new Adoption();
		
		adoption.setOwner(owner);
		adoption.setPet(pet);
		
		constraintViolations = validator.validate(adoption);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("date");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
	}
	
	@Test
	void shouldNotValidateWhenOwnerNull() {
		Adoption adoption;
		LocalDate date;
		Pet pet;
		Validator validator;
		Set<ConstraintViolation<Adoption>> constraintViolations;
		ConstraintViolation<Adoption> violation;
		
		validator = createValidator();
		date = LocalDate.now();
		pet = new Pet();
		adoption = new Adoption();
		
		adoption.setDate(date);
		adoption.setPet(pet);
		
		constraintViolations = validator.validate(adoption);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("owner");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
	}
	
	@Test
	void shouldNotValidateWhenPetNull() {
		Adoption adoption;
		Owner owner;
		LocalDate date;
		Validator validator;
		Set<ConstraintViolation<Adoption>> constraintViolations;
		ConstraintViolation<Adoption> violation;
		
		validator = createValidator();
		owner = new Owner();
		date = LocalDate.now();
		adoption = new Adoption();
		
		adoption.setOwner(owner);
		adoption.setDate(date);
		
		constraintViolations = validator.validate(adoption);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("pet");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
	}
	
}
