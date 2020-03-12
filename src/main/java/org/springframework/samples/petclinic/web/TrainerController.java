package org.springframework.samples.petclinic.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

	//Revisar lo siguiente después de que se termine la teoría de
	//Advanced Unit Testing
	@Autowired
	private TrainerService	trainerService;
	
	@GetMapping()
	public String listTrainers(ModelMap modelMap) {
		String view;
		Iterable<Trainer> trainers;
		
		view = "trainers/listTrainers";
		trainers = trainerService.findAll();
		modelMap.addAttribute("trainers", trainers);
		return view;
	}
	
	@GetMapping("/{trainerId}")
	public ModelAndView showTrainer(@PathVariable("trainerId") int trainerId) {
		Optional<Trainer> trainer;
		ModelAndView mav = new ModelAndView("trainers/showTrainer");
		trainer = this.trainerService.findTrainerById(trainerId);
		if(trainer.isPresent()) {
			mav.addObject("trainer", trainer.get());
		}
		return mav;
	}
	
}
