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

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.web.formatters.PetTypeFormatter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = MedicineController.class,
	includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration= SecurityConfiguration.class)
class MedicineControllerTests {
	
	@MockBean
	private MedicineService medicineService;
	
	@MockBean
	private PetService petService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		PetType petType = new PetType();
		petType.setId(2);
		petType.setName("dog");
		given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(petType));
		given(this.medicineService.findMedicineById(1)).willReturn(new Medicine());
		given(this.medicineService.findMedicineById(0)).willReturn(null);
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/medicine/create"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("medicine/form"))
			   .andExpect(model().attributeExists("medicine"))
			   .andExpect(model().attributeExists("petTypes"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProccessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/medicine/create")
							.with(csrf())
							.param("name", "Test")
							.param("expirationDate", "2023/01/01")
							.param("maker", "Test")
							.param("petType", "dog"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(view().name("redirect:/medicine/show?id=null"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProccessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/medicine/create")
							.with(csrf())
							.param("name", "")
							.param("expirationDate", "2019/01/01")
							.param("maker", "Test"))
			   .andExpect(status().isOk())
			   .andExpect(model().attributeHasErrors("medicine"))
			   .andExpect(view().name("medicine/form"))
			   .andExpect(model().attributeExists("petTypes"))
			   .andExpect(model().attributeExists("medicine"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/medicine/update")
							.queryParam("id", "1"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("medicine/form"))
			   .andExpect(model().attributeExists("petTypes"))
			   .andExpect(model().attributeExists("medicine"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateFormNullReference() throws Exception {
		mockMvc.perform(get("/medicine/update")
							.queryParam("id", "0"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProccessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/medicine/update")
							.queryParam("id", "1")
							.with(csrf())
							.param("name", "Test")
							.param("expirationDate", "")
							.param("maker", "")
							.param("petType", "dog"))
			   .andExpect(model().attributeHasErrors("medicine"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("medicine/form"))
			   .andExpect(model().attributeExists("medicine"))
			   .andExpect(model().attribute("medicine", hasProperty("maker", is(""))))
			   .andExpect(model().attributeExists("petTypes"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProccessUpdateFormNullReference() throws Exception {
		mockMvc.perform(post("/medicine/update")
							.queryParam("id", "0")
							.with(csrf())
							.param("name", "Test")
							.param("expirationDate", "2100/01/01")
							.param("maker", "Test")
							.param("petType", "dog"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProccessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/medicine/update")
							.queryParam("id", "1")
							.with(csrf())
							.param("name", "Test")
							.param("expirationDate", "2100/01/01")
							.param("maker", "Test")
							.param("petType", "dog"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(view().name("redirect:/medicine/show?id=1"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testDeleteMedicineSuccess() throws Exception {
		mockMvc.perform(get("/medicine/delete")
							.queryParam("id", "1"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(view().name("redirect:/medicine/list"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDeleteMedicineHasErrors() throws Exception {
		mockMvc.perform(get("/medicine/delete")
							.queryParam("id", "0"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowMedicineSuccess() throws Exception {
		mockMvc.perform(get("/medicine/show")
				.queryParam("id", "1"))
		   .andExpect(status().isOk())
		   .andExpect(view().name("medicine/show"))
		   .andExpect(model().attributeExists("medicine"))
		   .andExpect(model().attributeExists("petTypes"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowMedicineWithNullReference() throws Exception {
		mockMvc.perform(get("/medicine/show")
								.queryParam("id", "0"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testListMedicineOneResult() throws Exception {
		given(this.medicineService.findManyMedicineByName("")).willReturn(Lists.newArrayList(new Medicine()));
		
		mockMvc.perform(get("/medicine/list"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(view().name("redirect:/medicine/show?id=null"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testListMedicineNoResults() throws Exception {
		given(this.medicineService.findManyMedicineByName("")).willReturn(Lists.newArrayList());
		
		mockMvc.perform(get("/medicine/list"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("medicine/list"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testListMedicineMultipleResults() throws Exception {
		given(this.medicineService.findManyMedicineByName("")).willReturn(Lists.newArrayList(new Medicine(), new Medicine()));
		
		mockMvc.perform(get("/medicine/list"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("medicine/list"))
			   .andExpect(model().attributeExists("results"));
	}
	
}
