package org.springframework.samples.petclinic.web.formatters;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.stereotype.Component;

@Component
public class OwnerFormatter implements Formatter<Owner> {

	@Autowired
	private OwnerService ownerService;
	
	@Override
	public String print(Owner owner, Locale locale) {
		return owner.getFirstName() + " " + owner.getLastName();
	}

	@Override
	public Owner parse(String text, Locale locale) throws ParseException {
		Collection<Owner> owners = ownerService.findAll();
		for(Owner owner : owners) {
			if(text.contains(owner.getFirstName()) && text.contains(owner.getLastName())) {
				return owner;
			}
		}
		throw new ParseException("Owner not found: " + text, 0);
	}

}
