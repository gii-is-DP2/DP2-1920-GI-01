package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
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

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "homelessPets/editPet";
	
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
	
	@GetMapping()
	public String listHomelessPets (ModelMap model) {
		List<Pet> homelessPets = new ArrayList<Pet>();
		homelessPets = this.petService.findHomelessPets();
		model.put("homelessPets", homelessPets);
		return "homelessPets/listPets";
	}
	
//	@GetMapping("/{petId}")
//	public String showHomelessPet(@PathVariable("petId") int petId, ModelMap model) {
//		Pet homelessPet = new Pet();
//		homelessPet = this.petService.findPetById(petId);
//		model.addAttribute("homelessPet", homelessPet);
//		return "homelessPets/showPet";
//	}

	@GetMapping(value = "/new")
	public String initCreationForm(Owner owner, ModelMap model) {
		Pet pet = new Pet();
		owner.addPet(pet);
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid Pet pet, BindingResult result, ModelMap model) {		
		if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		else {
	    	try{
	        	this.petService.savePet(pet);
	        }catch(DuplicatedPetNameException ex){
	        	result.rejectValue("name", "duplicate", "already exists");
	        	return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	        }
	        return "redirect:/homeless-pets";
		}
	}

	@GetMapping(value = "/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") int petId, ModelMap model) {
		Pet pet = this.petService.findPetById(petId);
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}
	
    @PostMapping(value = "/{petId}/edit")
    public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner,@PathVariable("petId") int petId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		else {
			Pet petToUpdate=this.petService.findPetById(petId);
			BeanUtils.copyProperties(pet, petToUpdate, "id","visits");                                                                                  
	        	try {                    
	        		this.petService.savePet(petToUpdate);                    
	            } catch (DuplicatedPetNameException ex) {
	            	result.rejectValue("name", "duplicate", "already exists");
	            	return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	            }
			return "redirect:/homeless-pets";
		}
	}
    
    @GetMapping(value = {"/{petId}/delete"})
    public String deleteHomelessPet(@PathVariable("petId") int petId, ModelMap model) {
    	String view;
    	Pet pet;
    	view = "homelessPets/listPets";
    	pet = this.petService.findPetById(petId);
    	if(pet != null) {
    		petService.deletePet(pet);
    		model.addAttribute("message", "Pet deleted successfully!");
    		view = listHomelessPets(model);
    	} else {
    		model.addAttribute("message", "Pet not found!");
    		view = listHomelessPets(model);
    	}
    	return view;
    }
	
}
