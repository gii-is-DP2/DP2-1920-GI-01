
package org.springframework.samples.petclinic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Intervention;

public interface InterventionRepository {

	void save(Intervention intervention) throws DataAccessException;

	List<Intervention> findInterventionByPetId(Integer petId);

	Optional<Intervention> findById(int id) throws DataAccessException;
}
