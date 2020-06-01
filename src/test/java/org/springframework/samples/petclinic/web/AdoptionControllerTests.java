package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.web.formatters.OwnerFormatter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = AdoptionController.class,
includeFilters = @ComponentScan.Filter(value = OwnerFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class AdoptionControllerTests {

	@MockBean
	private AdoptionService adoptionService;
	
	@MockBean
	private OwnerService ownerService;
	
	@MockBean
	private PetService petService;	
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		Owner owner;
		Pet pet;
		Adoption adoption;
		
		owner = new Owner();
		pet = new Pet();
		adoption = new Adoption();
		
		owner.setId(1);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		
		given(ownerService.findAll()).willReturn(Lists.newArrayList(owner));
		given(petService.findPetById(14)).willReturn(pet);
		given(adoptionService.findByPetId(1)).willReturn(Lists.newArrayList(adoption));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/14/adopt"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("adoption/form"))
			   .andExpect(model().attributeExists("adoption"))
			   .andExpect(model().attributeExists("owners"));	
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProccessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/14/adopt")
						.with(csrf())
						.param("owner", "George Franklin"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(view().name("redirect:/owners/1"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProccessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/14/adopt")
								.with(csrf()))
		       .andExpect(status().isOk())
		       .andExpect(view().name("adoption/form"))
		       .andExpect(model().attributeExists("owners"))
		       .andExpect(model().attributeExists("adoption"))
		       .andExpect(model().attributeHasErrors("adoption"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testListAdoption() throws Exception {
		mockMvc.perform(get("/owners/*/pets/1/adoption-history"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("adoption/list"))
			   .andExpect(model().attributeExists("adoptions"));
	}
	
	
}
