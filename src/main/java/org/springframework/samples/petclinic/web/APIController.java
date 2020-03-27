package org.springframework.samples.petclinic.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.samples.petclinic.API.model.Petfinder.Organization;
import org.springframework.samples.petclinic.API.resources.PetfinderResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/organizations")
public class APIController {

	private PetfinderResource petFinderResource = new PetfinderResource();
	
	@GetMapping()
	public String listOrganizations(ModelMap model) throws UnsupportedEncodingException {
		List<org.springframework.samples.petclinic.API.model.Petfinder.Organization> organizations = new ArrayList<org.springframework.samples.petclinic.API.model.Petfinder.Organization>();
		organizations = this.petFinderResource.getOrganizations().getOrganizations();
		model.put("organizations", organizations);
		return "organizations/listOrganizations";
	}
	
	@GetMapping("/{id}")
	public String showOrganization(@PathVariable("id") String id, ModelMap model) throws UnsupportedEncodingException {
		Organization organization = new Organization();
		organization = this.petFinderResource.getOrganizationsById(id).getOrganization();
		model.put("organization", organization);
		return "organizations/showOrganization";
	}
	
}
