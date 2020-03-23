package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Prescription;

public interface PrescriptionRepository extends CrudRepository<Prescription, Integer> {
	
	public Collection<Prescription> findByMedicalRecord(MedicalRecord medicalRecord);
	
	@Modifying
	@Query("DELETE FROM Prescription p WHERE p.medicalRecord = ?1")
	public void deleteAllAssociated(MedicalRecord medicalRecord);

}
