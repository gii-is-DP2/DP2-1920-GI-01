
package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.web.VisitController;
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
/*
 * @TestPropertySource(
 * locations = "classpath:application-mysql.properties")
 */
public class VisitControllerE2ETests {

	private static final int	TEST_PET_ID	= 1;

	@Autowired
	private VisitController		visitController;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {
		//BDDMockito.given(this.petService.findPetById(VisitControllerE2ETests.TEST_PET_ID)).willReturn(new Pet());
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testInitNewVisitForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/{petId}/visits/new", VisitControllerE2ETests.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"));
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testProcessNewVisitFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/{petId}/visits/new", VisitControllerE2ETests.TEST_PET_ID).param("name", "George").with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Visit Description"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testProcessNewVisitFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/{petId}/visits/new", VisitControllerE2ETests.TEST_PET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("date", ""))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("visit")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"));
	}

	@WithMockUser(username = "vet1", authorities = {
		"veterinarian"
	})
	@Test
	void testShowVisits() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/{petId}/visits", VisitControllerE2ETests.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("visits"))
			.andExpect(MockMvcResultMatchers.view().name("visitList"));
	}

}
