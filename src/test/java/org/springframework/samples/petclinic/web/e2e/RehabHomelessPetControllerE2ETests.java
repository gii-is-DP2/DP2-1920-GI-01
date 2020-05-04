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

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.RehabService;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.samples.petclinic.web.RehabHomelessPetController;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
		webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*@TestPropertySource(
		locations = "classpath:application-mysql.properties")*/
public class RehabHomelessPetControllerE2ETests {

	private static final int TEST_PET_ID = 14;
	private static final int TEST_REHAB_ID = 1;
	
	@Autowired
	private RehabHomelessPetController rehabHomelessPetController;
	
	@MockBean
	private RehabService	rehabService;
	
	@MockBean
	private PetService		petService;
	
	@MockBean
	private TrainerService 	trainerService;
	
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
		
		User user = new User();
		user.setUsername("testing");
		user.setPassword("testing");
		user.setEnabled(true);
		
		Trainer t = new Trainer();
		t.setFirstName("testFirstName");
		t.setLastName("testLastName");
		t.setEmail("email@mail.com");
		t.setPhone("34 999999999");
		t.setUser(user);
		
		Rehab r = new Rehab();
		r.setDate(LocalDate.of(2020, Month.DECEMBER, 24));
		r.setTime(2);
		r.setDescription("Test");
		r.setPet(pet);
		r.setTrainer(t);
		
		Set<Rehab> rehabs = new HashSet<Rehab>();
		rehabs.add(r);
		t.setRehabs(rehabs);

		given(this.petService.findPetById(TEST_PET_ID)).willReturn(new Pet());
		given(this.rehabService.findRehabById(TEST_REHAB_ID)).willReturn(Optional.of(r));
	}
	
	//Creating new rehabs -------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testInitNewRehabHomelessPetForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/rehabs/new", TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editRehab"))
				.andExpect(model().attributeExists("rehab"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitNewRehabHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/rehabs/new", TEST_PET_ID))
				.andExpect(status().is4xxClientError());
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testProcessNewRehabHomelessPetFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/rehabs/new", TEST_PET_ID)
						.with(csrf())
						.param("date", "2021/04/04")
						.param("time", "2")
						.param("description", "Test description of a rehab of a homeless pet"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testProcessNewRehabHomelessPetFormHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/rehabs/new", TEST_PET_ID)
						.with(csrf())
						.param("date", "null")
						.param("time", "2")
						.param("description", "The date is null, this test must fail"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editRehab"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProcessNewRehabHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/rehabs/new", TEST_PET_ID)
						.with(csrf())
						.param("date", "2021/04/04")
						.param("time", "2")
						.param("description", "Test description of a rehab of a homeless pet"))
				.andExpect(status().is4xxClientError());
	}
	
	//Editing rehabs --------------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testInitEditRehabHomelessPetForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/rehabs/{rehabId}/edit", TEST_PET_ID, TEST_REHAB_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editRehab"))
				.andExpect(model().attributeExists("rehab"))
				.andExpect(model().attribute("rehab", hasProperty("date", is(LocalDate.of(2020, 12, 24)))))
				.andExpect(model().attribute("rehab", hasProperty("time", is(2))))
				.andExpect(model().attribute("rehab", hasProperty("description", is("Test"))));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testInitEditRehabHomelessPetFormHasErrors() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/rehabs/-1/edit", TEST_PET_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editRehab"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitEditRehabHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/rehabs/{rehabId}/edit", TEST_PET_ID, TEST_REHAB_ID))
				.andExpect(status().is4xxClientError());
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testProcessEditRehabHomelessPetFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/rehabs/{rehabId}/edit", TEST_PET_ID, TEST_REHAB_ID)
						.with(csrf())
						.param("date", "2020/12/12")
						.param("time", "2")
						.param("description", "The attributes have been changed"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void testProcessEditRehabHomelessPetHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/rehabs/{rehabId}/edit", TEST_PET_ID, TEST_REHAB_ID)
						.with(csrf())
						.param("date", "null")
						.param("time", "2")
						.param("description", "The date is null, this test must fail"))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editRehab"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProcessEditRehabHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/rehabs/{rehabId}/edit", TEST_PET_ID, TEST_REHAB_ID)
						.with(csrf())
						.param("date", "2020/12/12")
						.param("time", "2")
						.param("description", "The attributes have been changed"))
				.andExpect(status().is4xxClientError());
	}
	
	//Deleting rehabs ------------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void deleteRehabHomelessPet() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/rehabs/{rehabId}/delete", TEST_PET_ID, TEST_REHAB_ID)
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "trainer1", authorities = {"trainer"})
	@Test
	void deleteRehabHomelessPetHasErrors() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/rehabs/-1/delete", TEST_PET_ID)
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void deleteRehabHomelessPetHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/rehabs/{rehabId}/delete", TEST_PET_ID, TEST_REHAB_ID)
						.with(csrf()))
				.andExpect(status().is4xxClientError());
	}
	
}
