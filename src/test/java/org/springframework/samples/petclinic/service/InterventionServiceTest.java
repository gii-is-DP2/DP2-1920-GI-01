
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import generatedAssertions.customAssertions.InterventionAssert;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class InterventionServiceTest {

	@Autowired
	protected PetService petService;


	@Test
	@Transactional
	public void shouldAddNewInterventionForPet() {
		Pet pet1 = this.petService.findPetById(1);
		int found = pet1.getInterventions().size();
		Intervention intervention = new Intervention();
		pet1.addIntervention(intervention);
		intervention.setInterventionDescription("test");
		intervention.setInterventionDate(LocalDate.of(2020, 07, 07));
		intervention.setInterventionTime(1);
		this.petService.saveIntervention(intervention);
		try {
			this.petService.savePet(pet1);
		} catch (DuplicatedPetNameException ex) {
			Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}

		pet1 = this.petService.findPetById(1);
		Assertions.assertThat(pet1.getInterventions().size()).isEqualTo(found + 1);
		Assertions.assertThat(intervention.getId()).isNotNull();

		//Custom Assertions
		InterventionAssert.assertThat(intervention).hasInterventionDate(LocalDate.of(2020, 07, 07));
		InterventionAssert.assertThat(intervention).hasInterventionDescription("test");
		InterventionAssert.assertThat(intervention).hasInterventionTime(1);
	}

	//	@Test
	//	void shouldFindInterventionsByPetId() throws Exception {
	//		Collection<Intervention> interventions = this.petService.findInterventionsByPetId(7);
	//		Assertions.assertThat(interventions.size()).isEqualTo(2);
	//		Intervention[] interventionArr = interventions.toArray(new Intervention[interventions.size()]);
	//		Assertions.assertThat(interventionArr[0].getPet()).isNotNull();
	//		Assertions.assertThat(interventionArr[0].getInterventionDate()).isNotNull();
	//		Assertions.assertThat(interventionArr[0].getPet().getId()).isEqualTo(7);
	//	}
}
