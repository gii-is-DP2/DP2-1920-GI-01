
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "medical_records")
public class MedicalRecord extends NamedEntity {

	@NotBlank
	private String	description;

	@NotBlank
	private String	status;

	@OneToOne
	@JoinColumn(name = "visit_id")
	private Visit	visit;

}
