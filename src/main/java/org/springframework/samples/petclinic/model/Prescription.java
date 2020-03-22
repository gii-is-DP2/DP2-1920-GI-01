
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Prescription extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "medical_record_id")
	@NotNull
	private MedicalRecord	medicalRecord;

	@ManyToOne
	@JoinColumn(name = "medicine_id")
	@NotNull
	private Medicine		medicine;

	@NotBlank
	private String			dose;

}
