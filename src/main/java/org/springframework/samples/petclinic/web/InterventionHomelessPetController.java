package org.springframework.samples.petclinic.web;

import java.security.Principal; 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.InterventionService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.web.validators.InterventionValidator;
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
public class InterventionHomelessPetController {

	private final PetService petService;
	private final InterventionService interventionService;
	private final VetService vetService;
	
	@Autowired
	public InterventionHomelessPetController(PetService petService, InterventionService interventionService, VetService vetService) {
		this.petService = petService;
		this.interventionService = interventionService;
		this.vetService = vetService;
	}
	
	@InitBinder("intervention")
	public void initInterventionBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new InterventionValidator());
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
	
	@GetMapping("/homeless-pets/{petId}/interventions/new")
	public String initNewInterventionHomelessPetForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Intervention intervention = new Intervention();
			Pet pet = this.petService.findPetById(petId);
			pet.addIntervention(intervention);
			model.put("intervention", intervention);
			view = "homelessPets/editIntervention";
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/interventions/new")
	public String processNewInterventionHomelessPetForm(@PathVariable("petId") int petId, @Valid Intervention intervention, BindingResult result, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Pet pet = this.petService.findPetById(petId);
			pet.addIntervention(intervention);
			if (result.hasErrors()) {
				model.addAttribute("intervention", intervention);
				view = "homelessPets/editIntervention";
			}
			else {
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String username = ((UserDetails)principal).getUsername();
				Optional<Vet> vet = this.vetService.findVetByUsername(username);
				if(vet.isPresent()) {
					intervention.setVet(vet.get());
					this.interventionService.saveIntervention(intervention);
				}
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/interventions/{interventionId}/edit")
	public String initEditInterventionHomelessPetForm(@PathVariable("petId") int petId, @PathVariable("interventionId") int interventionId, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			view = "homelessPets/editIntervention";
			Optional<Intervention> intervention = this.interventionService.findInterventionById(interventionId);
			if(intervention.isPresent()) {
				model.addAttribute("intervention", intervention.get());
			} else {
				model.addAttribute("message", "Intervention not found!");
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/interventions/{interventionId}/edit")
	public String processEditInterventionHomelessPetForm(@PathVariable("petId") int petId, @PathVariable("interventionId") int interventionId, @Valid Intervention intervention, BindingResult result, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			if(result.hasErrors()) {
				model.put("intervention", intervention);
				view = "homelessPets/editIntervention";
			} else {
				Optional<Intervention> interventionToUpdate = this.interventionService.findInterventionById(interventionId);
				if(interventionToUpdate.isPresent()) {
					BeanUtils.copyProperties(intervention, interventionToUpdate.get(), "id", "pet", "vet");
					try {
						this.interventionService.saveIntervention(interventionToUpdate.get());
					} catch (Exception e) {
						view = "homelessPets/editIntervention";
					}
				}
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/interventions/{interventionId}/delete")
	public String deleteInterventionHomelessPet(@PathVariable("petId") int petId, @PathVariable("interventionId") int interventionId, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("veterinarian");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Optional<Intervention> intervention;
			view = "homelessPets/listPets";
			intervention = this.interventionService.findInterventionById(interventionId);
			Pet pet = this.petService.findPetById(petId);
			if(intervention.isPresent()) {
				pet.removeIntervention(intervention.get());
				Vet vet = intervention.get().getVet();
				vet.removeIntervention(intervention.get());
				this.interventionService.delete(intervention.get());
				model.addAttribute("message", "Intervention deleted successfully!");
				view = "redirect:/homeless-pets/" + petId;
			} else {
				model.addAttribute("message", "Intervention not found!");
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
}