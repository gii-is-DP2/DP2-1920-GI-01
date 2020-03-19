package org.springframework.samples.petclinic.web;

import java.util.Optional; 

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.samples.petclinic.web.validators.TrainerValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TrainerController {

	//Revisar lo siguiente después de que se termine la teoría de
	//Advanced Unit Testing
	@Autowired
	private TrainerService	trainerService;
	
	@InitBinder
	public void initTrainerBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new TrainerValidator());
	}
	
	@GetMapping("/trainers")
  public String listTrainers(ModelMap modelMap) {
    String view;
    Iterable<Trainer> trainers;
    view = "trainers/listTrainers";
    trainers = trainerService.findAll();
    modelMap.addAttribute("trainers", trainers);
    return view;
  }
  
	@GetMapping("/admin/trainers")
	public String listTrainersAsAdmin(ModelMap modelMap) {
		String view;
		Iterable<Trainer> trainers;
		view = "admin/trainers/listTrainers";
		trainers = trainerService.findAll();
		modelMap.addAttribute("trainers", trainers);
		return view;
	}
  
	@GetMapping("/trainers/{trainerId}")
	public String showTrainer(@PathVariable("trainerId") int trainerId, ModelMap modelMap) {
		Optional<Trainer> trainer;
		String view = "trainers/showTrainer";
    trainer = this.trainerService.findTrainerById(trainerId);
    if(trainer.isPresent()) {
      modelMap.addAttribute("trainer", trainer.get());
    }
    return view;
  }
  
	@GetMapping("/admin/trainers/{trainerId}")
	public ModelAndView showTrainerAsAdmin(@PathVariable("trainerId") int trainerId) {
		Optional<Trainer> trainer;
		ModelAndView mav = new ModelAndView("admin/trainers/showTrainer");
		trainer = this.trainerService.findTrainerById(trainerId);
		if(trainer.isPresent()) {
			mav.addObject("trainer", trainer.get());
		}
		return mav;
	}
	
	@GetMapping("/admin/trainers/{trainerId}/delete")
	public String deleteTrainer(@PathVariable("trainerId") int trainerId, ModelMap modelMap) {
		String view;
		Optional<Trainer> trainer;
		view = "admin/trainers/listTrainers";
		trainer = this.trainerService.findTrainerById(trainerId);
		if(trainer.isPresent()) {
			this.trainerService.deleteTrainer(trainer.get());
			modelMap.addAttribute("message", "Trainer deleted successfully!");
			view = listTrainersAsAdmin(modelMap);
		} else {
			modelMap.addAttribute("message", "Trainer not found!");
			view = listTrainersAsAdmin(modelMap);
		}
		return view;
	}
	
	@GetMapping("/admin/trainers/{trainerId}/edit")
	public String initUpdateForm(@PathVariable("trainerId") int trainerId, ModelMap modelMap) {
		Optional<Trainer> trainer = this.trainerService.findTrainerById(trainerId);
		if(trainer.isPresent()) {
			modelMap.put("trainer", trainer.get());
		}
		return "admin/trainers/editTrainer";
	}
	
	@PostMapping("/admin/trainers/{trainerId}/edit")
	public String processUpdateForm(@Valid Trainer trainer, BindingResult result, @PathVariable("trainerId") int trainerId, ModelMap modelMap) {
		if(result.hasErrors()) {
			modelMap.put("trainer", trainer);
			return "admin/trainers/editTrainer";
		} else {
			Optional<Trainer> trainerToUpdate = this.trainerService.findTrainerById(trainerId);
			if(trainerToUpdate.isPresent()) {
				BeanUtils.copyProperties(trainer, trainerToUpdate.get());
				try {
					this.trainerService.saveTrainer(trainerToUpdate.get());
				} catch (Exception e) {
					return "admin/trainers/editTrainer";
				}
			}
			return "redirect:/admin/trainers";
		}
	}
	
	@GetMapping("/admin/trainers/new")
	public String initCreateForm(ModelMap modelMap) {
		Trainer trainer = new Trainer();
		modelMap.put("trainer", trainer);
		return "admin/trainers/editTrainer";
	}
	
	@PostMapping("/admin/trainers/new")
	public String processCreateForm(@Valid Trainer trainer, BindingResult result, ModelMap modelMap) {
		if(result.hasErrors()) {
			modelMap.put("trainer", trainer);
			return "admin/trainers/editTrainer";
		} else {
			try {
				this.trainerService.saveTrainer(trainer);
			} catch (Exception e) {
				return "admin/trainers/editTrainer";
			}
			return "redirect:/admin/trainers";
		}
  }
}
