package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(value = TrainerController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class TrainerControllerTests {

	private static final int TEST_TRAINER_ID = 1;
	
	@Autowired
	private TrainerController trainerController;
	
	@MockBean
	private TrainerService	trainerService;
	
	@Autowired
	private MockMvc mockMvc;
	
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
			.andExpect(view().name("trainers/showTrainer"));
//			.andExpect(model().attributeExists("trainer"));
	}
	
}