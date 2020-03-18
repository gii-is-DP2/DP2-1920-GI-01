
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.SimpleVisit;
import org.springframework.samples.petclinic.repository.SimpleVisitRepository;

public interface SpringSimpleVisitRepository extends SimpleVisitRepository, Repository<SimpleVisit, Integer> {

}
