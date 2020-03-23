package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class MedicineServiceTests {
	
	@Autowired
	protected MedicineService medicineService;
	
	@Autowired
	protected PetService petService;
	
	@Test
	void shouldFindMedicineWithCorrectId() {
		Medicine medicine1;
		
		medicine1 = medicineService.findMedicineById(1);
		
		assertThat(medicine1.getName()).startsWith("Test");
		assertThat(medicine1.getMaker()).startsWith("Maker");
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
		
		medicine = new Medicine();
		petType = EntityUtils.getById(petService.findPetTypes(), PetType.class, 1);
		
		medicine.setName("Test");
		medicine.setExpirationDate(LocalDate.now());
		medicine.setMaker("Test");
		medicine.setPetType(petType);
		
        try {
            medicineService.saveMedicine(medicine);
        } catch (Exception ex) {
            Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		//ADD ASSERT WITH SIZE OF LIST + 1 (Made in US-002)
		
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

}
