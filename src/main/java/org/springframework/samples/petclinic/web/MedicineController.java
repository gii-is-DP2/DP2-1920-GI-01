package org.springframework.samples.petclinic.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.web.validators.MedicineValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/medicine")
public class MedicineController {
	
	private static final String SHOW_VIEW = "medicine/show";

	private static final String CREATE_OR_UPDATE_VIEW = "medicine/form";
	
	@Autowired
	private MedicineService medicineService;
	
	@Autowired
	private PetService petService;
	
	@ModelAttribute("petTypes")
	public Collection<PetType> populatePetTypes() {
		return petService.findPetTypes();
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");	
	}
	
	@InitBinder
	public void initMedicineBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new MedicineValidator());
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
		Integer medicineId;
		String redirection;
		
		if(result.hasErrors()) {
			model.put("medicine", medicine);
			return CREATE_OR_UPDATE_VIEW;
		}
		
		medicineService.saveMedicine(medicine);
		
		medicineId = medicine.getId();
		redirection = "redirect:/medicine/show?id=" + medicineId;
		
		return redirection;
	}
	
	@GetMapping("/show")
	public String showMedicine(@RequestParam(value = "id", required = true) Integer medicineId, ModelMap model) {
		Medicine medicine;
		
		medicine = medicineService.findMedicineById(medicineId);
		
		if(medicine == null) {
			throw new NullPointerException("Medicine not found");
		}
		
		model.put("medicine", medicine);
		
		return SHOW_VIEW;
	}
	
}
