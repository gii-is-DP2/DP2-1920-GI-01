package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;  
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(value = TrainerController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
 class TrainerControllerTests {

	private static final int TEST_TRAINER_ID = 1;
	private static final int TEST_TRAINER_ID_2 = 4;
	private static final int TEST_TRAINER_ID_3 = 100;
	private static final int TEST_TRAINER_ID_4 = 101;
	
	@Autowired
	private TrainerController trainerController;
	
	@MockBean
	private TrainerService	trainerService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		Trainer t1 = new Trainer();
		Trainer t2 = new Trainer();
		Trainer t3 = new Trainer();
		Set<Rehab> rehabsT2 = new HashSet<Rehab>();
		Set<Rehab> rehabsT3 = new HashSet<Rehab>();
		Rehab r1 = new Rehab();
		Rehab r2 = new Rehab();
		
		r1.setDescription("TestRehab");
		r1.setDate(LocalDate.of(2021, Month.APRIL, 4));
		r1.setTime(2);
		r2.setDescription("TestRehab");
		r2.setDate(LocalDate.of(2019, Month.APRIL, 4));
		r2.setTime(2);
		rehabsT2.add(r2);
		rehabsT3.add(r1);
		t2.setRehabs(rehabsT2);
		t3.setRehabs(rehabsT3);
		
		t1.setFirstName("John");
		t1.setLastName("Doe");
		t1.setEmail("acme@mail.com");
		t1.setPhone("111111111");
		Optional<Trainer> trainer = Optional.of(t1);
		Optional<Trainer> trainer2 = Optional.empty();
		Optional<Trainer> trainer3 = Optional.of(t2);
		Optional<Trainer> trainer4 = Optional.of(t3);
		
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID)).willReturn(trainer);
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID_2)).willReturn(trainer2);
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID_3)).willReturn(trainer3);
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID_4)).willReturn(trainer4);
	}
	
	// US-020 Unregistered user can see trainers -------------------------------------------------------------------
	
	@WithMockUser(value = "spring")
	@Test
	void testListTrainersAsUnregisteredUser() throws Exception {
		mockMvc.perform(get("/trainers")).andExpect(status().isOk())
				.andExpect(view().name("trainers/listTrainers"))
				.andExpect(model().attributeExists("trainers"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowTrainerAsUnregisteredUser() throws Exception {
		mockMvc.perform(get("/trainers/{trainerId}", TEST_TRAINER_ID)).andExpect(status().isOk())
				.andExpect(view().name("trainers/showTrainer"))
				.andExpect(model().attributeExists("trainer"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowTrainerAsUnregisteredUserHasErrors() throws Exception {
		mockMvc.perform(get("/trainers/-1")).andExpect(status().isOk())
				.andExpect(view().name("trainers/showTrainer"))
				.andExpect(model().attributeExists("message"));
	}
	
	// US-019 Administrator manages trainers -----------------------------------------------------------------------
	
	// Listing trainers --------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testListTrainersAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/trainers"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/listTrainers"))
				.andExpect(model().attributeExists("trainers"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testListTrainersAsAdministratorHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/admin/trainers"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	// Showing trainers ------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testShowTrainerAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/trainers/{trainerId}", TEST_TRAINER_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/showTrainer"))
				.andExpect(model().attributeExists("trainer"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testShowTrainerAsAdministratorHasErrorsWithId() throws Exception {
		mockMvc.perform(get("/admin/trainers/-1"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/showTrainer"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testShowTrainerAsAdministratorHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/admin/trainers/{trainerId}", TEST_TRAINER_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	// Creating trainers ------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testInitCreateForm() throws Exception {
		mockMvc.perform(get("/admin/trainers/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/editTrainer"))
				.andExpect(model().attributeExists("trainer"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testInitCreateFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/admin/trainers/new"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testProcessCreateFormSuccess() throws Exception {
		mockMvc.perform(post("/admin/trainers/new")
							.with(csrf())
							.param("firstName", "testFirstName")
							.param("lastName", "testLastName")
							.param("email", "test@test.com")
							.param("phone", "34 999999999")
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/admin/trainers"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testProcessCreateFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/admin/trainers/new")
							.with(csrf())
							.param("firstName", "testFirstName")
							.param("lastName", "testLastName")
							.param("email", "test@test.com")
							.param("phone", "34 999999999")
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testProcessCreateFormHasErrors() throws Exception {
		mockMvc.perform(post("/admin/trainers/new")
							.with(csrf())
							.param("firstName", "")
							.param("lastName", "testLastName")
							.param("email", "test@test.com")
							.param("phone", "34 999999999")
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(model().attributeHasErrors("trainer"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/editTrainer"));
	}
	
	// Updating trainers -----------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/admin/trainers/{trainerId}/edit", TEST_TRAINER_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trainer"))
				.andExpect(view().name("admin/trainers/editTrainer"));
			
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testInitUpdateFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/admin/trainers/{trainerId}/edit", TEST_TRAINER_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
			
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testInitUpdateFormHasErrors() throws Exception {
		mockMvc.perform(get("/admin/trainers/-1/edit"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("message"))
				.andExpect(view().name("admin/trainers/editTrainer"));
			
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/admin/trainers/{trainerId}/edit", TEST_TRAINER_ID)
							.with(csrf())
							.param("firstName", "John")
							.param("lastName", "Doe")
							.param("email", "acme@mail.com")
							.param("phone", "34 999999999")
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/admin/trainers"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testProcessUpdateFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/admin/trainers/{trainerId}/edit", TEST_TRAINER_ID)
							.with(csrf())
							.param("firstName", "John")
							.param("lastName", "Doe")
							.param("email", "acme@mail.com")
							.param("phone", "34 999999999")
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/admin/trainers/{trainerId}/edit", TEST_TRAINER_ID)
							.with(csrf())
							.param("firstName", "John")
							.param("lastName", "Doe")
							.param("email", "acme@mail.com")
							.param("phone", "")
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(model().attributeHasErrors("trainer"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/editTrainer"));
	}
	
	// Deleting trainers -----------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testDelete() throws Exception {
		mockMvc.perform(get("/admin/trainers/{trainerId}/delete", TEST_TRAINER_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/listTrainers"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testDeleteHasErrorsBecauseTrainerHasFutureRehabs() throws Exception {
		mockMvc.perform(get("/admin/trainers/{trainerId}/delete", TEST_TRAINER_ID_4))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/listTrainers"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testDeleteTrainerHasNoFutureRehabs() throws Exception {
		mockMvc.perform(get("/admin/trainers/{trainerId}/delete", TEST_TRAINER_ID_3))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/listTrainers"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testDeleteHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/admin/trainers/{trainerId}/delete", TEST_TRAINER_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"admin"})
	@Test
	void testDeleteHasErrors() throws Exception {
		mockMvc.perform(get("/admin/trainers/-1/delete"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("message"))
				.andExpect(view().name("admin/trainers/listTrainers"));
	}
	
	
/* -------  US-24 Owner sees trainer’s personal information ------- */
	@WithMockUser(username = "spring", authorities = {"owner"})
	@Test
	void testOwnerSeesTrainersPersonalInformation() throws Exception {
		mockMvc.perform(get("/trainers/{trainerId}", TEST_TRAINER_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("trainers/showTrainer"))
				.andExpect(model().attributeExists("trainer"));
	}
	
	
	@WithMockUser(username = "spring", authorities = {"owner"})
	@Test
	void testOwnerSeesTrainersPersonalInformationWithError() throws Exception {
		mockMvc.perform(get("/trainers/-2", TEST_TRAINER_ID))
				.andExpect(status().isOk())		
				.andExpect(view().name("trainers/showTrainer"))
				.andExpect(model().attributeExists("message"));
	}
	
	

	
	
}