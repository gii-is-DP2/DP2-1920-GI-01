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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

	//This method will let us check security
	public boolean userHasAuthorities(final Collection<SimpleGrantedAuthority> authorities) {
		Boolean res = false;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			Collection<? extends GrantedAuthority> principalAuthorities = ((UserDetails) principal).getAuthorities();
			if (principalAuthorities.containsAll(authorities)) {
				res = true;
			}
		}
		return res;
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

	//This method allows us to show a certain pet given an id
	@GetMapping("/vets/{vetId}")
	public ModelAndView showVet(@PathVariable("vetId") final int vetId) {
		Optional<Vet> vet;
		ModelAndView mav = new ModelAndView("vets/vetDetails");
		vet = this.vetService.findVetById(vetId);
		if (vet.isPresent()) {
			mav.addObject("vet", vet.get());
		} else {
			mav.addObject("message", "Vet not found!");
		}
		return mav;
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

	// US-013 Administrator manages vet ----------------------------------------------------------------------------

	//This method allows us to list the vets as an admin in a different view
	@GetMapping("/admin/vets")
	public String showVetListAsAdmin(final Map<String, Object> model) {
		String view;
		Boolean hasAuthorities;

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);

		hasAuthorities = this.userHasAuthorities(authorities);

		if (hasAuthorities) {
			Vets vets = new Vets();
			vets.getVetList().addAll(this.vetService.findVets());
			model.put("vets", vets);
			view = "admin/vets/vetList";
		} else {
			view = "redirect:/oups";
		}
		return view;
	}

	//This method allows us to show a certain pet given an id
	@GetMapping(value = {
		"/admin/vets/{vetId}"
	})
	public ModelAndView showVetAsAdmin(@PathVariable("vetId") final int vetId) {

		String view;
		ModelAndView mav;
		Boolean hasAuthorities;

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);

		hasAuthorities = this.userHasAuthorities(authorities);

		if (hasAuthorities == true) {
			view = "admin/vets/vetShow";
			Optional<Vet> vet;
			mav = new ModelAndView(view);
			vet = this.vetService.findVetById(vetId);
			if (vet.isPresent()) {
				mav.addObject("vet", vet.get());
				if (vet.get().getInterventions().isEmpty()) {
					mav.addObject("message2", "This vet has no interventions");
				}
			} else {
				mav.addObject("message", "Vet not found!");
			}
		} else {
			view = "redirect:/oups";
			mav = new ModelAndView(view);
		}
		return mav;
	}

	//This method allows us to display vet data when trying to create
	@GetMapping("/admin/vets/new")
	public String initCreateForm(final ModelMap model) {
		String view;
		Boolean hasAuthorities;

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);

		hasAuthorities = this.userHasAuthorities(authorities);

		if (hasAuthorities == true) {
			Vet vet = new Vet();
			model.addAttribute("vet", vet);
			view = "admin/vets/vetEdit";
		} else {
			view = "redirect:/oups";
		}
		return view;
	}

	//This method allows us to save a new vet
	@PostMapping("/admin/vets/new")
	public String processCreateForm(@Valid final Vet vet, final BindingResult result, final ModelMap model) {
		String view;
		Boolean hasAuthorities;

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);

		hasAuthorities = this.userHasAuthorities(authorities);

		if (hasAuthorities == true) {
			if (result.hasErrors()) {
				model.addAttribute("vet", vet);
				view = "admin/vets/vetEdit";
			} else {
				try {
					this.vetService.saveVet(vet);
					view = this.showVetListAsAdmin(model);
				} catch (Exception e) {
					view = "admin/vets/vetEdit";
				}
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}

	//This method allows us to display vet data when trying to update
	@GetMapping("/admin/vets/{vetId}/edit")
	public String initUpdateForm(@PathVariable("vetId") final int vetId, final ModelMap modelMap) {
		String view;
		Boolean hasAuthorities;

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);

		hasAuthorities = this.userHasAuthorities(authorities);

		if (hasAuthorities == true) {
			Optional<Vet> vet = this.vetService.findVetById(vetId);
			if (vet.isPresent()) {
				modelMap.put("vet", vet.get());
			} else {
				modelMap.addAttribute("message", "Vet not found!");
			}
			view = "admin/vets/vetEdit";
		} else {
			view = "redirect:/oups";
		}
		return view;
	}

	//This method allows us to update an existing vet
	@PostMapping("/admin/vets/{vetId}/edit")
	public String processUpdateForm(@Valid final Vet vet, final BindingResult result, @PathVariable("vetId") final int vetId, final ModelMap model) {
		String view;
		Boolean hasAuthorities;

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);

		hasAuthorities = this.userHasAuthorities(authorities);

		if (hasAuthorities == true) {
			if (result.hasErrors()) {
				model.put("vet", vet);
				view = "admin/vets/vetEdit";
			} else {
				try {
					vet.setId(vetId);
					this.vetService.saveVet(vet);
				} catch (DataAccessException e) {
					view = "admin/vets/vetEdit";
				}
				view = "redirect:/admin/vets";
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}

	//This method allows us to delete a certain vet given an id
	@GetMapping("/admin/vets/{vetId}/delete")
	public String deleteVet(@PathVariable("vetId") final int vetId, final ModelMap modelMap) {
		String view;
		Boolean hasAuthorities;

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);

		hasAuthorities = this.userHasAuthorities(authorities);

		if (hasAuthorities == true) {
			Optional<Vet> vet;
			view = "admin/vets/vetList";
			vet = this.vetService.findVetById(vetId);
			if (vet.isPresent()) {
				List<Intervention> interventions = vet.get().getInterventions();
				if(interventions == null || interventions.isEmpty()) {
					this.vetService.deleteVet(vet.get());
					modelMap.addAttribute("message", "Vet deleted successfully!");
				} else {
					boolean res = interventions.stream().anyMatch(i -> i.getInterventionDate().isAfter(LocalDate.now()));
					if(res == true) {
						modelMap.addAttribute("message", "You can't delete a vet that has future interventions.");
					} else {
						modelMap.addAttribute("message", "Vet deleted successfully!");
						this.vetService.deleteVet(vet.get());
					}
				}
			} else {
				modelMap.addAttribute("message", "Vet not found!");
			}
			view = this.showVetListAsAdmin(modelMap);
		} else {
			view = "redirect:/oups";
		}
		return view;
	}

}
