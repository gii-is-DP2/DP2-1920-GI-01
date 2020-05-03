
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Adoption;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdoptionController {

	private static final String	CREATE_VIEW	= "adoption/form";
	private static final String	LIST_VIEW	= "adoption/list";

	@Autowired
	private AdoptionService		adoptionService;

	@Autowired
	private OwnerService		ownerService;

	@Autowired
	private PetService			petService;


	@ModelAttribute("owners")
	public Collection<Owner> populateOwners() {
		return this.ownerService.findAll();
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "date", "pet");
	}

	@GetMapping("/homeless-pets/{petId}/adopt")
	public String initCreationForm(final ModelMap model) {
		Adoption adoption;

		adoption = new Adoption();

		adoption.setDate(LocalDate.now());

		model.put("adoption", adoption);

		return AdoptionController.CREATE_VIEW;
	}

	@PostMapping("/homeless-pets/{petId}/adopt")
	public String proccessCreationForm(@PathVariable final Integer petId, final Adoption adoption, final BindingResult result, final ModelMap model) {
		Owner owner;
		Pet pet;
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

		owner = adoption.getOwner();
		pet = this.petService.findPetById(petId);

		validator.afterPropertiesSet();
		adoption.setPet(pet);
		adoption.setOwner(owner);
		adoption.setDate(LocalDate.now());

		validator.validate(adoption, result);
		validator.close();

		if (result.hasErrors()) {
			model.put("adoption", adoption);
			return AdoptionController.CREATE_VIEW;
		}

		owner.addPet(pet);
		owner.addAdoptions(adoption);

		this.adoptionService.saveAdoption(adoption);
		this.ownerService.saveOwner(owner);
		try {
			this.petService.savePet(pet);
		} catch (DuplicatedPetNameException e) {
			e.printStackTrace();
			return "redirect:/oups";
		}

		return "redirect:/owners/" + owner.getId();
	}

	@GetMapping("/owners/*/pets/{petId}/adoption-history")
	public String listAdoptions(@PathVariable final Integer petId, final ModelMap model) {
		Collection<Adoption> adoptions;

		adoptions = this.adoptionService.findByPetId(petId);

		model.put("adoptions", adoptions);

		return AdoptionController.LIST_VIEW;
	}

}
