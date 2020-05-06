package org.springframework.samples.petclinic.web.e2e;

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
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.MedicalRecordService;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.PrescriptionService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
/*@TestPropertySource(locations = "classpath:application-mysql.properties")*/
public class PrescriptionControllerE2ETests {
	
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
		Owner owner;
		Pet pet;
		Visit visit;
		MedicalRecord medicalRecord;
		Medicine medicine;
		PetType petType;
		
		owner = new Owner();
		pet = new Pet();
		visit = new Visit();
		medicalRecord = new MedicalRecord();
		medicine = new Medicine();
		petType = new PetType();
		
		petType.setName("Test");
		petType.setId(1);
		owner.addPet(pet);
		owner.setId(1);
		pet.setType(petType);
		pet.setId(1);
		visit.setPet(pet);
		visit.setId(1);
		medicalRecord.setVisit(visit);
		medicine.setName("Cat medicine");
		medicine.setPetType(petType);
		
		given(medicineService.findManyAll()).willReturn(Lists.newArrayList(medicine));
		given(medicalRecordService.findMedicalRecordById(1)).willReturn(medicalRecord);
		given(medicineService.findByPetType(null)).willReturn(Lists.newArrayList(medicine));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/*/visits/*/medical-record/{medical-recordId}/prescription/create", 1))
			   .andExpect(status().isOk())
			   .andExpect(view().name("prescription/form"))
			   .andExpect(model().attributeExists("prescription"))
			   .andExpect(model().attributeExists("medicines"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProccessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/*/pets/*/visits/*/medical-record/{medical-recordId}/prescription/create", 1)
							.with(csrf())
							.queryParam("dose", "Test")
							.queryParam("medicine", "Cat medicine"))
			   .andExpect(status().is3xxRedirection())
			   .andExpect(view().name("redirect:/owners/1/pets/1/visits/1/medical-record/show?id=1"));
	}
	
	@WithMockUser(username = "vet1", authorities = {"veterinarian"})
	@Test
	void testProccessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/*/pets/*/visits/*/medical-record/{medical-recordId}/prescription/create", 1)
							.with(csrf())
							.queryParam("dose", "Test")
							.queryParam("medicine", ""))
			   .andExpect(status().isOk())
			   .andExpect(view().name("prescription/form"))
			   .andExpect(model().hasErrors())
			   .andExpect(model().attributeHasErrors("prescription"))
			   .andExpect(model().attributeExists("prescription"));
	}
	
}
