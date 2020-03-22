package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.assertj.core.util.Lists;

@WebMvcTest(value = TrainerController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class TrainerControllerTests {

	private static final int TEST_TRAINER_ID = 1;
	private static final int TEST_TRAINER_ID_2 = 4;
	
	@Autowired
	private TrainerController trainerController;
	
	@MockBean
	private TrainerService	trainerService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		Trainer t = new Trainer();
		t.setFirstName("John");
		t.setLastName("Doe");
		t.setEmail("acme@mail.com");
		t.setPhone("111111111");
		Optional<Trainer> trainer = Optional.of(t);
		Optional<Trainer> trainer2 = Optional.empty();
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID)).willReturn(trainer);
		given(this.trainerService.findTrainerById(TEST_TRAINER_ID_2)).willReturn(trainer2);
	}
	
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
	void testListTrainersAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/trainers")).andExpect(status().isOk())
			.andExpect(view().name("admin/trainers/listTrainers"))
			.andExpect(model().attributeExists("trainers"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowTrainerAsAdministrator() throws Exception {
		mockMvc.perform(get("/admin/trainers/{trainerId}", TEST_TRAINER_ID)).andExpect(status().isOk())
			.andExpect(view().name("admin/trainers/showTrainer"))
			.andExpect(model().attributeExists("trainer"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreateForm() throws Exception {
		mockMvc.perform(get("/admin/trainers/new")).andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/editTrainer"))
				.andExpect(model().attributeExists("trainer"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreateFormSuccess() throws Exception {
		mockMvc.perform(post("/admin/trainers/new")
							.with(csrf())
							.param("firstName", "testFirstName")
							.param("lastName", "testLastName")
							.param("email", "test@test.com")
							.param("phone", "999999999")
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/admin/trainers"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreateFormHasErrors() throws Exception {
		mockMvc.perform(post("/admin/trainers/new")
							.with(csrf())
							.param("firstName", "")
							.param("lastName", "testLastName")
							.param("email", "test@test.com")
							.param("phone", "999999999")
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(model().attributeHasErrors("trainer"))
				.andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/editTrainer"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/admin/trainers/{trainerId}/edit", TEST_TRAINER_ID))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trainer"))
				.andExpect(view().name("admin/trainers/editTrainer"));
			
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/admin/trainers/{trainerId}/edit", TEST_TRAINER_ID)
							.with(csrf())
							.param("firstName", "John")
							.param("lastName", "Doe")
							.param("email", "acme@mail.com")
							.param("phone", "999999999")
							.param("user.username", "testing")
							.param("user.password", "testing"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/admin/trainers"));
	}
	
	@WithMockUser(value = "spring")
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
	
	@WithMockUser(value = "spring")
	@Test
	void testDelete() throws Exception {
		mockMvc.perform(get("/admin/trainers/{trainerId}/delete", TEST_TRAINER_ID)).andExpect(status().isOk())
				.andExpect(view().name("admin/trainers/listTrainers"));
	}
	
}