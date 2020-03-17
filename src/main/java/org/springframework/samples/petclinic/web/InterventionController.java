
package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Pet;
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
public class InterventionController {

	private final PetService petService;


	@Autowired
	public InterventionController(final PetService petService) {
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
	@ModelAttribute("intervention")
	public Intervention loadPetWithIntervention(@PathVariable("petId") final int petId) {
		Pet pet = this.petService.findPetById(petId);
		Intervention intervention = new Intervention();
		pet.addVisit(intervention);
		return intervention;
	}

	// Spring MVC calls method loadPetWithINtervention(...) before initNewVisitForm is called
	@GetMapping(value = "/owners/*/pets/{petId}/interventions/new")
	public String initNewINterventionForm(@PathVariable("petId") final int petId, final Map<String, Object> model) {
		return "pets/createOrUpdateINterventionForm";
	}

	// Spring MVC calls method loadPetWithIntervention(...) before processNewINterventionForm is called
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/interventions/new")
	public String processNewInterventionForm(@Valid final Intervention intervention, final BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateInterventionForm";
		} else {
			this.petService.saveIntervention(intervention);
			return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/owners/*/pets/{petId}/interventions")
	public String showInterventions(@PathVariable final int petId, final Map<String, Object> model) {
		model.put("interventions", this.petService.findInterventionsByPetId(petId));
		return "interventionList";
	}
}
