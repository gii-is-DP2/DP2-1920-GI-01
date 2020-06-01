
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.MedicalRecordService;
import org.springframework.samples.petclinic.service.PrescriptionService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = MedicalRecordController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
 class MedicalRecordControllerTests {

	private static final int			TEST_OWNER_ID			= 3;
	private static final int			TEST_PET_ID				= 7;
	private static final int			TEST_VISIT_ID			= 1;
	private static final int			TEST_MEDICAL_RECORD_ID	= 1;
	private static final MedicalRecord	TEST_MEDICAL_RECORD		= new MedicalRecord();

	@Autowired
	private MedicalRecordController		medicalRecordController;

	@MockBean
	private MedicalRecordService		medicalRecordService;

	@MockBean
	private VisitService				visitService;

	@MockBean
	private PrescriptionService			prescriptionService;

	@Autowired
	private MockMvc						mockMvc;


	@BeforeEach
	void setup() {
		Owner testOwner = new Owner();
		testOwner.setFirstName("testName");
		testOwner.setId(3);

		Pet testPet = new Pet();
		testPet.setId(7);
		testPet.setName("testName");
		testOwner.addPet(testPet);

		Visit testVisit = new Visit();
		testVisit.setDescription("Visit Description");
		testVisit.setDate(LocalDate.now());
		testVisit.setPet(testPet);
		BDDMockito.given(this.visitService.findVisitById(MedicalRecordControllerTests.TEST_VISIT_ID)).willReturn(Optional.of(testVisit));
		BDDMockito.given(this.medicalRecordService.findMedicalHistory()).willReturn(Lists.newArrayList(new MedicalRecord()));
		BDDMockito.given(this.medicalRecordService.findMedicalRecordById(MedicalRecordControllerTests.TEST_MEDICAL_RECORD_ID)).willReturn(new MedicalRecord());
		BDDMockito.given(this.medicalRecordService.findMedicalRecordByPetId(MedicalRecordControllerTests.TEST_PET_ID)).willReturn(Lists.newArrayList(new MedicalRecord()));
		BDDMockito.given(this.prescriptionService.findManyByMedicalRecord(MedicalRecordControllerTests.TEST_MEDICAL_RECORD)).willReturn(Lists.newArrayList(new Prescription()));
	}

	@WithMockUser(value = "spring")
	@Test
	void testListMedicalHistoryAsUnregisteredUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/{petId}/medical-history", // 
			MedicalRecordControllerTests.TEST_PET_ID)) //
			.andExpect(MockMvcResultMatchers.status().isOk()) //
			.andExpect(MockMvcResultMatchers.view().name("medicalRecord/list")) //
			.andExpect(MockMvcResultMatchers.model().attributeExists("medicalRecord"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowMedicalRecordAsUnregisteredUser() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/*/visits/{visitId}/medical-record/show", //
			MedicalRecordControllerTests.TEST_VISIT_ID) //
			.queryParam("id", "1")) //
			.andExpect(MockMvcResultMatchers.status().isOk()) //
			.andExpect(MockMvcResultMatchers.view().name("medicalRecord/show"));
		//.andExpect(MockMvcResultMatchers.model().attributeExists("medical-history"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/*/visits/{visitId}/medical-record/new", //
			MedicalRecordControllerTests.TEST_VISIT_ID)) //
			.andExpect(MockMvcResultMatchers.status().isOk()) //
			.andExpect(MockMvcResultMatchers.view().name("medicalRecord/form")) //
			.andExpect(MockMvcResultMatchers.model().attributeExists("medicalRecord"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/*/visits/{visitId}/medical-record/new", MedicalRecordControllerTests.TEST_VISIT_ID) //
			.with(SecurityMockMvcRequestPostProcessors.csrf()) //
			.param("description", "testDescription") //
			.param("status", "testStatus"))//
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()) //
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/3/pets/7/visits/1/medical-record/show?id=null"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/*/visits/{visitId}/medical-record/new", MedicalRecordControllerTests.TEST_VISIT_ID) //
			.with(SecurityMockMvcRequestPostProcessors.csrf()) //
			.param("description", "testDescription") //
			.param("status", "")) //
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("medicalRecord")) //
			.andExpect(MockMvcResultMatchers.status().isOk()) //
			.andExpect(MockMvcResultMatchers.view().name("medicalRecord/form"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/*/pets/*/visits/{visitId}/medical-record/update", //
			MedicalRecordControllerTests.TEST_VISIT_ID) //
			.queryParam("id", "1")) //
			.andExpect(MockMvcResultMatchers.status().isOk()) //
			//				.andExpect(model().attributeExists("trainer"))
			.andExpect(MockMvcResultMatchers.view().name("medicalRecord/form"));

	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/*/visits/{visitId}/medical-record/update", //
			MedicalRecordControllerTests.TEST_VISIT_ID) //
			.queryParam("id", "1") //
			.with(SecurityMockMvcRequestPostProcessors.csrf()) //
			.param("description", "testDescription") //
			.param("status", "testStatus") //
			.param("visitId", "1")) //
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()) //
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/3/pets/7/visits/1/medical-record/show?id=1"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/*/pets/*/visits/{visitId}/medical-record/update", //
			MedicalRecordControllerTests.TEST_VISIT_ID) //
			.queryParam("id", "1") //
			.with(SecurityMockMvcRequestPostProcessors.csrf()) //
			.param("description", "") //
			.param("status", "testStatus")) //
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("medicalRecord")) //
			.andExpect(MockMvcResultMatchers.status().isOk()) //
			.andExpect(MockMvcResultMatchers.view().name("medicalRecord/form"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testDelete() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/visits/*/medical-record/delete", //
			MedicalRecordControllerTests.TEST_OWNER_ID, MedicalRecordControllerTests.TEST_PET_ID) //
			.queryParam("id", "1")) //
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()) //
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/3/pets/7/medical-history"));
	}
}
