/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows; 

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import generatedAssertions.customAssertions.VetAssert;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * ClinicServiceTests#clinicService clinicService}</code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class VetServiceTests {

	@Autowired
	protected VetService vetService;	

	// US-013 Administrator manages vets ---------------------------------------------------------------------------
	
	@Test
	void shouldNotFindVetWithIncorrectId() {
		Optional<Vet> noVet;
		
		noVet = this.vetService.findVetById(-1);
		
		assertThat(noVet.isPresent()).isEqualTo(false);
	}
	
	@Test
	void shouldFindVetWithCorrectId() {
		Optional<Vet> vet1;
		
		vet1 = this.vetService.findVetById(1);
	
		assertThat(vet1.isPresent()).isEqualTo(true);
		assertThat(vet1.get().getFirstName()).isEqualTo("James");
		assertThat(vet1.get().getLastName()).isEqualTo("Carter");
		
		User user = new User();
		user.setUsername("vet1");
		user.setPassword("v3t1");
		user.setEnabled(true);
		
		VetAssert.assertThat(vet1.get()).hasNoSpecialties();
		VetAssert.assertThat(vet1.get()).hasNrOfSpecialties(0);
		VetAssert.assertThat(vet1.get()).hasUser(user);
	}
	
	@Test
	void shouldFindVets() {
		Collection<Vet> vets;
		Vet vet;
		
		vets = this.vetService.findVets();
		vet = EntityUtils.getById(vets, Vet.class, 3);
		
		assertThat(vet.getLastName()).isEqualTo("Douglas");
		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties().get(0).getName()).isEqualTo("dentistry");
		assertThat(vet.getSpecialties().get(1).getName()).isEqualTo("surgery");
	}

	@Test
	void shouldFindAllSpecialties() {
		Collection<Specialty> specialties;
		Specialty specialty1, specialty2, specialty3;
		
		specialties = this.vetService.findAllSpecialty();
		specialty1 = EntityUtils.getById(specialties, Specialty.class, 1);
		specialty2 = EntityUtils.getById(specialties, Specialty.class, 2);
		specialty3 = EntityUtils.getById(specialties, Specialty.class, 3);
	
		assertThat(specialty1.getName()).isEqualTo("radiology");
		assertThat(specialty2.getName()).isEqualTo("surgery");
		assertThat(specialty3.getName()).isEqualTo("dentistry");
	}
	
	@Test
	@Transactional
	void shouldInsertVet() {
		Collection<Vet> vets;
		Vet vet = new Vet();
		User user = new User();
		int found;
		
		vets = this.vetService.findVets();
		found = vets.size();
		
		user.setUsername("testingUsername");
		user.setPassword("testingPassword");
		user.setEnabled(true);
		
		vet.setFirstName("testFirstName");
		vet.setLastName("testLastName");
		vet.setUser(user);
		
		this.vetService.saveVet(vet);
		
		assertThat(vet.getId()).isNotEqualTo(0);
		vets = this.vetService.findVets();
		assertThat(vets.size()).isEqualTo(found + 1);
	}
	
	@Test
	@Transactional
	void shouldNotInsertVetWithFirstNameBlank() {
		ConstraintViolationException exception;
		Vet vet = new Vet();
		User user = new User();
		
		user.setUsername("testVetUsername");
		user.setPassword("testVetPassword");
		user.setEnabled(true);
		
		vet.setFirstName("");
		vet.setLastName("testLastName");
		vet.setSpecialties(new ArrayList<Specialty>());
		vet.setUser(user);
		
		exception = assertThrows(ConstraintViolationException.class, () -> this.vetService.saveVet(vet));
		assertThat(exception.getMessage()).contains("Validation failed");
		
	}
	
	@Test
	@Transactional
	void shouldUpdateVetName() {
		Optional<Vet> vet1;
		String oldFirstName, newFirstName;
		
		vet1 = this.vetService.findVetById(1);
		
		assertThat(vet1.isPresent()).isEqualTo(true);
		oldFirstName = vet1.get().getFirstName();
		newFirstName = oldFirstName + "Test";
		vet1.get().setFirstName(newFirstName);
		
		this.vetService.saveVet(vet1.get());
		
		vet1 = this.vetService.findVetById(1);
		assertThat(vet1.isPresent()).isEqualTo(true);
		assertThat(vet1.get().getFirstName()).isEqualTo(newFirstName);
	}
	
	@Test
	@Transactional
	void shouldDeleteVet() {
		Optional<Vet> vet1;
		
		vet1 = this.vetService.findVetById(1);
		
		assertThat(vet1.isPresent()).isEqualTo(true);
		this.vetService.deleteVet(vet1.get());
		
		vet1 = this.vetService.findVetById(1);
		assertThat(vet1.isPresent()).isEqualTo(false);
	}

}
