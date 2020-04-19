package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Adoption;

public interface AdoptionRepository extends CrudRepository<Adoption, Integer>, JpaRepository<Adoption, Integer> {
	
	Collection<Adoption> findByPetId(Integer petId);

}
