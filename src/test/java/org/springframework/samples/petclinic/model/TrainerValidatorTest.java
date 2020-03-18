package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat; 

import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

public class TrainerValidatorTest extends ValidatorTests {

	//The right scenario
	@Test
	void shouldValidateWhenCorrect() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setName("testName");
		trainer.setSurname("testSurname");
		trainer.setEmail("example@test.com");
		trainer.setPhone("123456789");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
	}
	
	//This test should not validate
	//because the name's length is less than 3 characters
	@Test
	void shouldNotValidateWhenNameSizeLessThanThree() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setName("aa");
		trainer.setSurname("testSurname");
		trainer.setEmail("example@test.com");
		trainer.setPhone("123456789");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trainer> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 50");
	}
	
	//This test should validate
	//because the name's length is exactly 3 characters
	//and there's no problem with that
	@Test
	void shouldValidateWhenNameSizeExactlyThree() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setName("aaa");
		trainer.setSurname("testSurname");
		trainer.setEmail("example@test.com");
		trainer.setPhone("123456789");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
	}
	
	//This test should validate
	//because the name's length is exactly 50 characters
	//and there's no problem with that
	@Test
	void shouldValidateWhenNameSizeExactlyFifty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setName("Teeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeest");
		trainer.setSurname("testSurname");
		trainer.setEmail("example@test.com");
		trainer.setPhone("123456789");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
	}
	
	//This test should not validate
	//because the name's length is more than 50 characters
	@Test
	void shouldNotValidateWhenNameSizeMoreThanFifty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setName("Teeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeest");
		trainer.setSurname("testSurname");
		trainer.setEmail("example@test.com");
		trainer.setPhone("123456789");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trainer> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 50");
	}
	
	//This is a parameterized test which should validate every value
	//because they are all >= 3 and <= 50 characters
	@ParameterizedTest
	@ValueSource(strings = {"testName", "aaa",
			"Teeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeest"})
	void shouldValidateWhenNameIsCorrect(String name) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setName(name);
		trainer.setSurname("testSurname");
		trainer.setEmail("example@test.com");
		trainer.setPhone("123456789");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
	}
	
	//This is a parameterized test which should not validate neither value
	//because they are not the specified length
	@ParameterizedTest
	@ValueSource(strings = {"aa", "Teeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeest"})
	void shouldValidateWhenNameIsNotCorrect(String name) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setName(name);
		trainer.setSurname("testSurname");
		trainer.setEmail("example@test.com");
		trainer.setPhone("123456789");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trainer> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 50");
	}
	
	//This test should not validate
	//because the surname is empty
	@Test
	void shouldNotValidateWhenSurnameEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setName("Test");
		trainer.setSurname("");
		trainer.setEmail("example@test.com");
		trainer.setPhone("123456789");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trainer> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("surname");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}
	
	//This test should not validate
	//because the email is empty
	@Test
	void shouldNotValidateWhenEmailEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Trainer trainer = new Trainer();
		trainer.setName("Test");
		trainer.setSurname("testing");
		trainer.setEmail("");
		trainer.setPhone("123456789");
		
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
		trainer.setName("Test");
		trainer.setSurname("testing");
		trainer.setEmail("example@test.com");
		trainer.setPhone("");
		
		Validator validator = createValidator();
		Set<ConstraintViolation<Trainer>> constraintViolations = validator.validate(trainer);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Trainer> violation = constraintViolations.iterator().next();
		assertThat(violation.getPropertyPath().toString()).isEqualTo("phone");
		assertThat(violation.getMessage()).isEqualTo("must not be empty");
	}
	
}
