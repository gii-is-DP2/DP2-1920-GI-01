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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class VetService {

	private VetRepository		vetRepository;

	@Autowired
	private UserService			userService;

	@Autowired
	private AuthoritiesService	authoritiesService;


	@Autowired
	public VetService(final VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	@Cacheable("vets")
	@Transactional(readOnly = true)
	public Collection<Vet> findVets() throws DataAccessException {
		return this.vetRepository.findAll();
	}

	@Cacheable("specialties")
	@Transactional(readOnly = true)
	public Collection<Specialty> findAllSpecialty() throws DataAccessException {
		return this.vetRepository.findAllSpecialty();
	}

	@Transactional
	public Optional<Vet> findVetById(final int vetId) {
		Optional<Vet> res;
		res = this.vetRepository.findById(vetId);
		return res;
	}

	@Transactional
	public Optional<Vet> findVetByUsername(final String username) {
		Optional<Vet> res;
		res = this.vetRepository.findByUsername(username);
		return res;
	}

	@CacheEvict(cacheNames = "vets", allEntries = true)
	@Transactional
	public void deleteVet(final Vet vet) {
		this.vetRepository.delete(vet);
	}

	@CacheEvict(cacheNames = "vets", allEntries = true)
	@Transactional
	public void saveVet(final Vet vet) throws DataAccessException {
		this.vetRepository.save(vet);
		this.userService.saveUser(vet.getUser());
		this.authoritiesService.saveAuthorities(vet.getUser().getUsername(), "veterinarian");
	}

}
