
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "prescriptions")
public class Prescription extends NamedEntity {

	@ManyToOne
	@JoinColumn(name = "medical_record_id")
	private MedicalRecord	medicalRecord;

	@ManyToOne
	@JoinColumn(name = "medicine_id")
	private Medicine		medicine;

	@NotBlank
	private String			dose;

}
