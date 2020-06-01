
package org.springframework.samples.petclinic.web;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.web.validators.InterventionValidator;
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

@Controller
public class InterventionController {

	private final PetService	petService;

	private static final String	CREATE_FORM		= "pets/createOrUpdateInterventionForm";
	private static final String	INTERVENTION	= "intervention";


	@InitBinder
	public void initInterventionBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new InterventionValidator());
	}

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
		pet.addIntervention(intervention);
		return intervention;
	}

	@GetMapping(value = "/owners/*/pets/{petId}/interventions/new")
	public String initNewVisitForm(@PathVariable("petId") final int petId, final Map<String, Object> model) {
		return InterventionController.CREATE_FORM;
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/interventions/new")
	public String processNewInterventionForm(@Valid final Intervention intervention, final BindingResult result, final ModelMap modelMap) {
		if (result.hasErrors()) {
			modelMap.put(InterventionController.INTERVENTION, intervention);
			return InterventionController.CREATE_FORM;
		} else {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username = ((UserDetails) principal).getUsername();
			Optional<Vet> vet = this.petService.findVetByName(username);

			if (vet.isPresent()) {
				intervention.setVet(vet.get());
			}
			this.petService.saveIntervention(intervention);
			return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/owners/*/pets/{petId}/interventions")
	public String showInterventions(@PathVariable final int petId, final Map<String, Object> model) {
		model.put("interventions", this.petService.findPetById(petId).getInterventions());
		return "interventionList";
	}

	@GetMapping(value = "/owners/*/pets/{petId}/interventions/{interventionId}/delete")
	public String deleteIntervention(@PathVariable("interventionId") final int interventionId, final ModelMap modelMap) {
		String view;
		Optional<Intervention> intervention;
		view = "owners/ownerlist";
		intervention = this.petService.findInterventionById(interventionId);

		if (intervention.isPresent()) {
			this.petService.deleteIntervention(intervention.get());
		}
		return view;
	}

	@GetMapping("/owners/*/pets/{petId}/interventions/{interventionId}/edit")
	public String initUpdateForm(@PathVariable("interventionId") final int interventionId, @PathVariable("petId") final int petId, final ModelMap modelMap) {
		Optional<Intervention> intervention;
		intervention = this.petService.findInterventionById(interventionId);
		if (intervention.isPresent()) {
			modelMap.put(InterventionController.INTERVENTION, intervention.get());
		}
		return InterventionController.CREATE_FORM;
	}

	@PostMapping("/owners/*/pets/{petId}/interventions/{interventionId}/edit")
	public String processUpdateForm(@Valid final Intervention intervention, final BindingResult result, @PathVariable("interventionId") final int interventionId, @PathVariable("petId") final int petId, final ModelMap modelMap) {
		if (result.hasErrors()) {
			modelMap.put(InterventionController.INTERVENTION, intervention);
			return InterventionController.CREATE_FORM;
		} else {
			Optional<Intervention> interventionToUpdate = this.petService.findInterventionById(interventionId);
			if (interventionToUpdate.isPresent()) {
				BeanUtils.copyProperties(intervention, interventionToUpdate.get());
				try {
					this.petService.saveIntervention(interventionToUpdate.get());
				} catch (Exception e) {
					return "pets/createOrUpdateInterventionForm\"";
				}
			}
			return "redirect:/owners/ownerList";
		}
	}

}
