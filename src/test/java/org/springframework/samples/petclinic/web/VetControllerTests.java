package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers=VetController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class VetControllerTests {
	
	private static final int TEST_VET_ID = 1;
	private static final int TEST_VET_ID_2 = 100;
	private static final int TEST_VET_ID_3 = 101;

	@Autowired
	private VetController vetController;

	@MockBean
	private VetService clinicService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {

		Vet james = new Vet();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setId(1);
		Vet helen = new Vet();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setId(2);
		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		helen.addSpecialty(radiology);
		
		Vet v1 = new Vet();
		Vet v2 = new Vet();
		Set<Intervention> i1 = new HashSet<Intervention>();
		Set<Intervention> i2 = new HashSet<Intervention>();
		Intervention intervention1 = new Intervention();
		Intervention intervention2 = new Intervention();
		
		intervention1.setVet(v1);
		intervention1.setInterventionDate(LocalDate.of(2019, Month.APRIL, 4));
		intervention1.setInterventionTime(2);
		intervention1.setInterventionDescription("Test");
		intervention2.setVet(v2);
		intervention2.setInterventionDate(LocalDate.of(2021, Month.APRIL, 4));
		intervention2.setInterventionTime(2);
		intervention2.setInterventionDescription("Test");
		i1.add(intervention1);
		i2.add(intervention2);
		v1.setInterventionsInternal(i1);
		v2.setInterventionsInternal(i2);
		
		given(this.clinicService.findVets()).willReturn(Lists.newArrayList(james, helen));
		given(this.clinicService.findVetById(TEST_VET_ID)).willReturn(Optional.of(james));
		given(this.clinicService.findVetById(TEST_VET_ID_2)).willReturn(Optional.of(v1));
		given(this.clinicService.findVetById(TEST_VET_ID_3)).willReturn(Optional.of(v2));
	}
	
    @WithMockUser(value = "spring")
	@Test
	void testShowVetListHtml() throws Exception {
		mockMvc.perform(get("/vets"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("vets"))
				.andExpect(view().name("vets/vetList"));
	}	

	@WithMockUser(value = "spring")
    @Test
	void testShowVetListXml() throws Exception {
		mockMvc.perform(get("/vets.xml").accept(MediaType.APPLICATION_XML)).andExpect(status().isOk())
				.andExpect(content().contentType("application/xml;charset=UTF-8"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testListVetsAsUnregisteredUser() throws Exception {
		mockMvc.perform(get("/vets"))
				.andExpect(status().isOk())
				.andExpect(view().name("vets/vetList"))
				.andExpect(model().attributeExists("vets"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowVetAsUnregisteredUserHasErrors() throws Exception {
		mockMvc.perform(get("/vets/-1"))
				.andExpect(status().isOk())
				.andExpect(view().name("vets/vetDetails"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowVetAsUnregisteredUser() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("vets/vetDetails"))
				.andExpect(model().attributeExists("vet"));
	}
	
	// US-013 Administrator manages vets ---------------------------------------------------------------------------
	
	// Listing vets ------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testListVetsAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/vets"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetList"))
				.andExpect(model().attributeExists("vets"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testListVetsAsAdministratorHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/admin/vets"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	// Showing vets -----------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testShowVetAsAdministratorHasErrorsWithId() throws Exception {
		mockMvc.perform(get("/admin/vets/-1"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetShow"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testShowVetAsAdministratorHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/admin/vets/-1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testShowVetAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/vets/{vetId}", TEST_VET_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetShow"))
				.andExpect(model().attributeExists("vet"));
	}
	
	// Creating vets ----------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testInitCreateFormAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/vets/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetEdit"))
				.andExpect(model().attributeExists("vet"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testInitCreateFormAsAdministratorHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/admin/vets/new"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testProcessCreateFormSuccessAsAdministrator() throws Exception {
		mockMvc.perform(post("/admin/vets/new")
							.with(csrf())
							.param("firstName", "testFirstName")
							.param("lastName", "testLastName")
							//no specialties
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetList"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testProcessCreateFormAsAdministratorHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/admin/vets/new")
							.with(csrf())
							.param("firstName", "testFirstName")
							.param("lastName", "testLastName")
							//no specialties
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testProcessCreateFormHasErrorsAsAdministrator() throws Exception {
		mockMvc.perform(post("/admin/vets/new")
							.with(csrf())
							.param("firstName", "")
							.param("lastName", "testLastName")
							//no specialties
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(model().attributeHasErrors("vet"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetEdit"));
	}
	
	// Updating vets ----------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testInitUpdateFormAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/vets/{vetId}/edit", TEST_VET_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("vet"))
				.andExpect(view().name("admin/vets/vetEdit"));
			
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testInitUpdateFormAsAdministratorHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/admin/vets/{vetId}/edit", TEST_VET_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
			
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testInitUpdateFormHasErrorsAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/vets/-1/edit"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("message"))
				.andExpect(view().name("admin/vets/vetEdit"));
			
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testProcessUpdateFormSuccessAsAdministrator() throws Exception {
		mockMvc.perform(post("/admin/vets/{vetId}/edit", TEST_VET_ID)
							.with(csrf())
							.param("firstName", "James")
							.param("lastName", "Carter")
							//no specialties
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/admin/vets"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testProcessUpdateFormAsAdministratorHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/admin/vets/{vetId}/edit", TEST_VET_ID)
							.with(csrf())
							.param("firstName", "James")
							.param("lastName", "Carter")
							//no specialties
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testProcessUpdateFormHasErrorsAsAdministrator() throws Exception {
		mockMvc.perform(post("/admin/vets/{vetId}/edit", TEST_VET_ID)
							.with(csrf())
							.param("firstName", "")
							.param("lastName", "Carter")
							//no specialties
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(model().attributeHasErrors("vet"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetEdit"));
	}
	
	// Deleting vets ----------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testDeleteAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/vets/{vetId}/delete", TEST_VET_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetList"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testDeleteAsAdministratorHasErrorsBecauseVetHasFutureInterventions() throws Exception {
		mockMvc.perform(get("/admin/vets/{vetId}/delete", TEST_VET_ID_2))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetList"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testDeleteAsAdministratorVetHasNoFutureInterventions() throws Exception {
		mockMvc.perform(get("/admin/vets/{vetId}/delete", TEST_VET_ID_3))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetList"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testDeleteAsAdministratorHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/admin/vets/{vetId}/delete", TEST_VET_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testDeleteHasErrorsAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/vets/-1/delete"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("message"))
				.andExpect(view().name("admin/vets/vetList"));
	}

}
