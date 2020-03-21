package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrescriptionService {
	
	@Autowired
	private PrescriptionRepository prescriptionRepository;
	
	@Transactional
	public void savePrescription(Prescription prescription) {
		prescriptionRepository.save(prescription);
	}
	
	@Transactional(readOnly = true)
	public Collection<Prescription> findManyByMedicalRecord(MedicalRecord medicalRecord) {
		return prescriptionRepository.findByMedicalRecord(medicalRecord);
	}
	
	@Transactional
	public void deleteAllAssociated(MedicalRecord medicalRecord) {
		prescriptionRepository.deleteAllAssociated(medicalRecord);
	}

}
