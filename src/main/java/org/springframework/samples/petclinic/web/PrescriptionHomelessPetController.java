package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.MedicalRecordService;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.PrescriptionService;
import org.springframework.samples.petclinic.web.validators.PrescriptionValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PrescriptionHomelessPetController {

	private final MedicineService medicineService;
	private final MedicalRecordService medicalRecordService;
	private final PrescriptionService prescriptionService;
	
	@Autowired
	public PrescriptionHomelessPetController(MedicineService medicineService, MedicalRecordService medicalRecordService, PrescriptionService prescriptionService) {
		this.medicineService = medicineService;
		this.medicalRecordService = medicalRecordService;
		this.prescriptionService = prescriptionService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "medicalRecord");
	}
	
	@InitBinder
	public void initPrescriptionBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PrescriptionValidator());
	}
	
	//This method will let us check security
	public boolean userHasAuthorities(Collection<SimpleGrantedAuthority> authorities) {
		Boolean res = false;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			Collection<? extends GrantedAuthority> principalAuthorities = ((UserDetails)principal).getAuthorities();
			if(authorities.containsAll(principalAuthorities)) {
				res = true;
			}
		}
		return res;
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/prescriptions/new")
	public String initCreationForm(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, @PathVariable("medicalRecordId") int medicalRecordId, ModelMap model) {
		Prescription prescription;
		MedicalRecord medicalRecord;
		Collection<Medicine> medicines;
		PetType petType;
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			prescription = new Prescription();
			medicalRecord = this.medicalRecordService.findMedicalRecordById(medicalRecordId);
			prescription.setMedicalRecord(medicalRecord);
			petType = medicalRecord.getVisit().getPet().getType();
			medicines = this.medicineService.findByPetType(petType);
			view = "homelessPets/editPrescription";
			
			model.addAttribute("prescription", prescription);
			model.addAttribute("medicines", medicines);
		} else {
			view = "redirect:/oups";
		}
			
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/prescriptions/new")
	public String processCreationForm(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, @PathVariable("medicalRecordId") int medicalRecordId, Prescription prescription, BindingResult result, ModelMap model) {
		MedicalRecord medicalRecord;
		Collection<Medicine> medicines;
		PetType petType;
		PrescriptionValidator validator;
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			medicalRecord = this.medicalRecordService.findMedicalRecordById(medicalRecordId);
			validator = new PrescriptionValidator();
			prescription.setMedicalRecord(medicalRecord);
			validator.validate(prescription, result);
			
			if(result.hasErrors()) {
				petType = medicalRecord.getVisit().getPet().getType();
				medicines = this.medicineService.findByPetType(petType);
				model.addAttribute("prescription", prescription);
				model.addAttribute("medicines", medicines);
				view = "homelessPets/editPrescription";
			} else {
				this.prescriptionService.savePrescription(prescription);
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		
		return view;
	}
	
}
