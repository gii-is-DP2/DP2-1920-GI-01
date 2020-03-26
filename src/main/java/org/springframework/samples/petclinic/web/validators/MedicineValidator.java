package org.springframework.samples.petclinic.web.validators;

import java.time.LocalDate; 

import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MedicineValidator implements Validator {
	
	private static final String REQUIRED = "required";

	@Override
	public boolean supports(Class<?> clazz) {
		return Medicine.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Medicine medicine;
		String name;
		String maker;
		LocalDate expirationDate;
		PetType petType;
		
		medicine = (Medicine) target;
		name = medicine.getName();
		maker = medicine.getMaker();
		expirationDate = medicine.getExpirationDate();
		petType = medicine.getPetType();
		
		if(name == null || name.equals("")) {
			errors.rejectValue("name", REQUIRED, "Required");
		}
		
		if (!StringUtils.hasLength(name) || name.length() > 50 || name.length() < 3) {
			errors.rejectValue("name", REQUIRED+" and between 3 and 50 characters", REQUIRED+" and between 3 and 50 character");
		}
		
		if(maker == null || maker.equals("")) {
			errors.rejectValue("maker", REQUIRED, "Required");
		}
		
		if(expirationDate == null) {
			errors.rejectValue("expirationDate", REQUIRED, "Required");
		}
		
		if(expirationDate != null && expirationDate.isBefore(LocalDate.now())) {
			errors.rejectValue("expirationDate", "pastDate", "The expiration date must be in the future");
		}
		
		if(petType == null) {
			errors.rejectValue("petType", REQUIRED, "Required");
		}
	}

}
