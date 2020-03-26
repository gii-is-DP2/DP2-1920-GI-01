package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Medicine;
import org.springframework.samples.petclinic.model.PetType;

public interface MedicineRepository extends CrudRepository<Medicine, Integer>, JpaRepository<Medicine, Integer> {
	
	public Collection<Medicine> findByNameContaining(String name);
	
	@Query("SELECT m FROM Medicine m WHERE m.petType = ?1")
	public Collection<Medicine> findByPetType(PetType petType);

}
