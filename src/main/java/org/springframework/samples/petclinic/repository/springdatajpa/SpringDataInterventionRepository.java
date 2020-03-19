
package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.repository.InterventionRepository;

public interface SpringDataInterventionRepository extends InterventionRepository, Repository<Intervention, Integer> {

}
