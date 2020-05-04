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
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AdoptionServiceTests {
	
	@Autowired
	protected AdoptionService adoptionService;
	
	@Autowired
	protected OwnerService ownerService;
	
	@Autowired
	protected PetService petService;

	@Test
	void shouldInsertAdoptionIntoDatabaseAndGenerateId() {
		Adoption adoption;
		LocalDate date;
		Owner owner;
		Pet pet;
		Integer size;
		
		adoption = new Adoption();
		date = LocalDate.now();
		pet = petService.findPetById(1);
		owner = ownerService.findOwnerById(1);
		size = adoptionService.findManyAll().size();
		
		adoption.setDate(date);
		adoption.setOwner(owner);
		adoption.setPet(pet);
		
		adoptionService.saveAdoption(adoption);
		
		assertThat(adoptionService.findManyAll().size()).isEqualTo(size + 1);
		assertThat(adoption.getId()).isNotNull();
	}
	
	@Test
	void shouldNotInsertAdoptionWhenPetNull() {
		Adoption adoption;
		LocalDate date;
		Owner owner;
		ConstraintViolationException exception;
		
		adoption = new Adoption();
		date = LocalDate.now();
		owner = ownerService.findOwnerById(1);
		
		adoption.setDate(date);
		adoption.setOwner(owner);
		
		exception = assertThrows(ConstraintViolationException.class, () -> adoptionService.saveAdoption(adoption));
		
		assertThat(exception.getMessage()).contains("propertyPath=pet");
	}
	
	@Test
	void shouldListAdoptionsWithCorrectPetId() {
		Collection<Adoption> adoptions;
		
		adoptions = adoptionService.findByPetId(1);
		
		assertThat(adoptions).isNotNull();
		assertThat(adoptions).isNotEmpty();
	}
	
	@Test
	void shouldNotListAdoptionsWithIncorrectPetId() {
		Collection<Adoption> adoptions;
		
		adoptions = adoptionService.findByPetId(null);
		
		assertThat(adoptions).isNotNull();
		assertThat(adoptions).isEmpty();
	}
	
	@Test
	void shouldListAllAdoptions() {
		Collection<Adoption> adoptions;
		
		adoptions = adoptionService.findManyAll();
		
		assertThat(adoptions).isNotNull();
		assertThat(adoptions).isNotEmpty();
	}
	
}
