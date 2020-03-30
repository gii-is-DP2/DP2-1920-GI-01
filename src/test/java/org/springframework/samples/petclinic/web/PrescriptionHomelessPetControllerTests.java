package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.MedicalRecordService;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.PrescriptionService;
import org.springframework.samples.petclinic.web.formatters.MedicineFormatter;
import org.springframework.samples.petclinic.web.formatters.PetTypeFormatter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PrescriptionHomelessPetController.class,
		includeFilters = @ComponentScan.Filter(value = MedicineFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class PrescriptionHomelessPetControllerTests {

	private static final int TEST_PET_ID = 14;
	private static final int TEST_VISIT_ID = 5;
	private static final int TEST_MEDICAL_RECORD_ID = 2;
	
	@Autowired
	private PrescriptionHomelessPetController prescriptionHomelessPetController;
	
	@MockBean
	private MedicineService medicineService;
	
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
		
		MedicalRecord m = new MedicalRecord();
		m.setDescription("TestDescription");
		m.setStatus("Good");
		m.setVisit(v);
		
		Medicine medicine = new Medicine();
		medicine.setId(1);
		medicine.setName("testName");
		medicine.setMaker("testMaker");
		medicine.setExpirationDate(LocalDate.of(2020, Month.DECEMBER, 24));
		medicine.setPetType(petType);
		
		Collection<Medicine> medicines = new ArrayList<Medicine>();
		medicines.add(medicine);

		given(this.medicineService.findManyAll()).willReturn(Lists.newArrayList(medicine));
		given(this.medicineService.findByPetType(null)).willReturn(Lists.newArrayList(medicine));
		given(this.medicalRecordService.findMedicalRecordById(TEST_MEDICAL_RECORD_ID)).willReturn(m);
	}
	
	//Creating new prescription -------------------------------------------------------------------------------------------------------
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testInitNewPrescriptionHomelessPetForm() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordI}/prescriptions/new", TEST_PET_ID, TEST_VISIT_ID, TEST_MEDICAL_RECORD_ID))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editPrescription"))
				.andExpect(model().attributeExists("prescription"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testInitNewPrescriptionHomelessPetFormHasErrorsWithAuthority() throws Exception {
		mockMvc.perform(get("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/prescriptions/new", TEST_PET_ID, TEST_VISIT_ID, TEST_MEDICAL_RECORD_ID))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testProcessNewPrescriptionHomelessPetFormSuccess() throws Exception {
		
		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/prescriptions/new", TEST_PET_ID, TEST_VISIT_ID, TEST_MEDICAL_RECORD_ID)
						.with(csrf())
						.param("medicine", "testName")
						.param("dose", "Every 4 hours"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/homeless-pets/" + TEST_PET_ID));
	}
	
	@WithMockUser(username = "spring", authorities = {"veterinarian"})
	@Test
	void testProcessNewPrescriptionHomelessPetFormHasErrors() throws Exception {

		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/prescriptions/new", TEST_PET_ID, TEST_VISIT_ID, TEST_MEDICAL_RECORD_ID)
						.with(csrf())
						.param("medicine", "testName")
						.param("dose", ""))
				.andExpect(status().isOk())
				.andExpect(view().name("homelessPets/editPrescription"));
	}
	
	@WithMockUser(username = "spring", authorities = {"trainer"})
	@Test
	void testProcessNewPrescriptionHomelessPetFormHasErrorsWithAuthority() throws Exception {

		mockMvc.perform(post("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/prescriptions/new", TEST_PET_ID, TEST_VISIT_ID, TEST_MEDICAL_RECORD_ID)
						.with(csrf())
						.param("medicine", "testName")
						.param("dose", "Every 4 hours"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/oups"));
	}
	
}
