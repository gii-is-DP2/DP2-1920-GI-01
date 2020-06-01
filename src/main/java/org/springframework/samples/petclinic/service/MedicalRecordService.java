
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.repository.MedicalRecordRepository;
import org.springframework.samples.petclinic.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicalRecordService {

	@Autowired
	private MedicalRecordRepository	repository;

	@Autowired
	private PrescriptionRepository	prescriptionRepository;


	@CacheEvict(cacheNames = "medicalHistory", allEntries = true)
	@Transactional
	public void saveMedicalRecord(final MedicalRecord medicalRecord) {
		this.repository.save(medicalRecord);
	}

	@Transactional(readOnly = true)
	public MedicalRecord findMedicalRecordById(final Integer id) {
		return this.repository.findById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	public Collection<MedicalRecord> findMedicalRecordByPetId(final Integer id) {
		return this.repository.findByPetId(id);
	}

	@Transactional(readOnly = true)
	public MedicalRecord findMedicalRecordByVisitId(final Integer visitId) {
		return this.repository.findOneByVisitId(visitId);
	}

	@CacheEvict(cacheNames = "medicalHistory", allEntries = true)
	@Transactional
	public void deleteMedicalRecord(final MedicalRecord medicalRecord) {
		this.prescriptionRepository.deleteAllAssociated(medicalRecord);
		this.repository.delete(medicalRecord);
	}

	@Cacheable("medicalHistory")
	@Transactional(readOnly = true)
	public Collection<MedicalRecord> findMedicalHistory() {
		return this.repository.findAll();
	}
}
