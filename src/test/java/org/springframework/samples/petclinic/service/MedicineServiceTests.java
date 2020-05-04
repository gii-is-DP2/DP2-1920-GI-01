package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import generatedAssertions.customAssertions.MedicineAssert;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MedicineServiceTests {
	
	@Autowired
	protected MedicineService medicineService;
	
	@Autowired
	protected PetService petService;
	
	@Test
	void shouldFindMedicineWithCorrectId() {
		Medicine medicine1;
		PetType petType;
		
		medicine1 = medicineService.findMedicineById(1);
		petType = EntityUtils.getById(petService.findPetTypes(), PetType.class, 1);
		
		assertThat(medicine1.getName()).startsWith("Cat medicine");
		assertThat(medicine1.getMaker()).startsWith("Maker");
		
		MedicineAssert.assertThat(medicine1).hasMaker("Maker");
		MedicineAssert.assertThat(medicine1).hasPetType(petType);
	}
	
	@Test
	void shouldNotFindMedicineWithIncorrectId() {
		Medicine medicine0;
		
		medicine0 = medicineService.findMedicineById(0);
		
		assertThrows(NullPointerException.class, () -> medicine0.getName());
		assertThat(medicine0).isNull();
	}
	
	@Test
	@Transactional
	void shouldInsertMedicineIntoDatabaseAndGenerateId() {
		Medicine medicine; 
		PetType petType;
		Integer size;
		
		medicine = new Medicine();
		petType = EntityUtils.getById(petService.findPetTypes(), PetType.class, 1);
		size = medicineService.findManyAll().size();
		
		medicine.setName("Test");
		medicine.setExpirationDate(LocalDate.now());
		medicine.setMaker("Test");
		medicine.setPetType(petType);
		
        medicineService.saveMedicine(medicine);
        
        assertThat(medicineService.findManyAll().size()).isEqualTo(size + 1);
		assertThat(medicine.getId()).isNotNull();
	}
	
	@Test
	@Transactional
	void shouldNotInsertMedicineWithPetTypeIdNull() {
		Medicine medicine;
		PetType petType;
		InvalidDataAccessApiUsageException exception;
		
		medicine = new Medicine();
		petType = new PetType();
		
		petType.setName("dog");
		medicine.setName("Test");
		medicine.setExpirationDate(LocalDate.now());
		medicine.setMaker("Test");
		medicine.setPetType(petType);
		
        exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> medicineService.saveMedicine(medicine));
        
        assertThat(exception.getMessage()).contains("Not-null property references a transient value");
	}
	
	@Test
	@Transactional
	void shouldNotInsertMedicineWithNameBlank() {
		Medicine medicine;
		PetType petType;
		ConstraintViolationException exception;
		
		medicine = new Medicine();
		petType = new PetType();
		
		petType.setName("dog");
		petType.setId(2);
		medicine.setName("");
		medicine.setExpirationDate(LocalDate.now());
		medicine.setMaker("Test");
		medicine.setPetType(petType);
		
        exception = assertThrows(ConstraintViolationException.class, () -> medicineService.saveMedicine(medicine));
        
        assertThat(exception.getMessage()).contains("Validation failed");
	}
	
	@Test
	@Transactional
	void shouldDeleteMedicineWithCorrectId() {
		Medicine medicine1; 
		
		medicine1 = medicineService.findMedicineById(1);
		
		if(medicine1 != null) {
			medicineService.deleteMedicine(medicine1);
		}
		
		medicine1 = medicineService.findMedicineById(1);
		assertThat(medicine1).isNull();
	}
	
	@Test
	@Transactional
	void shoulNotDeleteMedicineWithIncorrectId() {
		Medicine medicine0;
		InvalidDataAccessApiUsageException exception;
		
		medicine0 = medicineService.findMedicineById(0);
		exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> medicineService.deleteMedicine(medicine0));
		
		assertThat(exception.getMessage()).contains("Entity must not be null");
	}
	
	@Test
	void shouldListMedicinesWithCorrectString() {
		Collection<Medicine> medicines;
		String query;
		Medicine medicine1;
		
		query = "";
		medicines = medicineService.findManyMedicineByName(query);
		medicine1 = medicineService.findMedicineById(1);
		
		assertThat(medicines).isNotNull();
		assertThat(medicines).isNotEmpty();
		assertThat(medicines).contains(medicine1);
	}
	
	@Test
	void shouldListMedicinesWithIncorrectString() {
		InvalidDataAccessApiUsageException exception;
		
		exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> medicineService.findManyMedicineByName(null));
		
		assertThat(exception.getMessage()).contains("Value must not be null");
	}
	
	@Test
	void shouldListAllMedicines() {
		Collection<Medicine> medicines;
		Medicine medicine1;
		
		medicines = medicineService.findManyAll();
		medicine1 = medicineService.findMedicineById(1);
		
		assertThat(medicines).isNotNull();
		assertThat(medicines).isNotEmpty();
		assertThat(medicines).contains(medicine1);
	}
	
	@Test
	void shouldListMedicinesWithCorrectPetType() {
		Collection<Medicine> medicines;
		PetType petType;
		Medicine medicine1;
		
		petType = new PetType();
		petType.setId(1);
		petType.setName("cat");
		
		medicines = medicineService.findByPetType(petType);
		medicine1 = medicineService.findMedicineById(1);
		
		assertThat(medicines).isNotNull();
		assertThat(medicines).isNotEmpty();
		assertThat(medicines).contains(medicine1);
	}
	
	@Test
	void shouldNotListMedicinesWithIncorrectPetType() {
		PetType petType;
		InvalidDataAccessApiUsageException exception;
		
		petType = new PetType();
		
		exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> medicineService.findByPetType(petType));
		
		assertThat(exception.getMessage()).contains("object references an unsaved transient instance");
	}

}
