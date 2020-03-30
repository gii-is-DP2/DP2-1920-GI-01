package org.springframework.samples.petclinic.web;

import java.util.ArrayList; 
import java.util.Collection;
import java.util.Map; 
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
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

	@Autowired
	public VisitHomelessPetController(PetService petService, VisitService visitService) {
		this.petService = petService;
		this.visitService = visitService;
	}
	
	@InitBinder("visit")
	public void initVisitBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new VisitValidator());
	}
	
	//This method will let us check security
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
	
	@GetMapping("/homeless-pets/{petId}/visits/new")
	public String initNewVisitHomelessPetForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Visit visit = new Visit();
			Pet pet = this.petService.findPetById(petId);
			pet.addVisit(visit);
			model.put("visit", visit);
			view = "homelessPets/editVisit";
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/visits/new")
	public String processNewVisitHomelessPetForm(@PathVariable("petId") int petId, @Valid Visit visit, BindingResult result) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Pet pet = this.petService.findPetById(petId);
			if (result.hasErrors()) {
				view = "homelessPets/editVisit";
			}
			else {
				pet.addVisit(visit);
				this.petService.saveVisit(visit);
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
			
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/{visitId}/edit")
	public String initEditVisitHomelessPetForm(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			view = "homelessPets/editVisit";
			Optional<Visit> visit = this.visitService.findVisitById(visitId);
			if(visit.isPresent()) {
				model.addAttribute("visit", visit.get());
			} else {
				model.addAttribute("message", "Visit not found!");
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/visits/{visitId}/edit")
	public String processEditVisitHomelessPetForm(@PathVariable("petId") int petId, @Valid Visit visit, BindingResult result, @PathVariable("visitId") int visitId, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			if(result.hasErrors()) {
				model.put("visit", visit);
				view = "homelessPets/editVisit";
			} else {
				Optional<Visit> visitToUpdate = this.visitService.findVisitById(visitId);
				if(visitToUpdate.isPresent()) {
					BeanUtils.copyProperties(visit, visitToUpdate.get(), "id", "pet");
					try {
						this.petService.saveVisit(visitToUpdate.get());
					} catch (Exception e) {
						view = "homelessPets/editVisit";
					}
				}
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/{visitId}/delete")
	public String deleteVisitHomelessPet(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Optional<Visit> visit;
			view = "homelessPets/listPets";
			visit = this.visitService.findVisitById(visitId);
			Pet pet = this.petService.findPetById(petId);
			if(visit.isPresent()) {
				pet.removeVisit(visit.get());
				visitService.delete(visit.get());
				model.addAttribute("message", "Visit deleted successfully!");
				view = "redirect:/homeless-pets/" + petId;
			} else {
				model.addAttribute("message", "Visit not found!");
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
}
