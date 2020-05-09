package org.springframework.samples.petclinic.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.RehabService;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.samples.petclinic.web.RehabController;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
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
	

	@MockBean
	private RehabService	rehabService;
	
	@MockBean
	private PetService		petService;

	@MockBean
	private TrainerService 	trainerService;
	
		
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

		given(this.petService.findPetById(RehabControllerE2ETests.TEST_PET_ID)).willReturn(pet);
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


		
		
}


				 
				 
				

