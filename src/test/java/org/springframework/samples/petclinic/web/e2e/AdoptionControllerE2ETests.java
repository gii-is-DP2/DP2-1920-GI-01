package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*@TestPropertySource(locations = "classpath:application-mysql.properties")*/
class AdoptionControllerE2ETests {
	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/14/adopt"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("adoption/form"))
			   .andExpect(model().attributeExists("adoption"))
			   .andExpect(model().attributeExists("owners"));	
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProccessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/14/adopt")
						.with(csrf())
						.param("owner", "George Franklin"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(view().name("redirect:/owners/1"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProccessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/14/adopt")
								.with(csrf()))
		       .andExpect(status().isOk())
		       .andExpect(view().name("adoption/form"))
		       .andExpect(model().attributeExists("owners"))
		       .andExpect(model().attributeExists("adoption"))
		       .andExpect(model().attributeHasErrors("adoption"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testListAdoption() throws Exception {
		mockMvc.perform(get("/owners/*/pets/1/adoption-history"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("adoption/list"))
			   .andExpect(model().attributeExists("adoptions"));
	}
	
}
