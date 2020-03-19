package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;   

import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.TrainerValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class TrainerValidatorTest extends ValidatorTests {

	//The right scenario
	@Test
	void shouldValidateWhenCorrect() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setFirstName("testFirstName");
		trainer.setLastName("testLastName");
		trainer.setEmail("example@test.com");
		trainer.setPhone("123456789");
		User user = new User();
		user.setUsername("testing");
		user.setPassword("testing");
		user.setEnabled(true);
		trainer.setUser(user);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
	}
	
	//This test should not validate
	//because the first name is empty
	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setFirstName("");
		trainer.setLastName("testLastName");
		trainer.setEmail("example@test.com");
		trainer.setPhone("123456789");
		User user = new User();
		user.setUsername("testing");
		user.setPassword("testing");
		user.setEnabled(true);
		trainer.setUser(user);
			
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trainer> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}
	
	//This test should not validate
	//because the last name is empty
	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setFirstName("testFirstName");
		trainer.setLastName("");
		trainer.setEmail("example@test.com");
		trainer.setPhone("123456789");
		User user = new User();
		user.setUsername("testing");
		user.setPassword("testing");
		user.setEnabled(true);
		trainer.setUser(user);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trainer> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("lastName");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}
	
	//This test should not validate
	//because the email is empty
	@Test
	void shouldNotValidateWhenEmailEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setFirstName("testFirstName");
		trainer.setLastName("testLastName");
		trainer.setEmail("");
		trainer.setPhone("123456789");
		User user = new User();
		user.setUsername("testing");
		user.setPassword("testing");
		user.setEnabled(true);
		trainer.setUser(user);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trainer> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}
	
	//This test should not validate
	//because the phone is empty
	@Test
	void shouldNotValidateWhenPhoneEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setFirstName("testFirstName");
		trainer.setLastName("testLastName");
		trainer.setEmail("example@test.com");
		trainer.setPhone("");
		User user = new User();
		user.setUsername("testing");
		user.setPassword("testing");
		user.setEnabled(true);
		trainer.setUser(user);
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trainer> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("phone");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}
	
	//This test should not validate
	//because the email doesn't follow the pattern
	@Test
	void shouldNotValidateWhenEmailDoesNotMatchPattern() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setFirstName("testFirstName");
		trainer.setLastName("testLastName");
		trainer.setEmail("noEmailPattern");
		trainer.setPhone("999999999");
		User user = new User();
		user.setUsername("testing");
		user.setPassword("testing");
		user.setEnabled(true);
		trainer.setUser(user);
		
		TrainerValidator trainerValidator = new TrainerValidator();
		Errors errors = new BeanPropertyBindingResult(trainer, "trainer");
		trainerValidator.validate(trainer, errors);
		
		assertThat(errors.hasFieldErrors("email")).isEqualTo(true);
		assertThat(errors.getFieldError("email").getDefaultMessage()).startsWith("Email should match the following pattern: acme@example.com");
	}
	
	//This test should not validate
	//because the phone doesn't follow the pattern
	@Test
	void shouldNotValidateWhenPhoneDoesNotMatchPattern() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setFirstName("testFirstName");
		trainer.setLastName("testLastName");
		trainer.setEmail("example@test.com");
		trainer.setPhone("aaaaaaaaa");
		User user = new User();
		user.setUsername("testing");
		user.setPassword("testing");
		user.setEnabled(true);
		trainer.setUser(user);
		
		TrainerValidator trainerValidator = new TrainerValidator();
		Errors errors = new BeanPropertyBindingResult(trainer, "trainer");
		trainerValidator.validate(trainer, errors);
		
		assertThat(errors.hasFieldErrors("phone")).isEqualTo(true);
		assertThat(errors.getFieldError("phone").getDefaultMessage()).startsWith("The phone should be added and it should contain only numbers");
	}
	
}
