
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.transaction.annotation.Transactional;

public interface InterventionRepository {

	void save(Intervention intervention);

	List<Intervention> findByPetId(Integer interventionId);

	@Transactional
	@Modifying
	@Query("DELETE FROM Intervention i where i.id=:interventionId")
	void delete(@Param(value = "interventionId") int interventionId);

	@Query("SELECT i FROM Intervention i where i.id=:interventionId")
	Intervention findByInterventionId(@Param(value = "interventionId") int interventionId);

	@Query("SELECT p.interventions FROM Pet p where p.id=:petId")
	Collection<Intervention> findInterventionsByPetId(@Param(value = "petId") int petId);

	@Transactional
	@Modifying
	@Query("DELETE FROM Intervention i where i.pet.id=:petId")
	void deleteAllInterventionsByPetId(@Param(value = "petId") int petId);

}
