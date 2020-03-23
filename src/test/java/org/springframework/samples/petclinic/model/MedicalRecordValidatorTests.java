
package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

public class MedicalRecordValidatorTests extends ValidatorTests {

	@Test
	void shouldValidateWhenCorrect() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setDescription("testDescription");
		medicalRecord.setStatus("testStatus");
		Visit visit = new Visit();
		visit.setId(1);
		medicalRecord.setVisit(visit);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<MedicalRecord>> constraintViolations = validator.validate(medicalRecord);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenDescriptionEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setDescription("");
		medicalRecord.setStatus("testStatus");
		Visit visit = new Visit();
		visit.setId(1);
		medicalRecord.setVisit(visit);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<MedicalRecord>> constraintViolations = validator.validate(medicalRecord);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<MedicalRecord> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}

	@Test
	void shouldNotValidateWhenStatusEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setDescription("testDescription");
		medicalRecord.setStatus("");
		Visit visit = new Visit();
		visit.setId(1);
		medicalRecord.setVisit(visit);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<MedicalRecord>> constraintViolations = validator.validate(medicalRecord);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<MedicalRecord> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("status");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}
}
