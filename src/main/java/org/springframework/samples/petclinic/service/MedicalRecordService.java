
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicalRecordService {

	@Autowired
	private MedicalRecordRepository repository;


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
	/*
	 * @Transactional
	 * public void deleteMedicalRecord(final MedicalRecord medicalRecord) {
	 * this.repository.delete(medicalRecord);
	 * }
	 */

	@Transactional(readOnly = true)
	public Collection<MedicalRecord> findMedicalHistory() throws DataAccessException {
		return this.repository.findAll();
	}
}
