package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/medicine")
public class MedicineController {
	
	private final String CREATE_OR_UPDATE_VIEW = "medicines/form";
	
	private final MedicineService medicineService;
	
	@Autowired
	public MedicineController(MedicineService medicineService) {
		this.medicineService = medicineService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
		
	}
	
	@GetMapping("/create")
	public String initCreationForm(ModelMap model) {
		Medicine medicine;
		
		medicine = new Medicine();
		
		model.put("medicine", medicine);
		
		return CREATE_OR_UPDATE_VIEW;
	}
	
	@PostMapping("/create")
	public String proccessCreationForm(@Valid Medicine medicine, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			model.put("medicine", medicine);
			return CREATE_OR_UPDATE_VIEW;
		}
		
		return "redirect:/";
	}

}
