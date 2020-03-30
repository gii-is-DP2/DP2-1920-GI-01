
/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.InterventionRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.RehabRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class PetService {

	private PetRepository			petRepository;

	private VisitRepository			visitRepository;

	private RehabRepository			rehabRepository;

	private InterventionRepository	interventionRepository;


	@Autowired
	public PetService(final PetRepository petRepository, final VisitRepository visitRepository, final InterventionRepository interventionRepository, final RehabRepository rehabRepository) {
		this.petRepository = petRepository;
		this.visitRepository = visitRepository;
		this.interventionRepository = interventionRepository;
		this.rehabRepository = rehabRepository;

	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return this.petRepository.findPetTypes();
	}

	@Transactional
	public void saveVisit(final Visit visit) throws DataAccessException {
		this.visitRepository.save(visit);
	}

	@Transactional(readOnly = true)
	public Pet findPetById(final int id) throws DataAccessException {
		return this.petRepository.findById(id);
	}

	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	public void savePet(final Pet pet) throws DataAccessException, DuplicatedPetNameException {
		Pet otherPet = new Pet();
		
		if(pet.getOwner() != null) {
			otherPet = pet.getOwner().getPetwithIdDifferent(pet.getName(), pet.getId());
		} else {
			otherPet = null;
		}
		
		if (pet.getOwner() != null && StringUtils.hasLength(pet.getName()) && otherPet != null && otherPet.getId() != pet.getId()) {
			throw new DuplicatedPetNameException();
		} else {
			this.petRepository.save(pet);
		}
	}

	public Collection<Visit> findVisitsByPetId(final int petId) {
		return this.visitRepository.findByPetId(petId);
	}

	//This method allows us to find all homeless pets
	public List<Pet> findHomelessPets() throws DataAccessException {
		return this.petRepository.findHomelessPets();
	}

	//This method allows us to delete a given pet
	public void deletePet(final Pet pet) throws DataAccessException {
		this.interventionRepository.findInterventionByPetId(pet.getId()).stream().forEach(i -> this.interventionRepository.delete(i));
		this.rehabRepository.findByPetId(pet.getId()).stream().forEach(r -> this.rehabRepository.delete(r));
		this.petRepository.delete(pet);
	}

	@Transactional
	public void saveIntervention(final Intervention intervention) throws DataAccessException {
		this.interventionRepository.save(intervention);
	}

	public Collection<Intervention> findInterventionsByPetId(final int petId) {
		return this.interventionRepository.findInterventionByPetId(petId);

	}

	@Transactional
	public void saveRehab(final Rehab rehab) throws DataAccessException {
		this.rehabRepository.save(rehab);
	}

	@Transactional
	public Optional<Intervention> findInterventionById(final int interventionId) throws DataAccessException {
		// TODO Auto-generated method stub
		return this.interventionRepository.findById(interventionId);
	}

	public void deleteIntervention(final Intervention intervention) throws DataAccessException {
		// TODO Auto-generated method stub
		this.interventionRepository.delete(intervention);

	}

	@Transactional
	public Optional<Vet> findVetByName(final String name) {
		return this.interventionRepository.findByUsername(name);
	}

}
