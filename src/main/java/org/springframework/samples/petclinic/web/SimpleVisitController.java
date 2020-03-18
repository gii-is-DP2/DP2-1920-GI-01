
package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.SimpleVisit;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SimpleVisitController {

	private final PetService petService;


	@Autowired
	public SimpleVisitController(final PetService petService) {
		this.petService = petService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @GetMapping or @PostMapping annotated method. 2 goals:
	 * - Make sure we always have fresh data - Since we do not use the session scope, make
	 * sure that Pet object always has an id (Even though id is not part of the form
	 * fields)
	 * 
	 * @param petId
	 * @return Pet
	 */
	@ModelAttribute("simple_visit")
	public Visit loadPetWithVisit(@PathVariable("petId") final int petId) {
		Pet pet = this.petService.findPetById(petId);
		SimpleVisit simpleVisit = new SimpleVisit();
		pet.addVisit(simpleVisit);
		return simpleVisit;
	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping(value = "/owners/*/pets/{petId}/simple_visits/new")
	public String initNewVisitForm(@PathVariable("petId") final int petId, final Map<String, Object> model) {
		return "pets/createOrUpdateSimpleVisitForm";
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/simple_visits/new")
	public String processNewVisitForm(@Valid final SimpleVisit simpleVisit, final BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateSimpleVisitForm";
		} else {
			this.petService.saveSimpleVisit(simpleVisit);
			return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/owners/*/pets/{petId}/simple_visits")
	public String showVisits(@PathVariable final int petId, final Map<String, Object> model) {
		model.put("simple_visits", this.petService.findPetById(petId).getSimpleVisits());
		return "simpleVisitList";
	}

}
