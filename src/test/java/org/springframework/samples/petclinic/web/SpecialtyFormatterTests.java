package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.web.formatters.SpecialtyFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SpecialtyFormatterTests {

	@Mock
	private VetService vetService;
	
	private SpecialtyFormatter specialtyFormatter;
	
	@BeforeEach
	void setup() {
		specialtyFormatter = new SpecialtyFormatter(vetService);
	}
	
	@Test
	void testPrint() {
		Specialty specialty = new Specialty();
		specialty.setName("Dentistry");
		String specialtyName = specialtyFormatter.print(specialty, Locale.ENGLISH);
		assertEquals("Dentistry", specialtyName);
	}
	
	@Test
	void shouldParse() throws ParseException {
		Mockito.when(vetService.findAllSpecialty()).thenReturn(makeSpecialties());
		Specialty specialty = specialtyFormatter.parse("Dentistry", Locale.ENGLISH);
		assertEquals("Dentistry", specialty.getName());
	}
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(vetService.findAllSpecialty()).thenReturn(makeSpecialties());
		Assertions.assertThrows(ParseException.class, () -> {
			specialtyFormatter.parse("Dinosaur", Locale.ENGLISH);
		});
	}

	private Collection<Specialty> makeSpecialties() {
		Collection<Specialty> specialties = new ArrayList<>();
		specialties.add(new Specialty() {
			{
				setName("Dentistry");
			}
		});
		specialties.add(new Specialty() {
			{
				setName("Radiology");
			}
		});
		return specialties;
	}
	
}
