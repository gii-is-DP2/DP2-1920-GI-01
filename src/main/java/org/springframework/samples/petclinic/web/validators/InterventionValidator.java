
package org.springframework.samples.petclinic.web.validators;

import java.time.LocalDate;

import org.apache.logging.log4j.util.Strings;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class InterventionValidator implements Validator {

	private static final String REQUIRED = "required";


	@Override
	public boolean supports(final Class<?> clazz) {
		return Intervention.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors) {
		Intervention intervention = (Intervention) obj;

		if (intervention.getInterventionDate() == null || intervention.getInterventionDate().isBefore(LocalDate.now())) {
			errors.rejectValue("interventionDate", "required and after current date", "required and after current date");
		}

		if (Strings.isBlank(intervention.getInterventionDescription())) {
			errors.rejectValue("interventionDescription", InterventionValidator.REQUIRED, InterventionValidator.REQUIRED);
		}

		if (intervention.getInterventionTime() == null) {
			errors.rejectValue("interventionTime", InterventionValidator.REQUIRED, InterventionValidator.REQUIRED);
		}

	}

}
