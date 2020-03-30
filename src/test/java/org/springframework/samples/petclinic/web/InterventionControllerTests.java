
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = InterventionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class InterventionControllerTests {

	private static final int		TEST_INTERVENTION_ID	= 1;
	private static final int		TEST_PET_ID				= 1;
	private static final int		TEST_OWNER_ID			= 1;

	@Autowired
	private InterventionController	interventionController;

	@MockBean
	private PetService				petService;

	@Autowired
	private MockMvc					mockMvc;


	@BeforeEach
	void setup() {
		Intervention intervention = new Intervention();
		intervention.setInterventionDate(LocalDate.now());
		intervention.setInterventionDescription("Quick intervention");
		intervention.setInterventionTime(1);

		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2007, 01, 01));
		pet.setName("Alfred");

		BDDMockito.given(this.petService.findPetById(InterventionControllerTests.TEST_PET_ID)).willReturn(pet);

	}

	@WithMockUser(value = "spring")
	@Test
	void testListInterventionAsAdministrator() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/" + InterventionControllerTests.TEST_PET_ID + "/interventions"))//
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("interventionList"))//
			.andExpect(MockMvcResultMatchers.model().attributeExists("interventions"));
	}

	@WithMockUser(value = "spring")
	@Test
	void initCreateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/" + InterventionControllerTests.TEST_PET_ID + "/interventions/new"))//
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateInterventionForm"))//
			.andExpect(MockMvcResultMatchers.model().attributeExists("intervention"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/" + InterventionControllerTests.TEST_PET_ID + "/interventions/new")//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("interventionDate", "2020/07/07")//
			.param("interventionTime", "1")//
			.param("interventionDescription", "Test description"))//
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/" + InterventionControllerTests.TEST_PET_ID + "/interventions/new")//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("interventionDate", "")//
			.param("interventionTime", "1")//
			.param("interventionDescription", ""))//
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("intervention"))//
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateInterventionForm"));//
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDelete() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/1/pets/1/interventions/1/delete")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("owners/ownerlist"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/1/pets/1/interventions/1/edit").with(SecurityMockMvcRequestPostProcessors.csrf()).param("interventionDate", "2020/07/07")//
			.param("interventionTime", "1")//
			.param("interventionDescription", "Test description"))//
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/ownerList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/1/pets/1/interventions/1/edit").with(SecurityMockMvcRequestPostProcessors.csrf()).param("interventionDate", "")//
			.param("interventionTime", "1")//
			.param("interventionDescription", ""))//
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("intervention")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateInterventionForm"));
	}

}
