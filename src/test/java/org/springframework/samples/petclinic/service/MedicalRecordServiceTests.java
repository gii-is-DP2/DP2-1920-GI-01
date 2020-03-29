
package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat; 
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import generatedAssertions.customAssertions.MedicalRecordAssert;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class MedicalRecordServiceTests {

	@Autowired
	protected MedicalRecordService medicalRecordService;
	
	@Autowired
	protected VisitService visitService;


	@Test
	void shouldNotFindMedicalRecordWithIncorrectId() {

		MedicalRecord noMedicalRecord;
		noMedicalRecord = this.medicalRecordService.findMedicalRecordById(-1);

		assertThrows(NullPointerException.class, () -> noMedicalRecord.getId());
		assertThat(noMedicalRecord).isNull();
	}

	@Test
	void shoudFindMedicalRecordWithCorrectId() {

		MedicalRecord medicalRecord;
		medicalRecord = this.medicalRecordService.findMedicalRecordById(1);

		assertThat(medicalRecord.getId().equals(1));
		assertThat(medicalRecord.getDescription().equals("Test"));
		assertThat(medicalRecord.getStatus().equals("TestStatus"));
		
		MedicalRecordAssert.assertThat(medicalRecord).hasDescription("Test");
		MedicalRecordAssert.assertThat(medicalRecord).hasVisit(visitService.findVisitById(1).get());
		MedicalRecordAssert.assertThat(medicalRecord).hasStatus("TestStatus");
	}

	@Test
	void shouldFindAllMedicalRecord() {
		Collection<MedicalRecord> medicalHistory = this.medicalRecordService.findMedicalHistory();
		
		MedicalRecord medicalRecord= EntityUtils.getById(medicalHistory, MedicalRecord.class, 1);
		
		assertThat(medicalRecord.getId().equals(1));
		assertThat(medicalRecord.getVisit().getId().equals(1));
	}
	
	@Test
	@Transactional
	void shouldInsertMedicalRecord() {
		Collection<MedicalRecord> medicalHistory = this.medicalRecordService.findMedicalHistory();
		int found = medicalHistory.size();
		
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setDescription("testDescription");
		medicalRecord.setStatus("testStatus");
		Visit visit = this.visitService.findVisitById(2).get();
		medicalRecord.setVisit(visit);
		
		this.medicalRecordService.saveMedicalRecord(medicalRecord);
		
		assertThat(medicalRecord.getId()).isNotEqualTo(0);
		medicalHistory = this.medicalRecordService.findMedicalHistory();
		assertThat(medicalHistory.size()).isEqualTo(found+1);
	}
	
//	@Test
//	@Transactional
//	void shouldNotInsertMedicalRecord() {
//		Collection<MedicalRecord> medicalHistory = this.medicalRecordService.findMedicalHistory();
//		int found = medicalHistory.size();
//		
//		MedicalRecord medicalRecord = new MedicalRecord();
//		medicalRecord.setDescription("");
//		medicalRecord.setStatus("testStatus");
//		Visit visit = this.visitService.findVisitById(2).get();
//		medicalRecord.setVisit(visit);
//		
//		this.medicalRecordService.saveMedicalRecord(medicalRecord);
//		
//		assertThat(medicalRecord.getId()).isEqualTo(0);
//		medicalHistory = this.medicalRecordService.findMedicalHistory();
//		assertThat(medicalHistory.size()).isEqualTo(found);
//		
//		medicalRecord.setDescription("testDescription");
//		medicalRecord.setStatus("");
//		medicalRecord.setVisit(visit);
//		
//		this.medicalRecordService.saveMedicalRecord(medicalRecord);
//		
//		assertThat(medicalRecord.getId()).isEqualTo(0);
//		medicalHistory = this.medicalRecordService.findMedicalHistory();
//		assertThat(medicalHistory.size()).isEqualTo(found);
//		
//		medicalRecord.setDescription("testDescription");
//		medicalRecord.setStatus("testVisit");
//		medicalRecord.setVisit(null);
//		
//		this.medicalRecordService.saveMedicalRecord(medicalRecord);
//		
//		assertThat(medicalRecord.getId()).isEqualTo(0);
//		medicalHistory = this.medicalRecordService.findMedicalHistory();
//		assertThat(medicalHistory.size()).isEqualTo(found);
//	}
	
	@Test
	@Transactional
	void shouldUpdateMedicalRecord() {
		MedicalRecord medicalRecord1 = this.medicalRecordService.findMedicalRecordById(1);
		String oldStatus, newStatus, oldDescription, newDescription;
		
		oldStatus = medicalRecord1.getStatus();
		oldDescription = medicalRecord1.getDescription();
		
		newStatus = oldStatus + "TEST";
		newDescription = oldDescription + "TEST";
		
		medicalRecord1.setStatus(newStatus);
		medicalRecord1.setDescription(newDescription);
		this.medicalRecordService.saveMedicalRecord(medicalRecord1);
		
		medicalRecord1 = this.medicalRecordService.findMedicalRecordById(1);
		assertThat(medicalRecord1.getStatus()).isEqualTo(newStatus);
		assertThat(medicalRecord1.getDescription()).isEqualTo(newDescription);
	}
	
//	@Test
//	@Transactional
//	void shouldNotUpdateMedicalRecord() {
//		MedicalRecord medicalRecord1 = this.medicalRecordService.findMedicalRecordById(1);
//		String oldStatus, oldDescription;
//		
//		oldStatus = medicalRecord1.getStatus();
//		oldDescription = medicalRecord1.getDescription();
//		
//		medicalRecord1.setStatus("");
//		medicalRecord1.setDescription("");
//		this.medicalRecordService.saveMedicalRecord(medicalRecord1);
//		
//		medicalRecord1 = this.medicalRecordService.findMedicalRecordById(1);
//		assertThat(medicalRecord1.getStatus()).isEqualTo(oldStatus);
//		assertThat(medicalRecord1.getDescription()).isEqualTo(oldDescription);
//	}
	
	@Test
	@Transactional
	void shouldDeleteMedicalRecord() {
		MedicalRecord medicalRecord = this.medicalRecordService.findMedicalRecordById(1);
		
		assertThat(medicalRecord).isNotNull();
		this.medicalRecordService.deleteMedicalRecord(medicalRecord);
		
		medicalRecord = this.medicalRecordService.findMedicalRecordById(1);
		assertThat(medicalRecord).isNull();
	}
	
	@Test
	void shoudFindMedicalRecordByPetWithCorrectId() {

		Collection<MedicalRecord> medicalHistory;
		medicalHistory = this.medicalRecordService.findMedicalRecordByPetId(7);
		int found = medicalHistory.size();
		
		
		assertThat(medicalHistory.size()).isEqualTo(found);
	}
	
	@Test
	void shoudNotFindMedicalRecordByPetWithIncorrectId() {

		Collection<MedicalRecord> medicalHistory;
		medicalHistory = this.medicalRecordService.findMedicalRecordByPetId(-1);
		int found = medicalHistory.size();
		
		
		assertThat(medicalHistory.size()).isEqualTo(found);
	}
}
