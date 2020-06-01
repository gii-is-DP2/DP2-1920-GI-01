package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.MedicineValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class MedicineValidatorTests extends ValidatorTests {
	
	@Test
	void shouldValidateWhenCorrect() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		PetType petType;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setName("Test");
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
		assertThat(errors.hasErrors()).isFalse();
	}

	@Test
	void shouldNotValidateWhenNameNull() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		PetType petType;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("Required");
	}
	
	@Test
	void shouldNotValidateWhenNameBlank() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		ConstraintViolation<Medicine> violation;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		PetType petType;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setName("");
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 50");
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("Required");
	}
	
	@Test
	void shouldNotValidateWhenNameLessThanThree() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		ConstraintViolation<Medicine> violation;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		PetType petType;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setName("12");
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 50");
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("required and between 3 and 50 character");
	}
	
	@Test
	void shouldValidateWhenNameExactlyThree() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		PetType petType;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setName("123");
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
		assertThat(errors.hasErrors()).isFalse();
	}
	
	@Test
	void shouldValidateWhenNameExactlyFifty() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		PetType petType;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setName("12345678901234567890123456789012345678901234567890");
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
		assertThat(errors.hasErrors()).isFalse();
	}
	
	@Test
	void shouldNotValidateWhenNameMoreThanFifty() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		ConstraintViolation<Medicine> violation;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		PetType petType;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setName("123456789012345678901234567890123456789012345678901");
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 50");
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("required and between 3 and 50 character");
		
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"123", "1234567890", "12345678901234567890123456789012345678901234567890"})
	void shouldValidateWhenNameIsCorrect(String name) {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		PetType petType;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setName(name);
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
		assertThat(errors.hasErrors()).isFalse();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"12", "123456789012345678901234567890123456789012345678901"})
	void shouldNotValidateWhenNameIsNotCorrect(String name) {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		ConstraintViolation<Medicine> violation;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		PetType petType;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setName(name);
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		assertThat(violation.getMessage()).isEqualTo("size must be between 3 and 50");
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("name").getDefaultMessage()).isEqualTo("required and between 3 and 50 character");
	}
	
	@Test
	void shouldNotValidateWhenMakerBlank() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		ConstraintViolation<Medicine> violation;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		PetType petType;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setName("Test");
		medicine.setMaker("");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("maker");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("maker").getDefaultMessage()).isEqualTo("Required");
	}
	
	@Test
	void shouldNotValidateWhenMakerNull() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		ConstraintViolation<Medicine> violation;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		PetType petType;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setName("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("maker");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("maker").getDefaultMessage()).isEqualTo("Required");
	}
	
	@Test
	void shouldNotValidateWhenExpirationDateNull() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		MedicineValidator customValidator;
		Errors errors;
		ConstraintViolation<Medicine> violation;
		PetType petType;
		
		validator = createValidator();
		petType = new PetType();
		medicine = new Medicine();
		
		petType.setName("dog");
		medicine.setName("Test");
		medicine.setMaker("Test");
		medicine.setExpirationDate(null);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("expirationDate");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("expirationDate").getDefaultMessage()).isEqualTo("Required");
	}
	
	@Test
	void shouldNotValidateWhenExpirationDatePast() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		MedicineValidator customValidator;
		Errors errors;
		PetType petType;
		LocalDate expirationDate;
		DateTimeFormatter formatter;
		
		validator = createValidator();
		petType = new PetType();
		medicine = new Medicine();
		formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		expirationDate = LocalDate.parse("2000/01/01", formatter);
		
		petType.setName("dog");
		medicine.setName("Test");
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("expirationDate").getDefaultMessage()).isEqualTo("The expiration date must be in the future");
	}
	
	@Test
	void shouldValidateWhenExpirationDateNow() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		MedicineValidator customValidator;
		Errors errors;
		PetType petType;
		LocalDate expirationDate;
		
		validator = createValidator();
		petType = new PetType();
		medicine = new Medicine();
		expirationDate = LocalDate.now();
		
		petType.setName("dog");
		medicine.setName("Test");
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
		assertThat(errors.hasErrors()).isFalse();
	}
	
	@Test
	void shouldValidateWhenExpirationDateFuture() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		MedicineValidator customValidator;
		Errors errors;
		PetType petType;
		LocalDate expirationDate;
		DateTimeFormatter formatter;
		
		validator = createValidator();
		petType = new PetType();
		medicine = new Medicine();
		formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		expirationDate = LocalDate.parse("2021/01/01", formatter);
		
		petType.setName("dog");
		medicine.setName("Test");
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		medicine.setPetType(petType);
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
		assertThat(errors.hasErrors()).isFalse();
	}
	
	@Test
	void shouldNotValidateWhenPetTypeNull() {
		Medicine medicine;
		Validator validator;
		Set<ConstraintViolation<Medicine>> constraintViolations;
		ConstraintViolation<Medicine> violation;
		MedicineValidator customValidator;
		Errors errors;
		LocalDate expirationDate;
		
		validator = createValidator();
		expirationDate = LocalDate.now();
		medicine = new Medicine();
		
		medicine.setName("Test");
		medicine.setMaker("Test");
		medicine.setExpirationDate(expirationDate);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		customValidator = new MedicineValidator();
		errors = new BeanPropertyBindingResult(medicine, "medicine");
		customValidator.validate(medicine, errors);
		constraintViolations = validator.validate(medicine);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("petType");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("petType").getDefaultMessage()).isEqualTo("Required");
	}
	
}
