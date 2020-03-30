
package org.springframework.samples.petclinic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Vet;

public interface InterventionRepository {

	void delete(Intervention intervention) throws DataAccessException;
	
	void save(Intervention intervention) throws DataAccessException;

	List<Intervention> findInterventionByPetId(Integer petId);

	Optional<Intervention> findById(int id) throws DataAccessException;

	@Query("SELECT vet FROM Vet vet WHERE vet.user.username=:username")
	Optional<Vet> findByUsername(String username);
}
