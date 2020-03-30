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
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.InterventionService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = InterventionHomelessPetController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class InterventionHomelessPetControllerTests {

	private static final int TEST_PET_ID = 14;
	private static final int TEST_INTERVENTION_ID = 1;
	
	@Autowired
	private InterventionHomelessPetController interventionHomelessPetController;
	
	@MockBean
	private InterventionService 	interventionService;
	
	@MockBean
	private PetService				petService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		
		PetType petType = new PetType();
		petType.setName("dog");
		
		Pet pet = new Pet();
		pet.setName("Test");
		pet.setBirthDate(LocalDate.of(2020, Month.APRIL, 4));
		pet.setType(petType);
		
		Intervention i = new Intervention();
		i.setInterventionDate(LocalDate.of(2020, Month.SEPTEMBER, 9));
		i.setInterventionTime(2);
		i.setInterventionDescription("Test");
		i.setPet(pet);
		
		Optional<Intervention> intervention = Optional.of(i);
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(new Pet());
		given(this.interventionService.findInterventionById(TEST_INTERVENTION_ID)).willReturn(intervention);
	}
	
	//Creating new interventions -------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testInitNewInterventionHomelessPetForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/interventions/new", TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editIntervention"))
				.andExpect(model().attributeExists("intervention"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testInitNewInterventionHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/interventions/new", TEST_PET_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testProcessNewInterventionHomelessPetFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/interventions/new", TEST_PET_ID)
						.with(csrf())
						.param("interventionDate", "2021/04/04")
						.param("interventionTime", "2")
						.param("interventionDescription", "Test description of an intervention of a homeless pet"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testProcessNewInterventionHomelessPetFormHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/interventions/new", TEST_PET_ID)
						.with(csrf())
						.param("interventionDate", "null")
						.param("interventionTime", "2")
						.param("interventionDescription", "Test"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editIntervention"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testProcessNewInterventionHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/interventions/new", TEST_PET_ID)
						.with(csrf())
						.param("interventionDate", "2021/04/04")
						.param("interventionTime", "2")
						.param("interventionDescription", "Test description of an intervention of a homeless pet"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	//Editing interventions --------------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testInitEditInterventionHomelessPetForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/interventions/{interventionId}/edit", TEST_PET_ID, TEST_INTERVENTION_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editIntervention"))
				.andExpect(model().attributeExists("intervention"))
				.andExpect(model().attribute("intervention", hasProperty("interventionDate", is(LocalDate.of(2020, 9, 9)))))
				.andExpect(model().attribute("intervention", hasProperty("interventionTime", is(2))))
				.andExpect(model().attribute("intervention", hasProperty("interventionDescription", is("Test"))));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testInitEditInterventionHomelessPetFormHasErrors() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/interventions/-1/edit", TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editIntervention"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testInitEditInterventionHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/interventions/{interventionId}/edit", TEST_PET_ID, TEST_INTERVENTION_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testProcessEditInterventionHomelessPetFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/interventions/{interventionId}/edit", TEST_PET_ID, TEST_INTERVENTION_ID)
						.with(csrf())
						.param("interventionDate", "2020/12/12")
						.param("interventionTime", "2")
						.param("interventionDescription", "TestUpdate"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testProcessEditInterventionsHomelessPetHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/interventions/{interventionId}/edit", TEST_PET_ID, TEST_INTERVENTION_ID)
						.with(csrf())
						.param("interventionDate", "null")
						.param("interventionTime", "2")
						.param("interventionDescription", "TestUpdate"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editIntervention"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testProcessEditInterventionHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/interventions/{interventionId}/edit", TEST_PET_ID, TEST_INTERVENTION_ID)
						.with(csrf())
						.param("interventionDate", "2020/12/12")
						.param("interventionTime", "2")
						.param("interventionDescription", "TestUpdate"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	//Deleting interventions ------------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void deleteInterventionHomelessPet() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/interventions/{interventionId}/delete", TEST_PET_ID, TEST_INTERVENTION_ID)
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void deleteInterventionHomelessPetHasErrors() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/interventions/-1/delete", TEST_PET_ID)
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void deleteInterventionHomelessPetHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/interventions/{interventionId}/delete", TEST_PET_ID, TEST_INTERVENTION_ID)
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
}
