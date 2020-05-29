
package org.springframework.samples.petclinic.service;

import java.util.Collection; 
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
	@Cacheable("petTypes")
	public Collection<PetType> findPetTypes() {
		return this.petRepository.findPetTypes();
	}

	@Transactional
	public void saveVisit(final Visit visit) {
		this.visitRepository.save(visit);
	}

	@Transactional(readOnly = true)
	public Pet findPetById(final int id) {
		return this.petRepository.findById(id);
	}

	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	@CacheEvict(cacheNames = "homelessPets", allEntries = true)
	public void savePet(final Pet pet) throws DuplicatedPetNameException {
		Pet otherPet;
		
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
	@Cacheable("homelessPets")
	public List<Pet> findHomelessPets() {
		return this.petRepository.findHomelessPets();
	}

	//This method allows us to delete a given pet
	public void deletePet(final Pet pet) {
		this.interventionRepository.findInterventionByPetId(pet.getId()).stream().forEach(i -> this.interventionRepository.delete(i));
		this.rehabRepository.findByPetId(pet.getId()).stream().forEach(r -> this.rehabRepository.delete(r));
		this.petRepository.delete(pet);
	}

	@Transactional
	public void saveIntervention(final Intervention intervention) {
		this.interventionRepository.save(intervention);
	}

	public Collection<Intervention> findInterventionsByPetId(final int petId) {
		return this.interventionRepository.findInterventionByPetId(petId);

	}

	@Transactional
	public void saveRehab(final Rehab rehab) {
		this.rehabRepository.save(rehab);
	}

	@Transactional
	public Optional<Intervention> findInterventionById(final int interventionId) {
		return this.interventionRepository.findById(interventionId);
	}

	public void deleteIntervention(final Intervention intervention) {
		this.interventionRepository.delete(intervention);

	}

	@Transactional
	public Optional<Vet> findVetByName(final String name) {
		return this.interventionRepository.findByUsername(name);
	}

}
