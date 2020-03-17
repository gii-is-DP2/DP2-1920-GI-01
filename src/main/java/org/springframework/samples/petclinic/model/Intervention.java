
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "interventions")
public class Intervention extends Visit {

	@Column(name = "intervention_date")
	@NotEmpty
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	interventionDate;

	@Column(name = "intervention_time")
	@NotEmpty
	@DateTimeFormat(pattern = "HH:mm")
	private LocalDate	interventionTime;

	@Column(name = "intervention_description")
	@NotEmpty
	private String		interventionDescription;

	@OneToOne(cascade = CascadeType.ALL)
	private Vet			vet;

}
