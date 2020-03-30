package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.MedicalRecordService;
import org.springframework.samples.petclinic.service.PrescriptionService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MedicalRecordHomelessPetController {

	private final VisitService visitService;
	private final MedicalRecordService medicalRecordService;
	private final PrescriptionService prescriptionService;
	
	@Autowired
	private MedicalRecordHomelessPetController(VisitService visitService, MedicalRecordService medicalRecordService, PrescriptionService prescriptionService) {
		this.visitService = visitService;
		this.medicalRecordService = medicalRecordService;
		this.prescriptionService = prescriptionService;
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
	
	@GetMapping("/homeless-pets/{petId}/visits/{visitId}/medical-record")
	public String showMedicalRecord(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, ModelMap model) {
		MedicalRecord medicalRecord;
		Collection<Prescription> prescriptions;
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		SimpleGrantedAuthority authorityTrainer = new SimpleGrantedAuthority("trainer");
		authorities.add(authorityVeterinarian);
		authorities.add(authorityTrainer);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			medicalRecord = this.medicalRecordService.findMedicalRecordByVisitId(visitId);
			prescriptions = this.prescriptionService.findManyByMedicalRecord(medicalRecord);
			if(medicalRecord == null) {
				model.addAttribute("message", "There was no medical record found for this visit.");
			} else {
				model.addAttribute("petId", petId);
				model.addAttribute("visitId", visitId);
				model.addAttribute("medicalRecord", medicalRecord);
				model.addAttribute("prescriptions", prescriptions);
			}
			view = "homelessPets/showMedicalRecord";
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/{visitId}/medical-record/new")
	public String intCreationForm(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, ModelMap model) {
		MedicalRecord medicalRecord;
		Optional<Visit> visit;
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
		
			medicalRecord = new MedicalRecord();
			visit = this.visitService.findVisitById(visitId);
			view = "homelessPets/editMedicalRecord";
			
			medicalRecord.setVisit(visit.get());
			model.addAttribute("medicalRecord", medicalRecord);
			//model.addAttribute("visit", visit.get());
		} else {
			view = "redirect:/oups";
		}
		
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/visits/{visitId}/medical-record/new")
	public String processCreationForm(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, @Valid MedicalRecord medicalRecord, BindingResult result, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		Optional<Visit> visit;
		
		if(hasAuthorities == true) {
			visit = this.visitService.findVisitById(visitId);
			
			if(result.hasErrors()) {
				model.addAttribute("medicalRecord", medicalRecord);
				view = "homelessPets/editMedicalRecord";
			} else {
				medicalRecord.setVisit(visit.get());
				this.medicalRecordService.saveMedicalRecord(medicalRecord);
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/edit")
	public String initUpdateForm(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, @PathVariable("medicalRecordId") int medicalRecordId, ModelMap model) {
		MedicalRecord medicalRecord;
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			medicalRecord = this.medicalRecordService.findMedicalRecordById(medicalRecordId);
			if(medicalRecord == null) {
				model.addAttribute("message", "Medical Record not found!");
			} else {
				model.addAttribute("medicalRecord", medicalRecord);
			}
			view = "homelessPets/editMedicalRecord";	
		} else {
			view = "redirect:/oups";
		}
			
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/edit")
	public String processUpdateForm(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, @PathVariable("medicalRecordId") int medicalRecordId, @Valid MedicalRecord medicalRecord, BindingResult result, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		MedicalRecord medicalRecordToUpdate;
		
		if(hasAuthorities == true) {
			if(result.hasErrors()) {
				model.addAttribute("medicalRecord", medicalRecord);
				view = "homelessPets/editMedicalRecord";
			} else {
				medicalRecordToUpdate = this.medicalRecordService.findMedicalRecordById(medicalRecordId);
				BeanUtils.copyProperties(medicalRecord, medicalRecordToUpdate, "id", "name", "visit");
				this.medicalRecordService.saveMedicalRecord(medicalRecordToUpdate);
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/delete")
	public String deleteMedicalRecord(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, @PathVariable("medicalRecordId") int medicalRecordId, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		MedicalRecord medicalRecord;
		
		if(hasAuthorities == true) {
			view = "redirect:/homeless-pets/" + petId;
			medicalRecord = this.medicalRecordService.findMedicalRecordById(medicalRecordId);
			this.prescriptionService.deleteAllAssociated(medicalRecord);
			this.medicalRecordService.deleteMedicalRecord(medicalRecord);
		} else {
			view = "redirect:/oups";
		}
			
		return view;
	}
}
