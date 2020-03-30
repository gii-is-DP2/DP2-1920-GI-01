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

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.RehabService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = RehabController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
class RehabControllerTests {

	private static final int TEST_PET_ID = 1;

	@MockBean
	private RehabService	rehabService;
	
	@MockBean
	private PetService		petService;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@BeforeEach
	void setup() {
		Rehab rehab = new Rehab();
		rehab.setDate(LocalDate.now());
		rehab.setTime(1);
		rehab.setDescription("First rehab");
		
		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2019, 02, 14));
		pet.setName("Maya");

		BDDMockito.given(this.petService.findPetById(RehabControllerTests.TEST_PET_ID)).willReturn(pet);

	}
	
	/* testing opening the rehab form*/
	 @WithMockUser(value = "spring")
	    @Test
		void OpeningANewRehabForm() throws Exception {
			mockMvc.perform(get("/owners/*/pets/{petId}/rehab/new", TEST_PET_ID)).andExpect(status().isOk())
					.andExpect(view().name("pets/createOrUpdateRehabForm"));
		}
	 
			 /* Testing when some of the input parameters are not filled */
				@WithMockUser(value = "spring")
				@Test
				void UnsuccessfulRehabFormAddingWithError() throws Exception {
					this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/" + RehabControllerTests.TEST_PET_ID + "/rehab/new")//
						.with(SecurityMockMvcRequestPostProcessors.csrf())//
						.param("date", "")//
						.param("time", "")//
						.param("description", "description"))//
						.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("rehab"))//
						.andExpect(MockMvcResultMatchers.status().isOk())//
						.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateRehabForm"));//
				}
				
				/* successfully adding a new rehab*/
				@WithMockUser(value = "spring")
			    @Test
				void AddingANewRehab() throws Exception {
					mockMvc.perform(post("/owners/*/pets/{petId}/rehab/new", TEST_PET_ID)
							.with(csrf())
							.param("date", "2021/04/04")
							.param("time", "2")
							.param("description", "Description"))                                
			                .andExpect(status().is3xxRedirection())
						.andExpect(view().name("redirect:/owners/{ownerId}"));
			 }
		
				/* seeing pets rehabs */
				@WithMockUser(value = "spring")
			    @Test
				void AddedRehabListCheck() throws Exception {
					mockMvc.perform(get("/owners/*/pets/{petId}/rehab", TEST_PET_ID)).andExpect(status().isOk())
							.andExpect(model().attributeExists("rehab")).andExpect(view().name("rehabList"));
				}

}

				/* when trying to add new rehabilitation, but someone other than trainer
				 *   !!! should be revised after implementing next us 
				 * 
				
				/* @WithMockUser(value = "spring", authorities = {"owner", "veterinarian", "administrator", "vet" })
				    @Test
					void AddingNewRehabWithoutAccess() throws Exception {
						mockMvc.perform(get("/owners//pets/{petId}/rehab/new", TEST_PET_ID)).andExpect(status().isOk())
						.andExpect(status().is3xxRedirection())
						.andExpect(view().name("redirect:/oups"));
					}
					
					
				}
			 */
				 
				 
				

