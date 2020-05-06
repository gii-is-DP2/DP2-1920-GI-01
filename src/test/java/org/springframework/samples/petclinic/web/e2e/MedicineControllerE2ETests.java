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

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*@TestPropertySource(locations = "classpath:application-mysql.properties")*/
public class MedicineControllerE2ETests {
	
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
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/medicine/create"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("medicine/form"))
			   .andExpect(model().attributeExists("medicine"))
			   .andExpect(model().attributeExists("petTypes"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
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
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
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
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/medicine/update")
							.queryParam("id", "1"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("medicine/form"))
			   .andExpect(model().attributeExists("petTypes"))
			   .andExpect(model().attributeExists("medicine"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitUpdateFormNullReference() throws Exception {
		mockMvc.perform(get("/medicine/update")
							.queryParam("id", "0"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("exception"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
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
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
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
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
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
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testDeleteMedicineSuccess() throws Exception {
		mockMvc.perform(get("/medicine/delete")
							.queryParam("id", "1"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(view().name("redirect:/medicine/list"));
	}

	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testDeleteMedicineHasErrors() throws Exception {
		mockMvc.perform(get("/medicine/delete")
							.queryParam("id", "0"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("exception"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testShowMedicineSuccess() throws Exception {
		mockMvc.perform(get("/medicine/show")
				.queryParam("id", "1"))
		   .andExpect(status().isOk())
		   .andExpect(view().name("medicine/show"))
		   .andExpect(model().attributeExists("medicine"))
		   .andExpect(model().attributeExists("petTypes"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testShowMedicineWithNullReference() throws Exception {
		mockMvc.perform(get("/medicine/show")
								.queryParam("id", "0"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("exception"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testListMedicineOneResult() throws Exception {
		given(this.medicineService.findManyMedicineByName("")).willReturn(Lists.newArrayList(new Medicine()));
		
		mockMvc.perform(get("/medicine/list"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(view().name("redirect:/medicine/show?id=null"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testListMedicineNoResults() throws Exception {
		given(this.medicineService.findManyMedicineByName("")).willReturn(Lists.newArrayList());
		
		mockMvc.perform(get("/medicine/list"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("medicine/list"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testListMedicineMultipleResults() throws Exception {
		given(this.medicineService.findManyMedicineByName("")).willReturn(Lists.newArrayList(new Medicine(), new Medicine()));
		
		mockMvc.perform(get("/medicine/list"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("medicine/list"))
			   .andExpect(model().attributeExists("results"));
	}

}
