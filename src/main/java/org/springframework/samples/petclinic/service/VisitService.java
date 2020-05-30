
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.MedicalRecordRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitService {

	@Autowired
	private VisitRepository			visitRepository;

	@Autowired
	private MedicalRecordRepository	medicalRepository;


	@Transactional(readOnly = true)
	public Optional<Visit> findVisitById(final int visitId) throws DataAccessException {
		return this.visitRepository.findById(visitId);
	}

	@Transactional
	public void delete(final Visit visit) throws DataAccessException {

		MedicalRecord mr = this.medicalRepository.findOneByVisitId(visit.getId());
		if (mr != null) {
			this.visitRepository.delete(visit);
			this.medicalRepository.delete(mr);
		} else {
			this.visitRepository.delete(visit);
		}

	}

}
