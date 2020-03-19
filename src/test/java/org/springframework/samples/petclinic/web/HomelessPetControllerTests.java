package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = HomelessPetController.class,
includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class HomelessPetControllerTests {

	private static final int TEST_PET_ID = 14;
	
	@Autowired
	private HomelessPetController homelessPetController;
	
	@MockBean
	private PetService petService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/new")).andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editPet")).andExpect(model().attributeExists("pet"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/new")
							.with(csrf())
							.param("name", "Dawg")
							.param("type", "hamster")
							.param("birthDate", "2019/01/01"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/listPets"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/new")
							.with(csrf())
							.param("name", "")
							.param("birthDate", "2015/02/12"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editPet"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/edit", TEST_PET_ID))
				.andExpect(status().isOk())
//				.andExpect(model().attributeExists("pet"))
				.andExpect(view().name("homelessPets/editPet"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/edit", TEST_PET_ID)
							.with(csrf())
							.param("name", "DawgTest")
							.param("type", "dog")
							.param("birthDate", "2019/01/01"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/listPets"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/edit", TEST_PET_ID)
							.with(csrf())
							.param("name", "")
							.param("birthDate", "2019/01/01"))
				.andExpect(model().attributeHasErrors("pet")).andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editPet"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testDelete() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/delete", TEST_PET_ID)).andExpect(status().isOk())
		.andExpect(view().name("homelessPets/listPets"));
	}
	
}
