package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Medicine extends NamedEntity {
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate expirationDate;
	
	@NotEmpty
	private String maker;
	
	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType petType;

}
