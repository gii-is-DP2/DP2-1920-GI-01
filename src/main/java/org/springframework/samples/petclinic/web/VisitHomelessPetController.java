package org.springframework.samples.petclinic.web;

import java.util.ArrayList; 
import java.util.Collection;
import java.util.List;
import java.util.Map; 
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.MedicalRecordService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.samples.petclinic.web.validators.VisitValidator;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VisitHomelessPetController {
	
	private final VisitService visitService;
	private final PetService petService;
	private final MedicalRecordService medicalRecordService;
	
	private static final String VETERINARIAN = "veterinarian";
	private static final String VISIT = "visit";
	private static final String MESSAGE = "message";
	private static final String EDIT_VIEW = "homelessPets/editVisit";
	private static final String REDIRECT_OUPS_URL = "redirect:/oups";
	private static final String REDIRECT_LIST_VIEW = "redirect:/homeless-pets/";

	@Autowired
	public VisitHomelessPetController(PetService petService, VisitService visitService, MedicalRecordService medicalRecordService) {
		this.petService = petService;
		this.visitService = visitService;
		this.medicalRecordService = medicalRecordService;
	}
	
	@InitBinder("visit")
	public void initVisitBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new VisitValidator());
	}
	
	public boolean userHasAuthorities(Collection<SimpleGrantedAuthority> authorities) {
		Boolean res = false;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			Collection<? extends GrantedAuthority> principalAuthorities = ((UserDetails)principal).getAuthorities();
			if(principalAuthorities.containsAll(authorities)) {
				res = true;
			}
		}
		return res;
	}
	
	public Collection<SimpleGrantedAuthority> makeAuthorities(List<String> authoritiesString) {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (String s: authoritiesString) {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(s);
			authorities.add(authority);
		}
		return authorities;
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/new")
	public String initNewVisitHomelessPetForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			Visit visit = new Visit();
			Pet pet = this.petService.findPetById(petId);
			pet.addVisit(visit);
			model.put(VISIT, visit);
			view = EDIT_VIEW;
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/visits/new")
	public String processNewVisitHomelessPetForm(@PathVariable("petId") int petId, @Valid Visit visit, BindingResult result) {
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			Pet pet = this.petService.findPetById(petId);
			if (result.hasErrors()) {
				view = EDIT_VIEW;
			}
			else {
				pet.addVisit(visit);
				this.petService.saveVisit(visit);
				view = REDIRECT_LIST_VIEW + petId;
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
			
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/{visitId}/edit")
	public String initEditVisitHomelessPetForm(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, ModelMap model) {
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			view = EDIT_VIEW;
			Optional<Visit> visit = this.visitService.findVisitById(visitId);
			if(visit.isPresent()) {
				model.addAttribute(VISIT, visit.get());
			} else {
				model.addAttribute(MESSAGE, "Visit not found!");
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/visits/{visitId}/edit")
	public String processEditVisitHomelessPetForm(@PathVariable("petId") int petId, @Valid Visit visit, BindingResult result, @PathVariable("visitId") int visitId, ModelMap model) {
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			if(result.hasErrors()) {
				model.put(VISIT, visit);
				view = EDIT_VIEW;
			} else {
				Optional<Visit> visitToUpdate = this.visitService.findVisitById(visitId);
				if(visitToUpdate.isPresent()) {
					BeanUtils.copyProperties(visit, visitToUpdate.get(), "id", "pet");
					try {
						this.petService.saveVisit(visitToUpdate.get());
					} catch (Exception e) {}
				}
				view = REDIRECT_LIST_VIEW + petId;
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/{visitId}/delete")
	public String deleteVisitHomelessPet(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, ModelMap model) {
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			Optional<Visit> visit;
			visit = this.visitService.findVisitById(visitId);
			Pet pet = this.petService.findPetById(petId);
			if(visit.isPresent()) {
				pet.removeVisit(visit.get());
				MedicalRecord mr = this.medicalRecordService.findMedicalRecordByVisitId(visit.get().getId());
				if(mr != null) {
					this.medicalRecordService.deleteMedicalRecord(mr);
				}
				visitService.delete(visit.get());
				model.addAttribute(MESSAGE, "Visit deleted successfully!");
				view = REDIRECT_LIST_VIEW + petId;
			} else {
				model.addAttribute(MESSAGE, "Visit not found!");
				view = REDIRECT_LIST_VIEW + petId;
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
}
