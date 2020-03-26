package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.xml.HasXPath.hasXPath;
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

import java.util.Optional;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers=VetController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class VetControllerTests {
	
	private static final int TEST_VET_ID = 1;

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
		given(this.clinicService.findVets()).willReturn(Lists.newArrayList(james, helen));
		given(this.clinicService.findVetById(TEST_VET_ID)).willReturn(Optional.of(james));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testListVetsAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/vets")).andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetList"))
				.andExpect(model().attributeExists("vets"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testListVetsAsUnregisteredUser() throws Exception {
		mockMvc.perform(get("/vets")).andExpect(status().isOk())
				.andExpect(view().name("vets/vetList"))
				.andExpect(model().attributeExists("vets"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowVetAsAdministratorHasErrors() throws Exception {
		mockMvc.perform(get("/admin/vets/-1")).andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetShow"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowVetAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/vets/{vetId}", TEST_VET_ID)).andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetShow"))
				.andExpect(model().attributeExists("vet"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowVetAsUnregisteredUserHasErrors() throws Exception {
		mockMvc.perform(get("/vets/-1")).andExpect(status().isOk())
				.andExpect(view().name("vets/vetDetails"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowVetAsUnregisteredUser() throws Exception {
		mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID)).andExpect(status().isOk())
				.andExpect(view().name("vets/vetDetails"))
				.andExpect(model().attributeExists("vet"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreateForm() throws Exception {
		mockMvc.perform(get("/admin/vets/new")).andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetEdit"))
				.andExpect(model().attributeExists("vet"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreateForm() throws Exception {
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
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreateFormHasErrors() throws Exception {
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
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/admin/vets/{vetId}/edit", TEST_VET_ID))
				.andExpect(status().isOk())
//				.andExpect(model().attributeExists("vet"))
				.andExpect(view().name("admin/vets/vetEdit"));
			
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFormHasErrors() throws Exception {
		mockMvc.perform(get("/admin/vets/-1/edit"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("message"))
				.andExpect(view().name("admin/vets/vetEdit"));
			
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
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
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
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
	
	@WithMockUser(value = "spring")
	@Test
	void testDelete() throws Exception {
		mockMvc.perform(get("/admin/vets/{vetId}/delete", TEST_VET_ID)).andExpect(status().isOk())
				.andExpect(view().name("admin/vets/vetList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testDeleteHasErrors() throws Exception {
		mockMvc.perform(get("/admin/vets/-1/delete")).andExpect(status().isOk())
				.andExpect(model().attributeExists("message"))
				.andExpect(view().name("admin/vets/vetList"));
	}
        
    @WithMockUser(value = "spring")
	@Test
	void testShowVetListHtml() throws Exception {
		mockMvc.perform(get("/vets")).andExpect(status().isOk()).andExpect(model().attributeExists("vets"))
				.andExpect(view().name("vets/vetList"));
	}	

	@WithMockUser(value = "spring")
    @Test
	void testShowVetListXml() throws Exception {
		mockMvc.perform(get("/vets.xml").accept(MediaType.APPLICATION_XML)).andExpect(status().isOk())
				.andExpect(content().contentType("application/xml;charset=UTF-8"));
	}

}
