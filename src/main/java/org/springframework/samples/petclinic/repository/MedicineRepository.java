package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Medicine;

public interface MedicineRepository extends CrudRepository<Medicine, Integer>, JpaRepository<Medicine, Integer> {
	
	//@Query("SELECT m FROM Medicine m WHERE ")
	public Collection<Medicine> findByNameContaining(String name);

}
