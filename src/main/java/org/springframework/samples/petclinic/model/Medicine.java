package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Medicine extends NamedEntity {
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Column(name = "expiration_date")
	private LocalDate expirationDate;
	
	@NotBlank
	private String maker;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "type_id")
	@NotNull
	private PetType petType;

}
