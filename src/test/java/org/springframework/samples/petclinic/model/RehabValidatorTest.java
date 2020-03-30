
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.RehabValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class RehabValidatorTest extends ValidatorTests {

	@Test
	void TestWhenEvrythingIsCorrect() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Rehab rehab = new Rehab();

		/* information about the rehab */
		rehab.setDate(LocalDate.of(2020, Month.MARCH, 31));
		rehab.setTime(3);
		rehab.setDescription("RehabDescriptionTesting");

		/* information about the pet */
		Pet pet = new Pet();
		pet.setName("Duksis");
		pet.setBirthDate(LocalDate.of(2020, Month.MARCH, 12));
		rehab.setPet(pet);

		/* information about the trainer */
		Trainer trainer = new Trainer();
		trainer.setFirstName("Marco");
		trainer.setLastName("Polo");
		rehab.setTrainer(trainer);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Rehab>> constraintViolations = validator.validate(rehab);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	/* Unit test for rehabilitation date, date cannot be an empty field */

	@Test
	void rehabDateIsNotSet() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Rehab rehab = new Rehab();

		rehab.setDate(null);
		rehab.setTime(3);
		rehab.setDescription("RehabDescriptionTesting");

		Pet pet = new Pet();
		pet.setName("Duksis");
		pet.setBirthDate(LocalDate.of(2020, Month.MARCH, 12));
		rehab.setPet(pet);

		Trainer trainer = new Trainer();
		trainer.setFirstName("Marco");
		trainer.setLastName("Polo");
		rehab.setTrainer(trainer);

		RehabValidator rehabValidator = new RehabValidator();
		Errors errors = new BeanPropertyBindingResult(rehab, "rehab");
		rehabValidator.validate(rehab, errors);

		Assertions.assertThat(errors.hasFieldErrors("date")).isEqualTo(true);
	}

	/* Unit test for rehabilitation date, date has to be current date, cannot be an after date */

	@Test
	void rehabDateIsSetAsABeforeDate() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Rehab rehab = new Rehab();

		rehab.setDate(LocalDate.of(2019, Month.JANUARY, 1));
		rehab.setTime(3);
		rehab.setDescription("RehabDescriptionTesting");

		Pet pet = new Pet();
		pet.setName("Duksis");
		pet.setBirthDate(LocalDate.of(2020, Month.MARCH, 12));
		rehab.setPet(pet);

		Trainer trainer = new Trainer();
		trainer.setFirstName("Marco");
		trainer.setLastName("Polo");
		rehab.setTrainer(trainer);

		RehabValidator rehabValidator = new RehabValidator();
		Errors errors = new BeanPropertyBindingResult(rehab, "rehab");
		rehabValidator.validate(rehab, errors);

		Assertions.assertThat(errors.hasFieldErrors("date")).isEqualTo(true);
		Assertions.assertThat(errors.getFieldError("date").getDefaultMessage()).startsWith("required is after current date");
	}

	/* Unit test for rehabilitation date, date has to be current date, cannot be a before date */

	@Test
	void rehabDateIsSetAsAnAfterDate() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Rehab rehab = new Rehab();

		rehab.setDate(LocalDate.of(2021, Month.JANUARY, 1));
		rehab.setTime(3);
		rehab.setDescription("RehabDescriptionTesting");

		Pet pet = new Pet();
		pet.setName("Duksis");
		pet.setBirthDate(LocalDate.of(2020, Month.MARCH, 12));
		rehab.setPet(pet);

		Trainer trainer = new Trainer();
		trainer.setFirstName("Marco");
		trainer.setLastName("Polo");
		rehab.setTrainer(trainer);

		RehabValidator rehabValidator = new RehabValidator();
		Errors errors = new BeanPropertyBindingResult(rehab, "rehab");
		rehabValidator.validate(rehab, errors);

		Assertions.assertThat(errors.hasFieldErrors("date")).isEqualTo(true);
		Assertions.assertThat(errors.getFieldError("date").getDefaultMessage()).startsWith("required is after current date");
	}

	/* Unit test for rehabilitation time, time cannot be an empty field */

	@Test
	void rehabTimeIsNotSet() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Rehab rehab = new Rehab();

		rehab.setDate(LocalDate.of(2020, Month.MARCH, 29));
		rehab.setDescription("RehabDescription");

		Pet pet = new Pet();
		pet.setName("Duksis");
		pet.setBirthDate(LocalDate.of(2020, Month.MARCH, 12));
		rehab.setPet(pet);

		Trainer trainer = new Trainer();
		trainer.setFirstName("Marco");
		trainer.setLastName("Polo");
		rehab.setTrainer(trainer);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Rehab>> constraintViolations = validator.validate(rehab);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Rehab> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("time");
	}

	/* Unit test for rehabilitation description, it cannot be an empty field */

	void rehabDescriptionIsNotSet() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Rehab rehab = new Rehab();

		rehab.setDate(LocalDate.of(2020, Month.MARCH, 29));
		rehab.setTime(2);
		rehab.setDescription(null);

		Pet pet = new Pet();
		pet.setName("Duksis");
		pet.setBirthDate(LocalDate.of(2020, Month.MARCH, 12));
		rehab.setPet(pet);

		Trainer trainer = new Trainer();
		trainer.setFirstName("Marco");
		trainer.setLastName("Polo");
		rehab.setTrainer(trainer);

		RehabValidator rehabValidator = new RehabValidator();
		Errors errors = new BeanPropertyBindingResult(rehab, "rehab");
		rehabValidator.validate(rehab, errors);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Rehab>> constraintViolations = validator.validate(rehab);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Rehab> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("rehabDescription");
	}

}
