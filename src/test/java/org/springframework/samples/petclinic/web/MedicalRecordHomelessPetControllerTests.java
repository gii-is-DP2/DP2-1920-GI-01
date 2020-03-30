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
import java.time.Month;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.MedicalRecordService;
import org.springframework.samples.petclinic.service.PrescriptionService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MedicalRecordHomelessPetController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class MedicalRecordHomelessPetControllerTests {

	private static final int TEST_PET_ID = 14;
	private static final int TEST_VISIT_ID = 5;
	private static final int TEST_VISIT_ID_2 = 6;
	private static final int TEST_MEDICAL_RECORD_ID = 2;
	
	@Autowired
	private MedicalRecordHomelessPetController medicalRecordHomelessPetController;
	
	@MockBean
	private VisitService	visitService;

	@MockBean
	private MedicalRecordService medicalRecordService;
	
	@MockBean
	private PrescriptionService prescriptionService;
	
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
		
		Visit v = new Visit();
		v.setDate(LocalDate.of(2018, 7, 9));
		v.setDescription("Description 1");
		v.setPet(pet);
		
		Visit v2 = new Visit();
		v2.setDate(LocalDate.of(2018, 8, 9));
		v2.setDescription("Description 2");
		v2.setPet(pet);
		
		MedicalRecord m = new MedicalRecord();
		m.setDescription("TestDescription");
		m.setStatus("Good");
		m.setVisit(v);
		
		given(this.medicalRecordService.findMedicalRecordByVisitId(TEST_VISIT_ID)).willReturn(m);
		given(this.medicalRecordService.findMedicalRecordById(TEST_MEDICAL_RECORD_ID)).willReturn(m);
		given(this.visitService.findVisitById(TEST_VISIT_ID)).willReturn(Optional.of(v));
		given(this.visitService.findVisitById(TEST_VISIT_ID_2)).willReturn(Optional.of(v2));
	}
	
	//Showing medical records --------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testShowMedicalRecordAsVeterinarian() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record", TEST_PET_ID, TEST_VISIT_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/showMedicalRecord"))
				.andExpect(model().attributeExists("medicalRecord"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testShowMedicalRecordAsTrainer() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record", TEST_PET_ID, TEST_VISIT_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/showMedicalRecord"))
				.andExpect(model().attributeExists("medicalRecord"));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testShowMedicalRecordWithNoMedicalRecord() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record", TEST_PET_ID, TEST_VISIT_ID_2))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/showMedicalRecord"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(username = "spring", authorities = {"owner"})
	@Test
	void testShowMedicalRecordHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record", TEST_PET_ID, TEST_VISIT_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	//Creating new medical records -------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testInitNewMedicalRecordHomelessPetFormAsVeterinarian() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record/new", TEST_PET_ID, TEST_VISIT_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editMedicalRecord"))
				.andExpect(model().attributeExists("medicalRecord"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testInitNewMedicalRecordHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record/new", TEST_PET_ID, TEST_VISIT_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testProcessNewMedicalRecordHomelessPetFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/medical-record/new", TEST_PET_ID, TEST_VISIT_ID)
						.with(csrf())
						.param("description", "Test")
						.param("status", "Test"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testProcessNewMedicalRecordHomelessPetFormHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/medical-record/new", TEST_PET_ID, TEST_VISIT_ID)
						.with(csrf())
						.param("description", "There is no status, this test should fail.")
						.param("status", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editMedicalRecord"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testProcessNewMedicalRecordHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/medical-record/new", TEST_PET_ID, TEST_VISIT_ID)
						.with(csrf())
						.param("description", "Test")
						.param("status", "Test"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	//Editing medical records --------------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testInitEditMedicalRecordHomelessPetForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/edit", TEST_PET_ID, TEST_VISIT_ID, TEST_MEDICAL_RECORD_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editMedicalRecord"))
				.andExpect(model().attributeExists("medicalRecord"))
				.andExpect(model().attribute("medicalRecord", hasProperty("description", is("TestDescription"))))
				.andExpect(model().attribute("medicalRecord", hasProperty("status", is("Good"))));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testInitEditMedicalRecordHomelessPetFormHasErrors() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record/-1/edit", TEST_PET_ID, TEST_VISIT_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editMedicalRecord"))
				.andExpect(model().attributeExists("message"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testInitEditMedicalRecordHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/edit", TEST_PET_ID, TEST_VISIT_ID, TEST_MEDICAL_RECORD_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testProcessEditMedicalRecordHomelessPetFormSuccess() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/edit", TEST_PET_ID, TEST_VISIT_ID_2, TEST_MEDICAL_RECORD_ID)
						.with(csrf())
						.param("description", "test")
						.param("status", "test"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testProcessEditMedicalRecordHomelessPetHasErrors() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/edit", TEST_PET_ID, TEST_VISIT_ID, TEST_MEDICAL_RECORD_ID)
						.with(csrf())
						.param("description", "")
						.param("status", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editMedicalRecord"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testProcessEditMedicalRecordHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/edit", TEST_PET_ID, TEST_VISIT_ID, TEST_MEDICAL_RECORD_ID)
						.with(csrf())
						.param("description", "test")
						.param("status", "test"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	//Deleting medical records ------------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void deleteMedicalRecordHomelessPet() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/delete", TEST_PET_ID, TEST_VISIT_ID, TEST_MEDICAL_RECORD_ID)
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void deleteMedicalRecordHomelessPetHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/delete", TEST_PET_ID, TEST_VISIT_ID, TEST_MEDICAL_RECORD_ID)
						.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
}
