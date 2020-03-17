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

import org.springframework.beans.BeanUtils;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	private final VetService vetService;

	@Autowired
	public VetController(VetService clinicService) {
		this.vetService = clinicService;
	}
	
	@ModelAttribute("specialties")
	public Collection<Specialty> populateSpecialties() {
		return this.vetService.findAllSpecialty();
	}

	@GetMapping(value = { "/vets"})
	public String showVetList(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}
	
	//This method allows us to list the vets as an admin in a different view
	@GetMapping(value = { "/admin/vets" })
	public String showVetListAsAdmin(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "admin/vets/vetList";
	}
	
	//This method allows us to show a certain pet given an id
	@GetMapping(value = {"/admin/vets/{vetId}"})
	public ModelAndView showVet(@PathVariable("vetId") int vetId) {
		Optional<Vet> vet;
		ModelAndView mav = new ModelAndView("admin/vets/vetShow");
		vet = this.vetService.findVetById(vetId);
		if(vet.isPresent()) {
			mav.addObject("vet", vet.get());
		}
		return mav;
	}
	
	//This method allows us to delete a certain vet given an id
	@GetMapping(value = {"/admin/vets/{vetId}/delete"})
	public String deleteVet(@PathVariable("vetId") int vetId, ModelMap modelMap) {
		String view;
		Optional<Vet> vet;
		view = "admin/vets/vetList";
		vet = this.vetService.findVetById(vetId);
		if(vet.isPresent()) {
			vetService.deleteVet(vet.get());
			modelMap.addAttribute("message", "Vet deleted successfully!");
			view = showVetListAsAdmin(modelMap);
		} else {
			modelMap.addAttribute("message", "Vet not found!");
			view = showVetListAsAdmin(modelMap);
		}
		return view;
	}
	
	@GetMapping("/admin/vets/new")
	public String initCreateForm(ModelMap model) {
		Vet vet = new Vet();
		model.addAttribute("vet", vet);
		return "/admin/vets/vetEdit";
	}
	
	@PostMapping("/admin/vets/new")
	public String processCreateForm(@Valid Vet vet, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.addAttribute("vet", vet);
			return "/admin/vets/vetEdit";
		} else {
			try {
				this.vetService.saveVet(vet);
				return showVetListAsAdmin(model);
			} catch (Exception e) {
				return "/admin/vets/vetEdit";
			}
		}
	}
	
	//This method allows us to display vet data when trying to update
	@GetMapping("/admin/vets/{vetId}/edit")
	public String initUpdateForm(@PathVariable("vetId") int vetId, ModelMap modelMap) {
		Optional<Vet> vet = this.vetService.findVetById(vetId);
		if(vet.isPresent()) {
			modelMap.put("vet", vet.get());
		}
		return "/admin/vets/vetEdit";
	}
	
	@PostMapping("/admin/vets/{vetId}/edit")
	public String processUpdateForm(@Valid Vet vet, BindingResult result, @PathVariable("vetId") int vetId, ModelMap model) {
		if (result.hasErrors()) {
			model.put("vet", vet);
			return "admin/vets/vetEdit";
		} else {
//			Optional<Vet> vetToUpdate = this.vetService.findVetById(vetId);
//			if(vetToUpdate.isPresent()) {
//				BeanUtils.copyProperties(vet, vetToUpdate.get(), "id");
//				try {
////					//vetToUpdate.get().setSpecialtiesInternal((Set<Specialty>) vet.getSpecialties());
////					List<Specialty> modelSpecialties = new ArrayList<Specialty>();
////					modelSpecialties = (List<Specialty>) model.getAttribute("specialties");
////					//modelSpecialties = vet.getSpecialties();
////					for (Specialty s : modelSpecialties) {
////						vetToUpdate.get().addSpecialty(s);
////					}
//					vetToUpdate.get().setId(vetId);
////					vetToUpdate.get().setSpecialtiesInternal(vet.getSpecialties().stream().collect(Collectors.toSet()));
//					this.vetService.saveVet(vetToUpdate.get());
//				} catch (DuplicatedVetNameException ex){
//					result.rejectValue("name", "duplicate", "already exists");
//					return "admin/vets/vetEdit";
//				}
//			}
			try {
				vet.setId(vetId);
				this.vetService.saveVet(vet);
			} catch (DataAccessException e) {
				return "admin/vets/vetEdit";
			}
			return "redirect:/admin/vets";
		}
	}

	@GetMapping(value = { "/vets.xml"})
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}

}
