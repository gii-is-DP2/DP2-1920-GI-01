package org.springframework.samples.petclinic.web.validators;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Visit;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VisitValidator implements Validator {
	
	private static final String REQUIRED = "required";

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Visit.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		Visit visit = (Visit) target;
		LocalDate date = visit.getDate();
		
		if(visit.isNew() && (date == null || date.isBefore(LocalDate.now()))) {
			errors.rejectValue("date", REQUIRED + " and after current date", REQUIRED + " and after current date");
		}
	}

	
	
}
