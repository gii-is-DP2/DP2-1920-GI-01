package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.repository.RehabRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RehabService {

	@Autowired
	private RehabRepository rehabRepository;
	
	@Transactional(readOnly = true)
	public Optional<Rehab> findRehabById(int rehabId) {
		return this.rehabRepository.findById(rehabId);
	}
	
	@Transactional
	public void saveRehab(Rehab rehab) {
		this.rehabRepository.save(rehab);
	}
	
	
	@Transactional
	public void delete(Rehab rehab) {
		this.rehabRepository.delete(rehab);
	}
}
