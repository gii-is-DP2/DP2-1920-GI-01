
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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

	private static final String REHAB_FORM = "pets/createOrUpdateRehabForm";
	private static final String ERROR = "redirect:/oups";
	private static final String REHAB = "rehab";
	private static final String OWNERS = "redirect:/owners/{ownerId}";
	private static final String TRAINER = "trainer";
	

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
	
	public Collection<SimpleGrantedAuthority> makeAuthorities(List<String> authoritiesString) {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (String s: authoritiesString) {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(s);
			authorities.add(authority);
		}
		return authorities;
	}
	
	@GetMapping("/owners/*/pets/{petId}/rehab/new")
	public String initNewRehabPetForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		
		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			Rehab rehab = new Rehab();
			Pet pet = this.petService.findPetById(petId);
			pet.addRehab(rehab);
			model.put(REHAB, rehab);
			view = REHAB_FORM;
		} else {
			view = ERROR;
		}
		return view;
	}
	
	@PostMapping("/owners/{ownerId}/pets/{petId}/rehab/new")
	public String processNewRehabForm(@PathVariable("petId") int petId, @Valid Rehab rehab, BindingResult result) {
		
		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		
		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
				if(Boolean.TRUE.equals(hasAuthorities)) {
			Pet pet = this.petService.findPetById(petId);
			if (result.hasErrors()) {
				view = REHAB_FORM;
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
				view = OWNERS;
			}
		} else {
			view = ERROR;
		}
		return view;
	}
	
	

	@GetMapping("/owners/{ownerId}/pets/{petId}/rehab/{rehabId}/delete")
	public String deleteRehab(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId, @PathVariable("rehabId") int rehabId, ModelMap model) {
		

		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		
		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
	
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			Optional<Rehab> rehab;
			//view = "owners/ownerDetails";
			rehab = this.rehabService.findRehabById(rehabId);
			Pet pet = this.petService.findPetById(petId);
			if(rehab.isPresent()) {
				Trainer trainer = rehab.get().getTrainer();
				trainer.removeRehab(rehab.get());
				pet.removeRehab(rehab.get());
				this.rehabService.delete(rehab.get());
				view =OWNERS;
			} else {
				view = OWNERS;
			}
		} else {
			view =ERROR;
		}
		return view;
	
}

	

	@GetMapping("/owners/{ownerId}/pets/{petId}/rehab/{rehabId}/edit")
	public String initEditRehabPetForm(@PathVariable("petId") int petId, @PathVariable("rehabId") int rehabId, @PathVariable("ownerId") int ownerId, ModelMap model) {
	

		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		
		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			view = REHAB_FORM;
			Optional<Rehab> rehab = this.rehabService.findRehabById(rehabId);
			if(rehab.isPresent()) {
				model.addAttribute(REHAB, rehab.get());
			} else {
				model.addAttribute("message", "Rehab not found!");
			}
		} else {
			view = ERROR;
		}
		return view;
	}
	
	@PostMapping("/owners/{ownerId}/pets/{petId}/rehab/{rehabId}/edit")
	public String processEditRehabPetForm(@PathVariable("petId") int petId, @PathVariable("rehabId") int rehabId,@Valid Rehab rehab, BindingResult result, @PathVariable("ownerId") int ownerId, ModelMap model) {
	
		

		String view;
		List<String> authorities = new ArrayList<>();
		Boolean hasAuthorities;
		
		
		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(Boolean.TRUE.equals(hasAuthorities)) {
			if(result.hasErrors()) {
				model.put(REHAB, rehab);
				view = REHAB_FORM;
			} else {
				Optional<Rehab> rehabToUpdate = this.rehabService.findRehabById(rehabId);
				if(rehabToUpdate.isPresent()) {
					BeanUtils.copyProperties(rehab, rehabToUpdate.get(), "id", "pet", TRAINER);
					try {
						this.rehabService.saveRehab(rehabToUpdate.get());
					} catch (Exception e) {
				
					}
				}
				view = OWNERS;
			}
		} else {
			view = ERROR;
		}
		return view;
	}
	

}


