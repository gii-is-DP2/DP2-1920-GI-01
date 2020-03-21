package org.springframework.samples.petclinic.web.formatters;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.service.MedicineService;
import org.springframework.stereotype.Component;

@Component
public class MedicineFormatter implements Formatter<Medicine> {

	@Autowired
	private MedicineService medicineService;
	
	@Override
	public String print(Medicine medicine, Locale locale) {
		return medicine.getName();
	}

	@Override
	public Medicine parse(String text, Locale locale) throws ParseException {
		Collection<Medicine> medicines = medicineService.findManyAll();
		for (Medicine medicine : medicines) {
			if (medicine.getName().equals(text)) {
				return medicine;
			}
		}
		throw new ParseException("Medicine not found: " + text, 0);
	}

}
