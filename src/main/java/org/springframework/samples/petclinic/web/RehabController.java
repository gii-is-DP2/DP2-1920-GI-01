
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.RehabService;
import org.springframework.samples.petclinic.service.TrainerService;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */



@Controller
public class RehabController {

	private final RehabService rehabService;
	private final PetService petService;
	private final TrainerService trainerService;


	@Autowired
	public RehabController(PetService petService, RehabService rehabService, TrainerService trainerService) {
		this.petService = petService;
		this.rehabService = rehabService;
		this.trainerService = trainerService;
		
	}


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
	
	@GetMapping("/owners/*/pets/{petId}/rehab/new")
	public String initNewRehabPetForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityTrainer = new SimpleGrantedAuthority("trainer");
		authorities.add(authorityTrainer);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Rehab rehab = new Rehab();
			Pet pet = this.petService.findPetById(petId);
			pet.addRehab(rehab);
			model.put("rehab", rehab);
			view = "pets/createOrUpdateRehabForm";
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@PostMapping("/owners/{ownerId}/pets/{petId}/rehab/new")
	public String processNewRehabForm(@PathVariable("petId") int petId, @Valid Rehab rehab, BindingResult result) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("trainer");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Pet pet = this.petService.findPetById(petId);
			if (result.hasErrors()) {
				view = "pets/createOrUpdateRehabForm";
			}
			else {
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String username = ((UserDetails)principal).getUsername();
				Optional<Trainer> trainer = this.trainerService.findTrainerByUsername(username);
				if(trainer.isPresent()) {
					pet.addRehab(rehab);
					rehab.setTrainer(trainer.get());
					this.rehabService.saveRehab(rehab);
				}
				view = "redirect:/owners/{ownerId}";
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
}




