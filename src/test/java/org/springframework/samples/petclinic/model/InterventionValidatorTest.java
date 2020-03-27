
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.InterventionValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class InterventionValidatorTest extends ValidatorTests {

	@Test
	void shouldValiteWhenCorrect() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Intervention intervention = new Intervention();
		intervention.setInterventionDate(LocalDate.now());
		intervention.setInterventionDescription("Quick intervention");
		intervention.setInterventionTime(1);
		Vet vet = new Vet();
		vet.setFirstName("Alfonso");
		vet.setLastName("Rodriguez Fernandez");
		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2008, 7, 7));
		pet.setName("Renato");
		intervention.setPet(pet);
		intervention.setVet(vet);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Intervention>> constraintViolations = validator.validate(intervention);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenDescriptionEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Intervention intervention = new Intervention();
		intervention.setInterventionDate(LocalDate.now());
		intervention.setInterventionDescription("");
		intervention.setInterventionTime(1);
		Vet vet = new Vet();
		vet.setFirstName("Alfonso");
		vet.setLastName("Rodriguez Fernandez");
		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2008, 7, 7));
		pet.setName("Renato");
		intervention.setPet(pet);
		intervention.setVet(vet);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Intervention>> constraintViolations = validator.validate(intervention);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Intervention> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("interventionDescription");
	}

	@Test
	void shouldNotValidateWhenInterventionTimeNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Intervention intervention = new Intervention();
		intervention.setInterventionDate(LocalDate.now());
		intervention.setInterventionDescription("Quick intervention");
		//intervention.setInterventionTime(1);
		Vet vet = new Vet();
		vet.setFirstName("Alfonso");
		vet.setLastName("Rodriguez Fernandez");
		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2008, 7, 7));
		pet.setName("Renato");
		intervention.setPet(pet);
		intervention.setVet(vet);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Intervention>> constraintViolations = validator.validate(intervention);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Intervention> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("interventionTime");
	}

	@Test
	void shouldNotValidateWhenInterventionDateNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Intervention intervention = new Intervention();
		intervention.setInterventionDate(null);
		intervention.setInterventionDescription("Quick intervention");
		intervention.setInterventionTime(1);
		Vet vet = new Vet();
		vet.setFirstName("Alfonso");
		vet.setLastName("Rodriguez Fernandez");
		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2008, 7, 7));
		pet.setName("Renato");
		intervention.setPet(pet);
		intervention.setVet(vet);

		InterventionValidator interventionValidator = new InterventionValidator();
		Errors errors = new BeanPropertyBindingResult(intervention, "intervention");
		interventionValidator.validate(intervention, errors);

		Assertions.assertThat(errors.hasFieldErrors("interventionDate")).isEqualTo(true);

	}
}
