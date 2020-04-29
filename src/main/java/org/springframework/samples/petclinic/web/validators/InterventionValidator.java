
package org.springframework.samples.petclinic.web.validators;

import java.time.LocalDate;

import org.apache.logging.log4j.util.Strings;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class InterventionValidator implements Validator {

	@Override
	public boolean supports(final Class<?> clazz) {
		// TODO Auto-generated method stub
		return Intervention.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors) {
		// TODO Auto-generated method stub
		Intervention intervention = (Intervention) obj;

		if (intervention.getInterventionDate() == null || intervention.getInterventionDate().isBefore(LocalDate.now())) {
			errors.rejectValue("interventionDate", "required and after current date", "required and after current date");
		}
		
		if(Strings.isBlank(intervention.getInterventionDescription())) {
			errors.rejectValue("interventionDescription", "required", "required");
		}
		
		if(intervention.getInterventionTime() == null) {
			errors.rejectValue("interventionTime", "required", "required");
		}

		//	if (intervention.getInterventionDate().isBefore(LocalDate.now().minusDays(1))) {
		//		errors.rejectValue("interventionDate", "required and has to be a date in the future", "required and has to be a date in the future");
		//	}

	}

}
