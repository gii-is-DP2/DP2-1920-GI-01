package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.VisitValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class VisitValidatorTest extends ValidatorTests{

	@Test
	void shouldValidateWhenCorrect() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Visit visit = new Visit();
		visit.setDescription("testDescription");
		visit.setDate(LocalDate.of(2020, Month.DECEMBER, 24));
		Pet pet = new Pet();
		pet.setName("petTestName");
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		visit.setPet(pet);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Visit>> constraintViolations = validator.validate(visit);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
	}
	
	@Test
	void shouldNotValidateWhenDescriptionEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Visit visit = new Visit();
		visit.setDescription("");
		visit.setDate(LocalDate.of(2020, Month.DECEMBER, 24));
		Pet pet = new Pet();
		pet.setName("petTestName");
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		visit.setPet(pet);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Visit>> constraintViolations = validator.validate(visit);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Visit> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}
	
	@Test
	void shouldNotValidateWhenDateIsNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Visit visit = new Visit();
		visit.setDescription("testDescription");
		visit.setDate(null);
		Pet pet = new Pet();
		pet.setName("petTestName");
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		visit.setPet(pet);
		
		VisitValidator visitValidator = new VisitValidator();
		Errors errors = new BeanPropertyBindingResult(visit, "visit");
		visitValidator.validate(visit, errors);
		
		assertThat(errors.hasFieldErrors("date")).isEqualTo(true);
		assertThat(errors.getFieldError("date").getDefaultMessage()).startsWith("required and after current date");
	}
	
	@Test
	void shouldNotValidateWhenDateIsBeforeCurrent() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Visit visit = new Visit();
		visit.setDescription("testDescription");
		visit.setDate(LocalDate.of(1999, Month.APRIL, 4));
		Pet pet = new Pet();
		pet.setName("petTestName");
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		visit.setPet(pet);
		
		VisitValidator visitValidator = new VisitValidator();
		Errors errors = new BeanPropertyBindingResult(visit, "visit");
		visitValidator.validate(visit, errors);
		
		assertThat(errors.hasFieldErrors("date")).isEqualTo(true);
		assertThat(errors.getFieldError("date").getDefaultMessage()).startsWith("required and after current date");
	}
	
}
