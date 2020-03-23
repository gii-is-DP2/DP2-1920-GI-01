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
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.RehabService;
import org.springframework.stereotype.Controller;
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
public class RehabController {

	private final RehabService rehabService;
	private final PetService petService;

	@Autowired
	public RehabController(PetService petService, RehabService rehabService) {
		this.petService = petService;
		this.rehabService = rehabService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	
	@ModelAttribute("rehab")
	public Rehab loadPetWithRehab(@PathVariable("petId") int petId) {
		Pet pet = this.petService.findPetById(petId);
		Rehab rehab = new Rehab();
		pet.addRehab(rehab);
		return rehab;
	}

	
	@GetMapping(value = "/owners/*/pets/{petId}/rehab/new")
	public String initNewRehabForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return "pets/createOrUpdateRehabForm";
	}

	
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/rehab/new")
	public String processNewRehabForm(@Valid Rehab rehab, BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateRehabForm";
		}
		else {
			this.petService.saveRehab(rehab);
			return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/owners/*/pets/{petId}/rehab")
	public String showRehabs(@PathVariable int petId, Map<String, Object> model) {
		model.put("rehab", this.petService.findPetById(petId).getRehabs());
		return "rehabList";
	}

}

