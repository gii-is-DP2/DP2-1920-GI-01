package org.springframework.samples.petclinic.repository;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Trainer;

public interface TrainerRepository extends CrudRepository<Trainer, Integer>{

	@Query("SELECT t FROM Trainer t WHERE t.user.username = :username")
	Optional<Trainer> findByUsername(String username) throws DataAccessException;
	
}
