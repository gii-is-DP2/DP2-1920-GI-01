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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RehabHomelessPetController {

	private final PetService petService;
	private final RehabService rehabService;
	private final TrainerService trainerService;
	
	private static final String TRAINER = "trainer";
	private static final String REHAB = "rehab";
	private static final String MESSAGE = "message";
	private static final String EDIT_VIEW = "homelessPets/editRehab";
	private static final String REDIRECT_OUPS_URL = "redirect:/oups";
	private static final String REDIRECT_LIST_VIEW = "redirect:/homeless-pets/";
	
	@Autowired
	public RehabHomelessPetController(PetService petService, RehabService rehabService, TrainerService trainerService) {
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
	
	@GetMapping("/homeless-pets/{petId}/rehabs/new")
	public String initNewRehabHomelessPetForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		
		String view;
		List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;

		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities == true) {
			Rehab rehab = new Rehab();
			Pet pet = this.petService.findPetById(petId);
			pet.addRehab(rehab);
			model.put(REHAB, rehab);
			view = EDIT_VIEW;
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/rehabs/new")
	public String processNewRehabHomelessPetForm(@PathVariable("petId") int petId, @Valid Rehab rehab, BindingResult result) {
		String view;
		List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;

		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities == true) {
			Pet pet = this.petService.findPetById(petId);
			if (result.hasErrors()) {
				view = EDIT_VIEW;
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
				view = REDIRECT_LIST_VIEW + petId;
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/rehabs/{rehabId}/edit")
	public String initEditRehabHomelessPetForm(@PathVariable("petId") int petId, @PathVariable("rehabId") int rehabId, ModelMap model) {
		String view;
		List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;

		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities == true) {
			view = EDIT_VIEW;
			Optional<Rehab> rehab = this.rehabService.findRehabById(rehabId);
			if(rehab.isPresent()) {
				model.addAttribute(REHAB, rehab.get());
			} else {
				model.addAttribute(MESSAGE, "Rehab not found!");
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/rehabs/{rehabId}/edit")
	public String processEditRehabHomelessPetForm(@PathVariable("petId") int petId, @Valid Rehab rehab, BindingResult result, @PathVariable("rehabId") int rehabId, ModelMap model) {
		String view;
		List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;

		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities == true) {
			if(result.hasErrors()) {
				model.put(REHAB, rehab);
				view = EDIT_VIEW;
			} else {
				Optional<Rehab> rehabToUpdate = this.rehabService.findRehabById(rehabId);
				if(rehabToUpdate.isPresent()) {
					BeanUtils.copyProperties(rehab, rehabToUpdate.get(), "id", "pet", "trainer");
					try {
						this.rehabService.saveRehab(rehabToUpdate.get());
					} catch (Exception e) {
					}
				}
				view = REDIRECT_LIST_VIEW + petId;
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
	@GetMapping("/homeless-pets/{petId}/rehabs/{rehabId}/delete")
	public String deleteRehabHomelessPet(@PathVariable("petId") int petId, @PathVariable("rehabId") int rehabId, ModelMap model) {
		String view;
		List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;

		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities == true) {
			Optional<Rehab> rehab;
			rehab = this.rehabService.findRehabById(rehabId);
			Pet pet = this.petService.findPetById(petId);
			if(rehab.isPresent()) {
				Trainer trainer = rehab.get().getTrainer();
				trainer.removeRehab(rehab.get());
				pet.removeRehab(rehab.get());
				this.rehabService.delete(rehab.get());
				model.addAttribute(MESSAGE, "Rehab deleted successfully!");
				view = REDIRECT_LIST_VIEW + petId;
			} else {
				model.addAttribute(MESSAGE, "Rehab not found!");
				view = REDIRECT_LIST_VIEW + petId;
			}
		} else {
			view = REDIRECT_OUPS_URL;
		}
		return view;
	}
	
}
