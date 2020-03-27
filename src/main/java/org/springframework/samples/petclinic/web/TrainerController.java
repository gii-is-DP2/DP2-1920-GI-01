package org.springframework.samples.petclinic.web;

import java.util.ArrayList; 
import java.util.Collection;
import java.util.Optional; 

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.samples.petclinic.service.TrainerService;
import org.springframework.samples.petclinic.web.validators.TrainerValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
	
	private final TrainerService	trainerService;
	
	@Autowired
	public TrainerController(TrainerService trainerService) {
		this.trainerService = trainerService;
	}
	
	@InitBinder
	public void initTrainerBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new TrainerValidator());
	}
	
	//This method will let us check security
	public boolean userHasAuthorities(Collection<SimpleGrantedAuthority> authorities) {
		Boolean res = false;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			Collection<? extends GrantedAuthority> principalAuthorities = ((UserDetails)principal).getAuthorities();
			if(principalAuthorities.containsAll(authorities)) {
				res = true;
			}
		}
		return res;
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
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Iterable<Trainer> trainers;
			view = "admin/trainers/listTrainers";
			trainers = trainerService.findAll();
			modelMap.addAttribute("trainers", trainers);
		} else {
			view = "redirect:/oups";
		}
		return view;
			
	}
  
	@GetMapping("/trainers/{trainerId}")
	public String showTrainer(@PathVariable("trainerId") int trainerId, ModelMap modelMap) {
		Optional<Trainer> trainer;
		String view = "trainers/showTrainer";
		trainer = this.trainerService.findTrainerById(trainerId);
		if(trainer.isPresent()) {
			modelMap.addAttribute("trainer", trainer.get());
		} else {
			modelMap.addAttribute("message", "Trainer not found!");
		}
		return view;
	}
  
	@GetMapping("/admin/trainers/{trainerId}")
	public ModelAndView showTrainerAsAdmin(@PathVariable("trainerId") int trainerId) {
		
		String view;
		ModelAndView mav;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			view = "admin/trainers/showTrainer";
			Optional<Trainer> trainer;
			mav = new ModelAndView(view);
			trainer = this.trainerService.findTrainerById(trainerId);
			if(trainer.isPresent()) {
				mav.addObject("trainer", trainer.get());
			} else {
		    	mav.addObject("message", "Trainer not found!");
		    }
		} else {
			view = "redirect:/oups";
			mav = new ModelAndView(view);
		}
		
		return mav;
			
	}
	
	@GetMapping("/admin/trainers/{trainerId}/delete")
	public String deleteTrainer(@PathVariable("trainerId") int trainerId, ModelMap modelMap) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);

		if(hasAuthorities == true) {
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
		} else {
			view = "redirect:/oups";
		}
		
		return view;
	}
	
	@GetMapping("/admin/trainers/{trainerId}/edit")
	public String initUpdateForm(@PathVariable("trainerId") int trainerId, ModelMap modelMap) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Optional<Trainer> trainer = this.trainerService.findTrainerById(trainerId);
			if(trainer.isPresent()) {
				modelMap.put("trainer", trainer.get());
			} else {
				modelMap.addAttribute("message", "Trainer not found!");
			}
			view = "admin/trainers/editTrainer";
		} else {
			view = "redirect:/oups";
		}
		return view;
			
	}
	
	@PostMapping("/admin/trainers/{trainerId}/edit")
	public String processUpdateForm(@Valid Trainer trainer, BindingResult result, @PathVariable("trainerId") int trainerId, ModelMap modelMap) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			if(result.hasErrors()) {
				modelMap.put("trainer", trainer);
				view = "admin/trainers/editTrainer";
			} else {
				Optional<Trainer> trainerToUpdate = this.trainerService.findTrainerById(trainerId);
				if(trainerToUpdate.isPresent()) {
					BeanUtils.copyProperties(trainer, trainerToUpdate.get());
					try {
						this.trainerService.saveTrainer(trainerToUpdate.get());
					} catch (Exception e) {
						view = "admin/trainers/editTrainer";
					}
				}
				view = "redirect:/admin/trainers";
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
	}
	
	@GetMapping("/admin/trainers/new")
	public String initCreateForm(ModelMap modelMap) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
			Trainer trainer = new Trainer();
			modelMap.put("trainer", trainer);
			view = "admin/trainers/editTrainer";
		} else {
			modelMap.addAttribute("message", "You don't have permission to perform that action. Please contact the administrator.");
			view = "redirect:/oups";
		}
		return view;	
	}
	
	@PostMapping("/admin/trainers/new")
	public String processCreateForm(@Valid Trainer trainer, BindingResult result, ModelMap modelMap) {
		String view;
		Boolean hasAuthorities;
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority authorityVeterinarian = new SimpleGrantedAuthority("admin");
		authorities.add(authorityVeterinarian);
		
		hasAuthorities = userHasAuthorities(authorities);
		
		if(hasAuthorities == true) {
		
			if(result.hasErrors()) {
				modelMap.put("trainer", trainer);
				view = "admin/trainers/editTrainer";
			} else {
				try {
					this.trainerService.saveTrainer(trainer);
				} catch (Exception e) {
					view = "admin/trainers/editTrainer";
				}
				view = "redirect:/admin/trainers";
			}
		} else {
			view = "redirect:/oups";
		}
		return view;
  }
}
