package org.springframework.samples.petclinic.web;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = RehabController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class RehabControllerTests {

	private static final int TEST_PET_ID = 1;
	

	@MockBean
	private RehabService	rehabService;
	
	@MockBean
	private PetService		petService;

	@MockBean
	private TrainerService 	trainerService;
	
	@MockBean
	private OwnerService 	ownerService;
	
	
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

		BDDMockito.given(this.petService.findPetById(RehabControllerTests.TEST_PET_ID)).willReturn(pet);
	}
	

	/* Opening new rehab form*/
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testAddingNewRehabForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/" + RehabControllerTests.TEST_PET_ID + "/rehab/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateRehabForm"))
				.andExpect(model().attributeExists("rehab"));
	}
	
	/* Creating a new rehab, successful */
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testProcessCreateRehabFormIsSuccessful() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/" + RehabControllerTests.TEST_PET_ID +"/rehab/new")//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("date", "2020/05/09")//
			.param("time", "3")//
			.param("description", "Testing1"))//
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	/* Creating a new rehab, unsuccessful */
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testProcessCreateRehabFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/" + RehabControllerTests.TEST_PET_ID + "/rehab/new")//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("date", "2020/05/09")//
			.param("time", "")//
			.param("description", "Testing"))//
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rehab"))//
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateRehabForm"));//
	}

	/* Deleting an existing rehab, successful */
		 
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void deleteExistingRehab() throws Exception {
		mockMvc.perform(get("/owners/1/pets/1/rehab/1/delete")
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
		}
	
	
	 
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void deleteRehabInvalidId() throws Exception {
		mockMvc.perform(get("/owners/5/pets/6/rehab/-3/delete")
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
		}
	
/* Editing a rehab, successful*/
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
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

}


				 
				 
				

