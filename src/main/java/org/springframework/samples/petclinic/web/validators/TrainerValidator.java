package org.springframework.samples.petclinic.web.validators;

import org.hibernate.validator.internal.util.StringHelper;
import org.springframework.samples.petclinic.model.Trainer;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TrainerValidator implements Validator {

	private static final String REQUIRED = "required";
	private static final String EMAIL = "required as follows: acme@example.com";
	private static final String PHONE = "required as follows: 999999999";
	private static final String NAME_LENGTH = "required length between 3 and 50";
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Trainer.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		Trainer trainer;
		String firstName;
		String lastName;
		String email;
		String phone;
		
		trainer = (Trainer) target;
		firstName = trainer.getFirstName();
		lastName = trainer.getLastName();
		email = trainer.getEmail();
		phone = trainer.getPhone();
		
		if(StringHelper.isNullOrEmptyString(firstName)) {
			errors.rejectValue("firstName", REQUIRED, "Required");
		}
		
		if(StringHelper.isNullOrEmptyString(lastName)) {
			errors.rejectValue("lastName", REQUIRED, "Required");
		}
		
		if(StringHelper.isNullOrEmptyString(email)) {
			errors.rejectValue("email", REQUIRED, "Required");
		} else if (!email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")){
			errors.rejectValue("email", EMAIL, "Email should match the following pattern: acme@example.com");
		}
		
		if(StringHelper.isNullOrEmptyString(phone) || !phone.matches("[0-9]+")) {
			errors.rejectValue("phone", PHONE, "The phone should be added and it should contain only numbers");
		}
		
	}

}
