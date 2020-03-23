/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

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
	public VetController(final VetService clinicService) {
		this.vetService = clinicService;
	}

	@ModelAttribute("specialties")
	public Collection<Specialty> populateSpecialties() {
		return this.vetService.findAllSpecialty();
	}

	@GetMapping("/vets")
	public String showVetList(final Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}

	//This method allows us to list the vets as an admin in a different view
	@GetMapping("/admin/vets")
	public String showVetListAsAdmin(final Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "admin/vets/vetList";
	}

	//This method allows us to show a certain pet given an id
	@GetMapping("/vets/{vetId}")
	public ModelAndView showVet(@PathVariable("vetId") final int vetId) {
		Optional<Vet> vet;
		ModelAndView mav = new ModelAndView("/vets/vetDetails");
		vet = this.vetService.findVetById(vetId);
		if (vet.isPresent()) {
			mav.addObject("vet", vet.get());
		}
		return mav;
	}
	
	//This method allows us to show a certain pet given an id
	@GetMapping(value = {"/admin/vets/{vetId}"})
	public ModelAndView showVetAsAdmin(@PathVariable("vetId") int vetId) {
		Optional<Vet> vet;
		ModelAndView mav = new ModelAndView("admin/vets/vetShow");
		vet = this.vetService.findVetById(vetId);
		if(vet.isPresent()) {
			mav.addObject("vet", vet.get());
		}
		return mav;
	}

	//This method allows us to delete a certain vet given an id
	@GetMapping("/admin/vets/{vetId}/delete")
	public String deleteVet(@PathVariable("vetId") final int vetId, final ModelMap modelMap) {
		String view;
		Optional<Vet> vet;
		view = "admin/vets/vetList";
		vet = this.vetService.findVetById(vetId);
		if (vet.isPresent()) {
			this.vetService.deleteVet(vet.get());
			modelMap.addAttribute("message", "Vet deleted successfully!");
			view = this.showVetListAsAdmin(modelMap);
		} else {
			modelMap.addAttribute("message", "Vet not found!");
			view = this.showVetListAsAdmin(modelMap);
		}
		return view;
	}

	@GetMapping("/admin/vets/new")
	public String initCreateForm(final ModelMap model) {
		Vet vet = new Vet();
		model.addAttribute("vet", vet);
		return "admin/vets/vetEdit";
	}

	@PostMapping("/admin/vets/new")
	public String processCreateForm(@Valid final Vet vet, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("vet", vet);
			return "admin/vets/vetEdit";
		} else {
			try {
				this.vetService.saveVet(vet);
				return this.showVetListAsAdmin(model);
			} catch (Exception e) {
				return "admin/vets/vetEdit";
			}
		}
	}

	//This method allows us to display vet data when trying to update
	@GetMapping("/admin/vets/{vetId}/edit")
	public String initUpdateForm(@PathVariable("vetId") final int vetId, final ModelMap modelMap) {
		Optional<Vet> vet = this.vetService.findVetById(vetId);
		if (vet.isPresent()) {
			modelMap.put("vet", vet.get());
		}
		return "admin/vets/vetEdit";
	}

	@PostMapping("/admin/vets/{vetId}/edit")
	public String processUpdateForm(@Valid final Vet vet, final BindingResult result, @PathVariable("vetId") final int vetId, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("vet", vet);
			return "admin/vets/vetEdit";
		} else {
			try {
				vet.setId(vetId);
				this.vetService.saveVet(vet);
			} catch (DataAccessException e) {
				return "admin/vets/vetEdit";
			}
			return "redirect:/admin/vets";
		}
	}

	@GetMapping(value = {
		"/vets.xml"
	})
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}

}
