
package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.web.InterventionController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class InterventionControllerE2ETest {

	private static final int		TEST_PET_ID	= 1;

	@Autowired
	private InterventionController	interventionController;

	@Autowired
	private MockMvc					mockMvc;


	@BeforeEach
	void setup() {
		//BDDMockito.given(this.petService.findPetById(InterventionControllerE2ETest.TEST_PET_ID)).willReturn(new Pet());
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testInitNewInterventionForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/{petId}/interventions/new", InterventionControllerE2ETest.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateInterventionForm"));
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testProcessNewInterventionFormSuccess() throws Exception {
		this.mockMvc
			.perform(MockMvcRequestBuilders.post("/owners/*/pets/{petId}/interventions/new", InterventionControllerE2ETest.TEST_PET_ID).param("name", "George").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("interventionDescription", "Intervention Description").param("interventionTime", "1").param("interventionDate", "2020/12/20"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testProcessNewVisitFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/{petId}/interventions/new", InterventionControllerE2ETest.TEST_PET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "George"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("intervention")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateInterventionForm"));
	}

}
