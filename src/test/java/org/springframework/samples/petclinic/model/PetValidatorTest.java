package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.PetValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class PetValidatorTest extends ValidatorTests{

	//Perfect scenario --------------------------------------------------------------------------------------------------
	
	@Test
	void shouldValidateWhenCorrect() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Pet pet = new Pet();
		pet.setName("testName");
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Pet>> constraintViolations = validator.validate(pet);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
	}
	
	//Parameterized tests -----------------------------------------------------------------------------------------------
	
	@ParameterizedTest
	@ValueSource(strings = {"p", "peeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeet"})
	void shouldNotValidateWhenNameLengthLowerThanThreeAndMoreThanFifty(String name) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Pet pet = new Pet();
		pet.setName(name);
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Pet>> constraintViolations = validator.validate(pet);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Pet> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 50");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"pet", "petTesting", "peeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeet"})
	void shouldValidateWhenNameLengthMoreThanThreeAndLessThanFifty(String name) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Pet pet = new Pet();
		pet.setName(name);
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Pet>> constraintViolations = validator.validate(pet);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
	}
	
	//Custom validator PetValidator -------------------------------------------------------------------------------------
	
	@ParameterizedTest
	@ValueSource(strings = {"p", "peeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeet"})
	void customValidatorShouldNotValidateWhenNameLengthLowerThanThreeAndMoreThanFifty(String name) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Pet pet = new Pet();
		pet.setName(name);
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		
		PetValidator petValidator = new PetValidator();
		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		petValidator.validate(pet, errors);
		
		assertThat(errors.hasFieldErrors("name")).isEqualTo(true);
		assertThat(errors.getFieldError("name").getDefaultMessage()).startsWith("required and between 3 and 50 characters");
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"pet", "petTesting", "peeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeet"})
	void customValidatorShouldValidateWhenNameLengthMoreThanThreeAndLessThanFifty(String name) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Pet pet = new Pet();
		pet.setName(name);
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		
		PetValidator petValidator = new PetValidator();
		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		petValidator.validate(pet, errors);
		
		assertThat(errors.hasFieldErrors("name")).isEqualTo(false);
	}
	
	@Test
	void customValidatorShouldNotValidateWhenBirthDateIsNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Pet pet = new Pet();
		pet.setName("testName");
		pet.setBirthDate(null);
		
		PetValidator petValidator = new PetValidator();
		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		petValidator.validate(pet, errors);
		
		assertThat(errors.hasFieldErrors("birthDate")).isEqualTo(true);
		assertThat(errors.getFieldError("birthDate").getDefaultMessage()).startsWith("required and before current date");
	}
	
	@Test
	void customValidatorShouldNotValidateWhenBirthDateIsAfterCurrent() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Pet pet = new Pet();
		pet.setName("testName");
		pet.setBirthDate(LocalDate.of(2021, Month.APRIL, 4));
		
		PetValidator petValidator = new PetValidator();
		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		petValidator.validate(pet, errors);
		
		assertThat(errors.hasFieldErrors("birthDate")).isEqualTo(true);
		assertThat(errors.getFieldError("birthDate").getDefaultMessage()).startsWith("required and before current date");
	}
	
}
