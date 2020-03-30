package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.RehabService;
import org.springframework.samples.petclinic.service.TrainerService;
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
public class RehabHomelessPetController {

	private final PetService petService;
	private final RehabService rehabService;
	private final TrainerService trainerService;
	
	@Autowired
	public RehabHomelessPetController(PetService petService, RehabService rehabService, TrainerService trainerService) {
		this.petService = petService;
		this.rehabService = rehabService;
		this.trainerService = trainerService;
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
	
	@GetMapping("/homeless-pets/{petId}/rehabs/new")
	public String initNewRehabHomelessPetForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		
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
			view = "homelessPets/editRehab";
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/rehabs/new")
	public String processNewRehabHomelessPetForm(@PathVariable("petId") int petId, @Valid Rehab rehab, BindingResult result) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("trainer");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Pet pet = this.petService.findPetById(petId);
			if (result.hasErrors()) {
				view = "homelessPets/editRehab";
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
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/rehabs/{rehabId}/edit")
	public String initEditRehabHomelessPetForm(@PathVariable("petId") int petId, @PathVariable("rehabId") int rehabId, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityTrainer = new SimpleGrantedAuthority("trainer");
		authorities.add(authorityTrainer);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			view = "homelessPets/editRehab";
			Optional<Rehab> rehab = this.rehabService.findRehabById(rehabId);
			if(rehab.isPresent()) {
				model.addAttribute("rehab", rehab.get());
			} else {
				model.addAttribute("message", "Rehab not found!");
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/rehabs/{rehabId}/edit")
	public String processEditRehabHomelessPetForm(@PathVariable("petId") int petId, @Valid Rehab rehab, BindingResult result, @PathVariable("rehabId") int rehabId, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("trainer");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			if(result.hasErrors()) {
				model.put("rehab", rehab);
				view = "homelessPets/editRehab";
			} else {
				Optional<Rehab> rehabToUpdate = this.rehabService.findRehabById(rehabId);
				if(rehabToUpdate.isPresent()) {
					BeanUtils.copyProperties(rehab, rehabToUpdate.get(), "id", "pet", "trainer");
					try {
						this.rehabService.saveRehab(rehabToUpdate.get());
					} catch (Exception e) {
						view = "homelessPets/editRehab";
					}
				}
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/rehabs/{rehabId}/delete")
	public String deleteRehabHomelessPet(@PathVariable("petId") int petId, @PathVariable("rehabId") int rehabId, ModelMap model) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("trainer");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Optional<Rehab> rehab;
			view = "homelessPets/listPets";
			rehab = this.rehabService.findRehabById(rehabId);
			Pet pet = this.petService.findPetById(petId);
			if(rehab.isPresent()) {
				Trainer trainer = rehab.get().getTrainer();
				trainer.removeRehab(rehab.get());
				pet.removeRehab(rehab.get());
				this.rehabService.delete(rehab.get());
				model.addAttribute("message", "Rehab deleted successfully!");
				view = "redirect:/homeless-pets/" + petId;
			} else {
				model.addAttribute("message", "Rehab not found!");
				view = "redirect:/homeless-pets/" + petId;
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
}
