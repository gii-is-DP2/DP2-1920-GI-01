package org.springframework.samples.petclinic.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.service.MedicalRecordService;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.PrescriptionService;
import org.springframework.samples.petclinic.web.validators.PrescriptionValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owners/*/pets/*/visits/*/medical-record/{medical-recordId}/prescription")
public class PrescriptionController {
	
	private static final String CREATE_VIEW = "prescription/form";
	
	@Autowired
	private MedicineService medicineService;
	
	@Autowired
	private MedicalRecordService medicalRecordService;
	
	@Autowired 
	private PrescriptionService prescriptionService;
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "medicalRecord");
	}
	
	@InitBinder
	public void initPrescriptionBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PrescriptionValidator());
	}
	
	@GetMapping("/create")
	public String initCreationForm(@PathVariable("medical-recordId") Integer medicalRecordId, ModelMap model) {
		Prescription prescription;
		MedicalRecord medicalRecord;
		Collection<Medicine> medicines;
		PetType petType;
		
		prescription = new Prescription();
		medicalRecord = medicalRecordService.findMedicalRecordById(medicalRecordId);
		petType = medicalRecord.getVisit().getPet().getType();
		medicines = medicineService.findByPetType(petType);
		
		prescription.setMedicalRecord(medicalRecord);
		model.put("prescription", prescription);
		model.put("medicines", medicines);
		
		return CREATE_VIEW;
	}
	
	@PostMapping("/create")
	public String proccessCreationForm(@PathVariable("medical-recordId") Integer medicalRecordId, Prescription prescription, BindingResult result, ModelMap model) {
		String ownerId;
		String petId;
		String visitId;
		String redirection;
		MedicalRecord medicalRecord;
		PrescriptionValidator validator;
		
		validator = new PrescriptionValidator();
		medicalRecord = medicalRecordService.findMedicalRecordById(medicalRecordId);
		prescription.setMedicalRecord(medicalRecord);
		
		validator.validate(prescription, result);
		
		if(result.hasErrors()) {
			Collection<Medicine> medicines;
			PetType petType;
			
			petType = medicalRecord.getVisit().getPet().getType();
			medicines = medicineService.findByPetType(petType);
			
			model.put("prescription", prescription);
			model.put("medicines", medicines);
			return CREATE_VIEW;
		}
		
		prescriptionService.savePrescription(prescription);
		
		visitId = prescription.getMedicalRecord().getVisit().getId().toString();
		petId = prescription.getMedicalRecord().getVisit().getPet().getId().toString();
		ownerId = prescription.getMedicalRecord().getVisit().getPet().getOwner().getId().toString();
		redirection = "redirect:/owners/" + ownerId + "/pets/" + petId + "/visits/" + visitId + "/medical-record/show?id=" + medicalRecordId;
		
		
		return redirection;
	}
	

}
