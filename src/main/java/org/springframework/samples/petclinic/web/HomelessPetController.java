package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.web.validators.PetValidator;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/homeless-pets")
public class HomelessPetController {

	private final PetService petService;

	private static final String EDIT_FORM = "homelessPets/editPet";
	private static final String VETERINARIAN = "veterinarian";
	private static final String TRAINER = "trainer";
	private static final String MESSAGE = "message";
	private static final String REDIRECT_URL = "redirect:/oups";
	
	@Autowired
	public HomelessPetController(PetService petService) {
		this.petService = petService;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
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
	
	@GetMapping()
	public String listHomelessPets (ModelMap model) {
		
		String view;
		List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities) {
			view = "homelessPets/listPets";
			List<Pet> homelessPets = this.petService.findHomelessPets();
			model.put("homelessPets", homelessPets);
		} else {
			model.addAttribute(MESSAGE, "You are not authorised.");
			view = REDIRECT_URL;
		}
			
		return view;
	}
	
	@GetMapping("/{petId}")
	public String showHomelessPet(@PathVariable("petId") int petId, ModelMap model) {
		String view;
		List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		authorities.add(TRAINER);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities) {
			Pet homelessPet = this.petService.findPetById(petId);
			model.addAttribute("homelessPet", homelessPet);
			view = "homelessPets/showPet";
		} else {
			model.addAttribute(MESSAGE, "You are not authorised.");
			view = REDIRECT_URL;
		}
		return view;
	}

	@GetMapping(value = "/new")
	public String initCreationForm(ModelMap model) {
		
		String view;
		List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities) {
			Pet pet = new Pet();
			model.put("pet", pet);
			view = EDIT_FORM;
		} else {
			view = REDIRECT_URL;
		}
		
		return view;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Pet pet, BindingResult result, ModelMap model) {
		
		String view;
		List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities) {
			if (result.hasErrors()) {
				model.put("pet", pet);
				view = EDIT_FORM;
			}
			else {
		    	try{
		        	this.petService.savePet(pet);
		        }catch(DuplicatedPetNameException ex){
		        	result.rejectValue("name", "duplicate", "already exists");
		        }
		        view = "redirect:/homeless-pets";
			}
		} else {
			view = REDIRECT_URL;
		}
		
		return view;
		
	}

	@GetMapping(value = "/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") int petId, ModelMap model) {
		
		String view;
		List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities) {
			Pet pet = this.petService.findPetById(petId);
			model.put("pet", pet);
			view = EDIT_FORM;
		} else {
			view = REDIRECT_URL;
		}

		return view;
	}
	
    @PostMapping(value = "/{petId}/edit")
    public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner,
    		@PathVariable("petId") int petId, ModelMap model) {
		
    	String view;
    	List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities) {
    		if (result.hasErrors()) {
				model.put("pet", pet);
				view = EDIT_FORM;
			} else {
				Pet petToUpdate=this.petService.findPetById(petId);
				BeanUtils.copyProperties(pet, petToUpdate, "id","visits","interventions", "rehabs");
		        	try {                    
		        		this.petService.savePet(petToUpdate);                    
		            } catch (DuplicatedPetNameException ex) {
		            	result.rejectValue("name", "duplicate", "already exists");
		            }
				view = "redirect:/homeless-pets";
			}
		} else {
			view = REDIRECT_URL;
		}
		
		return view;
	}
    
    @GetMapping(value = {"/{petId}/delete"})
    public String deleteHomelessPet(@PathVariable("petId") int petId, ModelMap model) {
    	
    	String view;
    	List<String> authorities = new ArrayList<String>();
		Boolean hasAuthorities;
		
		authorities.add(VETERINARIAN);
		
		hasAuthorities = userHasAuthorities(makeAuthorities(authorities));
		
		if(hasAuthorities) {
	    	Pet pet = this.petService.findPetById(petId);
	    	if(pet != null) {
	    		petService.deletePet(pet);
	    		model.addAttribute(MESSAGE, "Pet deleted successfully!");
	    		view = listHomelessPets(model);
	    	} else {
	    		model.addAttribute(MESSAGE, "Pet not found!");
	    		view = listHomelessPets(model);
	    	}
		} else {
			view = REDIRECT_URL;
		}
		return view;
    }
	
}
