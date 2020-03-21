package org.springframework.samples.petclinic.web.validators;

import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PrescriptionValidator implements Validator {
	
	private static final String REQUIRED = "required";

	@Override
	public boolean supports(Class<?> clazz) {
		return Prescription.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		Prescription prescription;
		Medicine medicine;
		String dose;
		
		prescription = (Prescription) target;
		medicine = prescription.getMedicine();
		dose = prescription.getDose();
		
		if(dose == null || dose.equals("")) {
			errors.rejectValue("dose", REQUIRED, "Must not be blank");
		}
		
		if(medicine == null) {
			errors.rejectValue("medicine", REQUIRED, "Required");
		} else if(!medicine.getPetType().equals(prescription.getMedicalRecord().getVisit().getPet().getType())) {
			errors.rejectValue("medicine", "medicine.different-types", "That medicine does not correspond with the pet type");
		}
	}

}
