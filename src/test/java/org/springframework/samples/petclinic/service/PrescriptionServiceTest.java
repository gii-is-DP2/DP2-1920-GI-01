package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class PrescriptionServiceTest {

	@Autowired
	private PrescriptionService prescriptionService;
	
	@Autowired
	private MedicalRecordService medicalRecordService;
	
	@Autowired
	private MedicineService medicineService;
	
	@Test
	@Transactional
	void shouldInsertPrescriptionAndGenerateId() {
		Prescription prescription;
		MedicalRecord medicalRecord;
		Medicine medicine;
		Integer size;
		
		prescription = new Prescription();
		medicalRecord = EntityUtils.getById(medicalRecordService.findMedicalHistory(), MedicalRecord.class, 1);
		medicine = EntityUtils.getById(medicineService.findManyAll(), Medicine.class, 1);
		size = prescriptionService.findManyByMedicalRecord(medicalRecord).size();
		
		prescription.setMedicine(medicine);
		prescription.setMedicalRecord(medicalRecord);
		prescription.setDose("Test");
		
		prescriptionService.savePrescription(prescription);
		
		assertThat(prescriptionService.findManyByMedicalRecord(medicalRecord).size()).isEqualTo(size + 1);
		assertThat(prescription.getId()).isNotNull();
	}
	
	@Test
	@Transactional
	void shouldNotInsertPrescriptionWithErrors() {
		Prescription prescription;
		MedicalRecord medicalRecord;
		Medicine medicine;
		ConstraintViolationException exception;
		
		prescription = new Prescription();
		medicalRecord = EntityUtils.getById(medicalRecordService.findMedicalHistory(), MedicalRecord.class, 1);
		medicine = EntityUtils.getById(medicineService.findManyAll(), Medicine.class, 1);
		
		prescription.setMedicine(medicine);
		prescription.setMedicalRecord(medicalRecord);
		
		exception = assertThrows(ConstraintViolationException.class, () -> prescriptionService.savePrescription(prescription));
		
		assertThat(exception.getMessage()).contains("must not be blank");
	}
	
	@Test
	void shouldFindPrescriptionsWithCorrectMedicalRecord() {
		Collection<Prescription> prescriptions;
		MedicalRecord medicalRecord;

		medicalRecord = EntityUtils.getById(medicalRecordService.findMedicalHistory(), MedicalRecord.class, 1);
		prescriptions = prescriptionService.findManyByMedicalRecord(medicalRecord);
		
		assertThat(prescriptions).isNotEmpty();
		assertThat(prescriptions).isNotNull();
	}
	
	@Test
	void shouldNotFindPrescriptionsWithMedicalRecordNull() {
		Collection<Prescription> prescriptions;
		MedicalRecord medicalRecord;
		
		medicalRecord = null;
		prescriptions = prescriptionService.findManyByMedicalRecord(medicalRecord);
		
		assertThat(prescriptions).isEmpty();
	}
	
	@Test
	@Transactional
	void shouldDeleteAllPrescriptionsWithCorrectMedicalRecord() {
		Collection<Prescription> prescriptions;
		MedicalRecord medicalRecord;

		medicalRecord = EntityUtils.getById(medicalRecordService.findMedicalHistory(), MedicalRecord.class, 1);

		prescriptionService.deleteAllAssociated(medicalRecord);
		
		prescriptions = prescriptionService.findManyByMedicalRecord(medicalRecord);
		assertThat(prescriptions).isEmpty();
	}
	
}
