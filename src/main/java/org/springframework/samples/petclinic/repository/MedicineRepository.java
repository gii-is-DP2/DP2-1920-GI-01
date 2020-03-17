package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Medicine;

public interface MedicineRepository extends CrudRepository<Medicine, Integer>{

}
