/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
public class VisitController {

	private final VisitService visitService;
	private final PetService petService;

	@Autowired
	public VisitController(PetService petService, VisitService visitService) {
		this.petService = petService;
		this.visitService = visitService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @GetMapping or @PostMapping annotated method. 2 goals:
	 * - Make sure we always have fresh data - Since we do not use the session scope, make
	 * sure that Pet object always has an id (Even though id is not part of the form
	 * fields)
	 * @param petId
	 * @return Pet
	 */
	@ModelAttribute("visit")
	public Visit loadPetWithVisit(@PathVariable("petId") int petId) {
		Pet pet = this.petService.findPetById(petId);
		Visit visit = new Visit();
		pet.addVisit(visit);
		return visit;
	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping(value = "/owners/*/pets/{petId}/visits/new")
	public String initNewVisitForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return "pets/createOrUpdateVisitForm";
	}
	
	@GetMapping(value="/homeless-pets/{petId}/visits/new")
	public String initNewVisitFormHomelessPet(@PathVariable("petId") int petId, Map<String, Object> model) {
		return "homelessPets/editVisit";
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(@Valid Visit visit, BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm";
		}
		else {
			this.petService.saveVisit(visit);
			return "redirect:/owners/{ownerId}";
		}
	}
	
	@PostMapping(value = "/homeless-pets/{petId}/visits/new")
	public String processNewVisitFormHomelessPet(@Valid Visit visit, BindingResult result) {
		if (result.hasErrors()) {
			return "homelessPets/editVisit";
		}
		else {
			this.petService.saveVisit(visit);
			return "redirect:/homeless-pets";
		}
	}

	@GetMapping(value = "/owners/*/pets/{petId}/visits")
	public String showVisits(@PathVariable int petId, Map<String, Object> model) {
		model.put("visits", this.petService.findPetById(petId).getVisits());
		return "visitList";
	}
	
	@GetMapping("homeless-pets/{petId}/visits/{visitId}/edit")
	public String initEditForm(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, ModelMap model) {
		String view = "/homelessPets/editVisit";
		Optional<Visit> visit = this.visitService.findVisitById(visitId);
		model.addAttribute("visit", visit.get());
		return view;
	}
	
	@PostMapping("homeless-pets/{petId}/visits/{visitId}/edit")
	public String processEditForm(@PathVariable("petId") int petId, @Valid Visit visit, BindingResult result, @PathVariable("visitId") int visitId, ModelMap model) {
		if(result.hasErrors()) {
			model.put("visit", visit);
			return "/homelessPets/editVisit";
		} else {
			Optional<Visit> visitToUpdate = this.visitService.findVisitById(visitId);
			BeanUtils.copyProperties(visit, visitToUpdate.get());
			try {
				//NO SE GUARDA NADA
				//this.visitService.deleteVisit(visit);
				this.visitService.saveVisit(visitToUpdate.get());
			} catch (Exception e) {
				return "/homelessPets/editVisit";
			}
			return "redirect:/homeless-pets";
		}
	}
	
	@GetMapping(value = {"/homeless-pets/{petId}/visits/{visitId}/delete"})
	public String deleteVisit(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, ModelMap model) {
		String view;
		Optional<Visit> visit;
		view = "homelessPets/listPets";
		visit = this.visitService.findVisitById(visitId);
		if(visit.isPresent()) {
			visitService.deleteVisit(visit.get());
			model.addAttribute("message", "Visit deleted successfully!");
			view = "redirect:/homeless-pets";
		} else {
			model.addAttribute("message", "Visit not found!");
			view = "redirect:/homeless-pets";
		}
		return view;
	}

}
