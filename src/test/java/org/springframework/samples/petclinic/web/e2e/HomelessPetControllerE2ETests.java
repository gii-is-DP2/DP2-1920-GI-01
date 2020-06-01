package org.springframework.samples.petclinic.web.e2e;

  
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.web.HomelessPetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*@TestPropertySource(
		locations = "classpath:application-mysql.properties")*/
 class HomelessPetControllerE2ETests {

	private static final int TEST_PET_ID = 14;
	private static final int TEST_PET_ID_2 = -1;
	private static final int TEST_PET_ID_3 = 16;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private HomelessPetController homelessPetController;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		PetType dog = new PetType();
		dog.setId(1);
		dog.setName("dog");
		Pet petForDuplicateNameTest = new Pet();
		petForDuplicateNameTest.setName("Tucker");
		petForDuplicateNameTest.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		petForDuplicateNameTest.setType(dog);
//		given(this.petService.findHomelessPets()).willReturn(Lists.newArrayList(petForDuplicateNameTest));
//		given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(dog));
//		given(this.petService.findPetById(TEST_PET_ID)).willReturn(new Pet());
//		given(this.petService.findPetById(TEST_PET_ID_2)).willReturn(null);
	}
	
	//Listing homeless pets ---------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testListHomelessPetsAsTrainer() throws Exception {
		mockMvc.perform(get("/homeless-pets"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/listPets"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testListHomelessPetsAsVeterinarian() throws Exception {
		mockMvc.perform(get("/homeless-pets"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/listPets"));
	}
	
	@WithMockUser(username = "owner1", authorities = {"owner"})
	@Test
	void testListHomelessPetsHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets"))
				.andExpect(status().is4xxClientError());
	}
	
	//Showing homeless pets --------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testShowHomelessPetAsTrainer() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}", TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/showPet"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testShowHomelessPetAsVeterinarian() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}", TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/showPet"));
	}
	
	@WithMockUser(username = "owner1", authorities = {"owner"})
	@Test
	void testShowHomelessPetHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}", TEST_PET_ID))
				.andExpect(status().is4xxClientError());
	}
	
	//Creating homeless pets --------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editPet"))
				.andExpect(model().attributeExists("pet"));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testInitCreationFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/new"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/new")
							.with(csrf())
							.param("name", "Dawg")
							.param("type", "dog")
							.param("birthDate", "2019/01/01"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/new")
							.with(csrf())
							.param("name", "")
							.param("type", "dog")
							.param("birthDate", "2015/02/12"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editPet"));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testProcessCreationFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/homeless-pets/new")
							.with(csrf())
							.param("name", "Dawg")
							.param("type", "dog")
							.param("birthDate", "2019/01/01"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	//Updating homeless pets -------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/edit", TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("pet"))
				.andExpect(view().name("homelessPets/editPet"));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testInitUpdateFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/edit", TEST_PET_ID))
				.andExpect(status().is4xxClientError());
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/edit", TEST_PET_ID)
							.with(csrf())
							.param("name", "DawgTest")
							.param("type", "dog")
							.param("birthDate", "2019/01/01"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/edit", TEST_PET_ID)
							.with(csrf())
							.param("name", "")
							.param("type", "dog")
							.param("birthDate", "2019/01/01"))
				.andExpect(model().attributeHasErrors("pet"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editPet"));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testProcessUpdateFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/edit", TEST_PET_ID)
							.with(csrf())
							.param("name", "DawgTest")
							.param("type", "dog")
							.param("birthDate", "2019/01/01"))
				.andExpect(status().is4xxClientError());
	}
	
	//Deleting homeless pet ---------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testDelete() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/delete", TEST_PET_ID_3))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/listPets"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testDeleteWithIncorrectId() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/delete", TEST_PET_ID_2))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/listPets"));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testDeleteHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/delete", TEST_PET_ID))
				.andExpect(status().is4xxClientError());
	}
	
}
