
package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.SimpleVisit;

public interface SimpleVisitRepository {

	void save(SimpleVisit visit) throws DataAccessException;

	List<SimpleVisit> findByPetId(Integer petId);
}
