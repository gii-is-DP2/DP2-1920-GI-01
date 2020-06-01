
package org.springframework.samples.petclinic.web.formatters;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Component;

@Component
public class SpecialtyFormatter implements Formatter<Specialty> {

	private final VetService vetService;


	@Autowired
	public SpecialtyFormatter(final VetService vetService) {
		this.vetService = vetService;
	}

	@Override
	public String print(final Specialty specialty, final Locale locale) {
		return specialty.getName();
	}

	@Override
	public Specialty parse(final String text, final Locale locale) throws ParseException {
		Collection<Specialty> findSpecialties = this.vetService.findAllSpecialty();
		for (Specialty specialty : findSpecialties) {
			if (specialty.getName().equals(text)) {
				return specialty;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}

}
