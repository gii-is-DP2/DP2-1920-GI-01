package org.springframework.samples.petclinic.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.MedicalRecordService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.web.VisitHomelessPetController;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*@TestPropertySource(
		locations = "classpath:application-mysql.properties")*/
 class VisitHomelessPetControllerE2ETests {

	private static final int TEST_PET_ID = 14;
	private static final int TEST_VISIT_ID = 5;
	private static final int TEST_VISIT_ID_2 = 6;
	
	@Autowired
	private VisitHomelessPetController	visitHomelessPetController;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		
		Visit v = new Visit();
		v.setDate(LocalDate.of(2018, 7, 9));
		v.setDescription("Description 1");
		Optional<Visit> visit = Optional.of(v);
//		given(this.petService.findPetById(TEST_PET_ID)).willReturn(new Pet());
//		given(this.visitService.findVisitById(TEST_VISIT_ID)).willReturn(visit);
	}
	
	//Creating new visits -------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitNewVisitHomelessPetForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/new", TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editVisit"))
				.andExpect(model().attributeExists("visit"));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testInitNewVisitHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/new", TEST_PET_ID))
				.andExpect(status().is4xxClientError());
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProcessNewVisitHomelessPetFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/new", TEST_PET_ID)
						.with(csrf())
						.param("date", "2021/04/04")
						.param("description", "Test description of a visit of a homeless pet"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProcessNewVisitHomelessPetFormHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/new", TEST_PET_ID)
						.with(csrf())
						.param("date", "2019/01/01")
						.param("description", "The date is in the past, this test must fail"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editVisit"));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testProcessNewVisitHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/new", TEST_PET_ID)
						.with(csrf())
						.param("date", "2021/04/04")
						.param("description", "Test description of a visit of a homeless pet"))
				.andExpect(status().is4xxClientError());
	}
	
	//Editing visits --------------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitEditVisitHomelessPetForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/edit", TEST_PET_ID, TEST_VISIT_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editVisit"))
				.andExpect(model().attributeExists("visit"))
				.andExpect(model().attribute("visit", hasProperty("date", is(LocalDate.of(2018, 7, 9)))))
				.andExpect(model().attribute("visit", hasProperty("description", is("Description 1"))));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitEditVisitHomelessPetFormHasErrors() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/-1/edit", TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editVisit"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testInitEditVisitHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/edit", TEST_PET_ID, TEST_VISIT_ID))
				.andExpect(status().is4xxClientError());
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProcessEditVisitHomelessPetFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/edit", TEST_PET_ID, TEST_VISIT_ID)
						.with(csrf())
						.param("date", "2020/12/12")
						.param("description", "The attributes have been changed"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProcessEditVisitHomelessPetHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/edit", TEST_PET_ID, TEST_VISIT_ID)
						.with(csrf())
						.param("date", "2010/12/12")
						.param("description", "The date is in the past, this test must fail"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editVisit"));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testProcessEditVisitHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/edit", TEST_PET_ID, TEST_VISIT_ID)
						.with(csrf())
						.param("date", "2020/12/12")
						.param("description", "The attributes have been changed"))
				.andExpect(status().is4xxClientError());
	}
	
	//Deleting visits ------------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void deleteVisitHomelessPet() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/delete", TEST_PET_ID, TEST_VISIT_ID_2)
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void deleteVisitHomelessPetHasErrors() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/-1/delete", TEST_PET_ID)
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void deleteVisitHomelessPetHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/delete", TEST_PET_ID, TEST_VISIT_ID)
						.with(csrf()))
				.andExpect(status().is4xxClientError());
	}
	
}
