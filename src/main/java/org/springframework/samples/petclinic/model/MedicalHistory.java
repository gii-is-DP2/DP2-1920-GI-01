
package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.txw2.annotation.XmlElement;

@XmlRootElement
public class MedicalHistory {

	private List<MedicalRecord> medicalRecords;


	@XmlElement
	public List<MedicalRecord> getMedicalRecordList() {
		if (this.medicalRecords == null) {
			this.medicalRecords = new ArrayList<>();
		}
		return this.medicalRecords;
	}
}
