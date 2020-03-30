package org.springframework.samples.petclinic.web.validators;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RehabValidator implements Validator {
	
		
	private static final String REQUIRED = "required";

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Rehab.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		Rehab rehab = (Rehab) target;
		LocalDate date = rehab.getDate();
		
		
		if(rehab.isNew() && (date == null || date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now()) )) {
			errors.rejectValue("date", REQUIRED + " is before current date", REQUIRED + " is after current date");
		}
		
	}
	
	}		



		
		/* expecting actual not to be null, sis lai pagaidam paliek komentos
		 * 
		 * Integer time = rehab.getTime();
		if (rehab.isNew() && (time == null)) {
			errors.rejectValue("time", REQUIRED + " cant be empty");
		} */
	
