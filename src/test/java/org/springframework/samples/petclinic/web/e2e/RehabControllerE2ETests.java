package org.springframework.samples.petclinic.web.e2e;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.web.RehabController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class RehabControllerE2ETests {

	private static final int TEST_PET_ID = 1;
	
	@Autowired
	private RehabController rehabController;
	
		
	@Autowired
	private MockMvc mockMvc;
	
	
	@BeforeEach
	void setup() {
		Rehab rehab = new Rehab();
		rehab.setDate(LocalDate.now());
		rehab.setTime(1);
		rehab.setDescription("First rehab");
		
		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2019, 02, 14));
		pet.setName("Reksis");

		//given(this.petService.findPetById(RehabControllerE2ETests.TEST_PET_ID)).willReturn(pet);
	}
	

	/* Opening new rehab form*/
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testAddingNewRehabForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/" + RehabControllerE2ETests.TEST_PET_ID + "/rehab/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateRehabForm"))
				.andExpect(model().attributeExists("rehab"));
	}
	
	/* Creating a new rehab, successful */
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testProcessCreateRehabFormIsSuccessful() throws Exception {
		this.mockMvc.perform(post("/owners/*/pets/" + RehabControllerE2ETests.TEST_PET_ID +"/rehab/new")//
			.with(csrf())//
			.param("date", "2020/05/09")//
			.param("time", "3")//
			.param("description", "Testing1"))//
			.andExpect(status().is3xxRedirection())//
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	/* Creating a new rehab, unsuccessful */
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testProcessCreateRehabFormHasErrors() throws Exception {
		this.mockMvc.perform(post("/owners/*/pets/" + RehabControllerE2ETests.TEST_PET_ID + "/rehab/new")//
			.with(csrf())//
			.param("date", "2020/05/09")//
			.param("time", "")//
			.param("description", "Testing"))//
			.andExpect(model().attributeHasFieldErrors("rehab"))//
			.andExpect(status().isOk())//
			.andExpect(view().name("pets/createOrUpdateRehabForm"));//
	}


	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void deleteExistingRehab() throws Exception {
		mockMvc.perform(get("/owners/1/pets/1/rehab/1/delete")
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
		}
	
	
	 
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void deleteRehabInvalidId() throws Exception {
		mockMvc.perform(get("/owners/5/pets/6/rehab/-3/delete")
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
		}
	

	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void UpdatingRehabFormSuccessfully() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/1/pets/1/rehab/1/edit")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("date", "2020/07/07")//
			.param("time", "1")//
			.param("description", "Test description"))//
			.andExpect(MockMvcResultMatchers.status()
			.is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}


	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testProcessEditingRehabWithError() throws Exception {
		mockMvc.perform(post("/owners/1/pets/1/rehab/1/edit")
						.with(csrf())
						.param("date", "null")
						.param("time", "2")
						.param("description", "edit test"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateRehabForm"));
	}
}

				 
				 
				

