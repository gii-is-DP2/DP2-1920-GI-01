
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.MedicalRecord;

public interface MedicalRecordRepository extends CrudRepository<MedicalRecord, Integer>, JpaRepository<MedicalRecord, Integer> {

	@Query("SELECT med FROM MedicalRecord med WHERE med.visit.pet.id =:id")
	Collection<MedicalRecord> findByPetId(@Param("id") int id);
}
