package org.springframework.samples.petclinic.web;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VisitHomelessPetController {
	
	private final VisitService visitService;
	private final PetService petService;

	@Autowired
	public VisitHomelessPetController(PetService petService, VisitService visitService) {
		this.petService = petService;
		this.visitService = visitService;
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/new")
	public String initNewVisitFormHomelessPet(@PathVariable("petId") int petId, Map<String, Object> model) {
		Visit visit = new Visit();
		Pet pet = this.petService.findPetById(petId);
		pet.addVisit(visit);
		model.put("visit", visit);
		return "homelessPets/editVisit";
	}
	
	@PostMapping("/homeless-pets/{petId}/visits/new")
	public String processNewVisitFormHomelessPet(@PathVariable("petId") int petId, @Valid Visit visit, BindingResult result) {
		Pet pet = this.petService.findPetById(petId);
		if (result.hasErrors()) {
			return "homelessPets/editVisit";
		}
		else {
			pet.addVisit(visit);
			this.petService.saveVisit(visit);
			return "redirect:/homeless-pets";
		}
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/{visitId}/edit")
	public String initEditForm(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, ModelMap model) {
		String view = "/homelessPets/editVisit";
		Optional<Visit> visit = this.visitService.findVisitById(visitId);
		model.addAttribute("visit", visit.get());
		return view;
	}
	
	@PostMapping("/homeless-pets/{petId}/visits/{visitId}/edit")
	public String processEditForm(@PathVariable("petId") int petId, @Valid Visit visit, BindingResult result, @PathVariable("visitId") int visitId, ModelMap model) {
		if(result.hasErrors()) {
			model.put("visit", visit);
			return "/homelessPets/editVisit";
		} else {
			Optional<Visit> visitToUpdate = this.visitService.findVisitById(visitId);
			if(visitToUpdate.isPresent()) {
				BeanUtils.copyProperties(visit, visitToUpdate.get(), "id", "pet");
				try {
					this.petService.saveVisit(visitToUpdate.get());
				} catch (Exception e) {
					return "/homelessPets/editVisit";
				}
			}
			return "redirect:/homeless-pets";
		}
	}
	
	@GetMapping("/homeless-pets/{petId}/visits/{visitId}/delete")
	public String deleteVisit(@PathVariable("petId") int petId, @PathVariable("visitId") int visitId, ModelMap model) {
		String view;
		Optional<Visit> visit;
		view = "homelessPets/listPets";
		visit = this.visitService.findVisitById(visitId);
		Pet pet = this.petService.findPetById(petId);
		if(visit.isPresent()) {
			pet.removeVisit(visit.get());
			visitService.delete(visit.get());
			model.addAttribute("message", "Visit deleted successfully!");
			view = "redirect:/homeless-pets";
		} else {
			model.addAttribute("message", "Visit not found!");
			view = "redirect:/homeless-pets";
		}
		return view;
	}
	
}
