package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
	
	private static final String VETERINARIAN = "veterinarian";
	private static final String INTERVENTION = "intervention";
	private static final String EDIT_VIEW = "homelessPets/editIntervention";
	private static final String REDIRECT_OUPS_URL = "redirect:/oups";
	private static final String REDIRECT_LIST_VIEW = "redirect:/homeless-pets/";
	private static final String MESSAGE = "message";
	
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
	
	public Collection<SimpleGrantedAuthority> makeAuthorities(List<String> authoritiesString) {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (String s: authoritiesString) {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(s);
			authorities.add(authority);
		}
		return authorities;
	}
	
	@GetMapping("/homeless-pets/{petId}/interventions/new")
	public String initNewInterventionHomelessPetForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			Intervention intervention = new Intervention();
			Pet pet = this.petService.findPetById(petId);
			pet.addIntervention(intervention);
			model.put(INTERVENTION, intervention);
			view = EDIT_VIEW;
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/interventions/new")
	public String processNewInterventionHomelessPetForm(@PathVariable("petId") int petId, @Valid Intervention intervention, BindingResult result, ModelMap model) {
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			Pet pet = this.petService.findPetById(petId);
			pet.addIntervention(intervention);
			if (result.hasErrors()) {
				model.addAttribute(INTERVENTION, intervention);
				view = EDIT_VIEW;
			}
			else {
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String username = ((UserDetails)principal).getUsername();
				Optional<Vet> vet = this.vetService.findVetByUsername(username);
				if(vet.isPresent()) {
					intervention.setVet(vet.get());
					this.interventionService.saveIntervention(intervention);
				}
				view = REDIRECT_LIST_VIEW + petId;
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/interventions/{interventionId}/edit")
	public String initEditInterventionHomelessPetForm(@PathVariable("petId") int petId, @PathVariable("interventionId") int interventionId, ModelMap model) {
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			view = EDIT_VIEW;
			Optional<Intervention> intervention = this.interventionService.findInterventionById(interventionId);
			if(intervention.isPresent()) {
				model.addAttribute(INTERVENTION, intervention.get());
			} else {
				model.addAttribute(MESSAGE, "Intervention not found!");
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/interventions/{interventionId}/edit")
	public String processEditInterventionHomelessPetForm(@PathVariable("petId") int petId, @PathVariable("interventionId") int interventionId, @Valid Intervention intervention, BindingResult result, ModelMap model) {
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			if(result.hasErrors()) {
				model.put(INTERVENTION, intervention);
				view = EDIT_VIEW;
			} else {
				Optional<Intervention> interventionToUpdate = this.interventionService.findInterventionById(interventionId);
				if(interventionToUpdate.isPresent()) {
					BeanUtils.copyProperties(intervention, interventionToUpdate.get(), "id", "pet", "vet");
					try {
						this.interventionService.saveIntervention(interventionToUpdate.get());
					} catch (Exception e) {}
				}
				view = REDIRECT_LIST_VIEW + petId;
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/interventions/{interventionId}/delete")
	public String deleteInterventionHomelessPet(@PathVariable("petId") int petId, @PathVariable("interventionId") int interventionId, ModelMap model) {
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			Optional<Intervention> intervention;
			intervention = this.interventionService.findInterventionById(interventionId);
			Pet pet = this.petService.findPetById(petId);
			if(intervention.isPresent()) {
				pet.removeIntervention(intervention.get());
				Vet vet = intervention.get().getVet();
				vet.removeIntervention(intervention.get());
				this.interventionService.delete(intervention.get());
				model.addAttribute(MESSAGE, "Intervention deleted successfully!");
				view = REDIRECT_LIST_VIEW + petId;
			} else {
				model.addAttribute(MESSAGE, "Intervention not found!");
				view = REDIRECT_LIST_VIEW + petId;
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
}