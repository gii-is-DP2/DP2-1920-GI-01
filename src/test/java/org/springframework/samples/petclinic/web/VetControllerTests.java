
package org.springframework.samples.petclinic.web;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.hamcrest.xml.HasXPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers = VetController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VetControllerTests {

	private static final int	TEST_VET_ID	= 1;

	@Autowired
	private VetController		vetController;

	@MockBean
	private VetService			clinicService;

	@Autowired
	private MockMvc				mockMvc;


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
		BDDMockito.given(this.clinicService.findVets()).willReturn(Lists.newArrayList(james, helen));
		BDDMockito.given(this.clinicService.findVetById(VetControllerTests.TEST_VET_ID)).willReturn(Optional.of(james));

	}

	@WithMockUser(value = "spring")
	@Test
	void testListVetsAsAdministrator() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/vets")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("admin/vets/vetList")).andExpect(MockMvcResultMatchers.model().attributeExists("vets"));
	}

	//	@WithMockUser(value = "spring")
	//	@Test
	//	void testShowVetAsAdministrator() throws Exception {
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/vets/{vetId}", VetControllerTests.TEST_VET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("admin/vets/vetShow"))//
	//			.andExpect(MockMvcResultMatchers.model().attributeExists("vet"));
	//	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/vets/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("admin/vets/vetEdit")).andExpect(MockMvcResultMatchers.model().attributeExists("vet"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/vets/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "testFirstName").param("lastName", "testLastName")
			//no specialties
			.param("user.username", "testing").param("user.password", "testing")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("admin/vets/vetList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/vets/new").with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "").param("lastName", "testLastName")
			//no specialties
			.param("user.username", "testing").param("user.password", "testing")).andExpect(MockMvcResultMatchers.model().attributeHasErrors("vet")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("admin/vets/vetEdit"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/vets/{vetId}/edit", VetControllerTests.TEST_VET_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			//				.andExpect(model().attributeExists("vet"))
			.andExpect(MockMvcResultMatchers.view().name("admin/vets/vetEdit"));

	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/vets/{vetId}/edit", VetControllerTests.TEST_VET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "James").param("lastName", "Carter")
			//no specialties
			.param("user.username", "testing").param("user.password", "testing")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/admin/vets"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/admin/vets/{vetId}/edit", VetControllerTests.TEST_VET_ID).with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "").param("lastName", "Carter")
			//no specialties
			.param("user.username", "testing").param("user.password", "testing")).andExpect(MockMvcResultMatchers.model().attributeHasErrors("vet")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("admin/vets/vetEdit"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDelete() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/vets/{vetId}/delete", VetControllerTests.TEST_VET_ID)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("admin/vets/vetList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVetListHtml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("vets")).andExpect(MockMvcResultMatchers.view().name("vets/vetList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVetListXml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets.xml").accept(MediaType.APPLICATION_XML)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_XML_VALUE))
			.andExpect(MockMvcResultMatchers.content().node(HasXPath.hasXPath("/vets/vetList[id=1]/id")));
	}

}
