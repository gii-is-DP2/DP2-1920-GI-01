package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.validators.PrescriptionValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class PrescriptionValidatorTest extends ValidatorTests {

	@Test
	void shouldValidateWhenCorrect() {
		Prescription prescription;
		Medicine medicine;
		MedicalRecord medicalRecord;
		Visit visit;
		Pet pet;
		PetType petType;
		Validator validator;
		Set<ConstraintViolation<Prescription>> constraintViolations;
		PrescriptionValidator customValidator;
		Errors errors;
		
		medicine = new Medicine();
		medicalRecord = new MedicalRecord();
		visit = new Visit();
		pet = new Pet();
		petType = new PetType();
		validator = createValidator();
		customValidator = new PrescriptionValidator();
		prescription = new Prescription();
		
		petType.setName("dog");
		pet.setType(petType);
		visit.setPet(pet);
		medicalRecord.setVisit(visit);
		medicine.setPetType(petType);
		prescription.setMedicine(medicine);
		prescription.setMedicalRecord(medicalRecord);
		prescription.setDose("Test");
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		constraintViolations = validator.validate(prescription);
		errors = new BeanPropertyBindingResult(prescription, "prescription");
		customValidator.validate(prescription, errors);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
		assertThat(errors.hasErrors()).isFalse();
	}
	
	@Test
	void shouldNotValidateWhenMedicalRecordNull() {
		Prescription prescription;
		Medicine medicine;
		PetType petType;
		Validator validator;
		Set<ConstraintViolation<Prescription>> constraintViolations;
		ConstraintViolation<Prescription> violation;
//		PrescriptionValidator customValidator;
//		Errors errors;
		
		medicine = new Medicine();
		petType = new PetType();
		validator = createValidator();
//		customValidator = new PrescriptionValidator();
		prescription = new Prescription();
		
		petType.setName("dog");
		medicine.setPetType(petType);
		prescription.setMedicine(medicine);
		prescription.setDose("Test");
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		constraintViolations = validator.validate(prescription);
//		errors = new BeanPropertyBindingResult(prescription, "prescription");
//		customValidator.validate(prescription, errors);
		violation = constraintViolations.iterator().next();
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("medicalRecord");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
//		assertThat(errors.hasErrors()).isFalse();
	}
	
	@Test
	void shouldNotValidateWhenCorrectMedicineNull() {
		Prescription prescription;
		MedicalRecord medicalRecord;
		Validator validator;
		Set<ConstraintViolation<Prescription>> constraintViolations;
		ConstraintViolation<Prescription> violation;
		PrescriptionValidator customValidator;
		Errors errors;
		
		medicalRecord = new MedicalRecord();
		validator = createValidator();
		customValidator = new PrescriptionValidator();
		prescription = new Prescription();
		
		prescription.setMedicalRecord(medicalRecord);
		prescription.setDose("Test");
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		constraintViolations = validator.validate(prescription);
		violation = constraintViolations.iterator().next();
		errors = new BeanPropertyBindingResult(prescription, "prescription");
		customValidator.validate(prescription, errors);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("medicine");
		assertThat(violation.getMessage()).isEqualTo("must not be null");
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("medicine").toString()).contains("Field error");
	}
	
	@Test
	void shouldNotValidateWhenMedicineTypeDistinctPetType() {
		Prescription prescription;
		Medicine medicine;
		MedicalRecord medicalRecord;
		Visit visit;
		Pet pet;
		PetType petTypeMedicine;
		PetType petTypePet;
		Validator validator;
		Set<ConstraintViolation<Prescription>> constraintViolations;
		PrescriptionValidator customValidator;
		Errors errors;
		
		medicine = new Medicine();
		medicalRecord = new MedicalRecord();
		visit = new Visit();
		pet = new Pet();
		petTypeMedicine = new PetType();
		petTypePet = new PetType();
		validator = createValidator();
		customValidator = new PrescriptionValidator();
		prescription = new Prescription();
		
		petTypeMedicine.setName("dog");
		petTypePet.setName("cat");
		pet.setType(petTypePet);
		visit.setPet(pet);
		medicalRecord.setVisit(visit);
		medicine.setPetType(petTypeMedicine);
		prescription.setMedicine(medicine);
		prescription.setMedicalRecord(medicalRecord);
		prescription.setDose("Test");
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		constraintViolations = validator.validate(prescription);
		errors = new BeanPropertyBindingResult(prescription, "prescription");
		customValidator.validate(prescription, errors);
		
		assertThat(constraintViolations.size()).isEqualTo(0);
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("medicine").getDefaultMessage()).isEqualTo("That medicine does not correspond with the pet type");
	}
	
	@Test
	void shouldNotValidateWhenDoseNull() {
		Prescription prescription;
		Medicine medicine;
		MedicalRecord medicalRecord;
		Visit visit;
		Pet pet;
		PetType petType;
		Validator validator;
		Set<ConstraintViolation<Prescription>> constraintViolations;
		ConstraintViolation<Prescription> violation;
		PrescriptionValidator customValidator;
		Errors errors;
		
		medicine = new Medicine();
		medicalRecord = new MedicalRecord();
		visit = new Visit();
		pet = new Pet();
		petType = new PetType();
		validator = createValidator();
		customValidator = new PrescriptionValidator();
		prescription = new Prescription();
		
		petType.setName("dog");
		pet.setType(petType);
		visit.setPet(pet);
		medicalRecord.setVisit(visit);
		medicine.setPetType(petType);
		prescription.setMedicine(medicine);
		prescription.setMedicalRecord(medicalRecord);
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		constraintViolations = validator.validate(prescription);
		violation = constraintViolations.iterator().next();
		errors = new BeanPropertyBindingResult(prescription, "prescription");
		customValidator.validate(prescription, errors);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("dose");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("dose").getDefaultMessage()).isEqualTo("Must not be blank");
	}
	
	@Test
	void shouldNotValidateWhenDoseBlank() {
		Prescription prescription;
		Medicine medicine;
		MedicalRecord medicalRecord;
		Visit visit;
		Pet pet;
		PetType petType;
		Validator validator;
		Set<ConstraintViolation<Prescription>> constraintViolations;
		ConstraintViolation<Prescription> violation;
		PrescriptionValidator customValidator;
		Errors errors;
		
		medicine = new Medicine();
		medicalRecord = new MedicalRecord();
		visit = new Visit();
		pet = new Pet();
		petType = new PetType();
		validator = createValidator();
		customValidator = new PrescriptionValidator();
		prescription = new Prescription();
		
		petType.setName("dog");
		pet.setType(petType);
		visit.setPet(pet);
		medicalRecord.setVisit(visit);
		medicine.setPetType(petType);
		prescription.setMedicine(medicine);
		prescription.setMedicalRecord(medicalRecord);
		prescription.setDose("");
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		
		constraintViolations = validator.validate(prescription);
		violation = constraintViolations.iterator().next();
		errors = new BeanPropertyBindingResult(prescription, "prescription");
		customValidator.validate(prescription, errors);
		
		assertThat(constraintViolations.size()).isEqualTo(1);
		assertThat(violation.getPropertyPath().toString()).isEqualTo("dose");
		assertThat(violation.getMessage()).isEqualTo("must not be blank");
		assertThat(errors.hasErrors()).isTrue();
		assertThat(errors.getFieldError("dose").getDefaultMessage()).isEqualTo("Must not be blank");
	}
	
}
