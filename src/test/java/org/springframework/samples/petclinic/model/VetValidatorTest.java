package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat; 

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;


public class VetValidatorTest extends ValidatorTests{
	
	//The right scenario
	@Test
	void shouldValidateWhenCorrect() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Vet vet = new Vet();
		vet.setFirstName("testFirstName");
		vet.setLastName("testLastName");
		List<Specialty> noSpecialties = new ArrayList<Specialty>();
		vet.setSpecialties(noSpecialties);
		User user = new User();
		user.setUsername("testing");
		user.setPassword("testing");
		user.setEnabled(true);
		vet.setUser(user);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Vet>> constraintViolations = validator.validate(vet);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
	}
	
	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Vet vet = new Vet();
		vet.setFirstName("");
		vet.setLastName("testLastName");
		List<Specialty> noSpecialties = new ArrayList<Specialty>();
		vet.setSpecialties(noSpecialties);
		User user = new User();
		user.setUsername("testing");
		user.setPassword("testing");
		user.setEnabled(true);
		vet.setUser(user);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Vet>> constraintViolations = validator.validate(vet);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Vet> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}
	
	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Vet vet = new Vet();
		vet.setFirstName("testFirstName");
		vet.setLastName("");
		List<Specialty> noSpecialties = new ArrayList<Specialty>();
		vet.setSpecialties(noSpecialties);
		User user = new User();
		user.setUsername("testing");
		user.setPassword("testing");
		user.setEnabled(true);
		vet.setUser(user);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Vet>> constraintViolations = validator.validate(vet);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Vet> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("lastName");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}
	
}
