
package org.springframework.samples.petclinic.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.MedicalRecord;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.MedicalRecordService;
import org.springframework.samples.petclinic.service.PrescriptionService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

//		/owners/{ownerId}/pets/{petId}/visits/{visitId}/medical-record

public class MedicalRecordController {

	private static final String		SHOW_VIEW				= "medicalRecord/show";
	private static final String		CREATE_OR_UPDATE_VIEW	= "medicalRecord/form";
	private static final String		VIEW					= "/owners/*/pets/*/visits/{visitId}/medical-record";
	private static final String		MEDICAL_RECORD			= "medicalRecord";
	private static final String		PET_QUERY				= "/pets/";
	private static final String		REDIRECT_QUERY			= "redirect:/owners/";
	private static final String		NOT_FOUND_ERROR			= "MedicalRecord not found";

	@Autowired
	private VisitService			visitService;

	@Autowired
	private MedicalRecordService	medicalRecordService;

	@Autowired
	private PrescriptionService		prescriptionService;


	@Autowired
	public MedicalRecordController(final MedicalRecordService medicalRecordService, final VisitService visitService) {
		this.medicalRecordService = medicalRecordService;
		this.visitService = visitService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(MedicalRecordController.VIEW + "/new")
	public String initCreationForm(@PathVariable("visitId") final int visitId, final ModelMap model) {
		MedicalRecord medicalRecord;
		Visit visit;
		String result;

		medicalRecord = new MedicalRecord();
		visit = this.visitService.findVisitById(visitId).get();
		result = MedicalRecordController.CREATE_OR_UPDATE_VIEW;

		medicalRecord.setVisit(visit);
		model.put(MedicalRecordController.MEDICAL_RECORD, medicalRecord);
		model.put("visit", visit);

		return result;
	}

	@PostMapping(MedicalRecordController.VIEW + "/new")
	public String proccessCreationForm(@PathVariable("visitId") final int visitId, @Valid final MedicalRecord medicalRecord, final BindingResult result, final ModelMap model) {
		Integer medicalRecordId;
		String redirection;
		Visit visit;

		visit = this.visitService.findVisitById(visitId).get();
		medicalRecord.setVisit(visit);

		if (result.hasErrors()) {

			model.put(MedicalRecordController.MEDICAL_RECORD, medicalRecord);
			return MedicalRecordController.CREATE_OR_UPDATE_VIEW;
		}

		this.medicalRecordService.saveMedicalRecord(medicalRecord);

		medicalRecordId = medicalRecord.getId();
		String petId = medicalRecord.getVisit().getPet().getId().toString();
		String ownerId = medicalRecord.getVisit().getPet().getOwner().getId().toString();
		redirection = MedicalRecordController.REDIRECT_QUERY + ownerId + MedicalRecordController.PET_QUERY + petId + "/visits/" + visitId + "/medical-record/show?id=" + medicalRecordId;

		return redirection;
	}

	@GetMapping(MedicalRecordController.VIEW + "/show")
	public String showMedicalRecord(@RequestParam(value = "id", required = true) final Integer medicalRecordId, final ModelMap model) {
		MedicalRecord medicalRecord;
		Collection<Prescription> prescriptions;

		medicalRecord = this.medicalRecordService.findMedicalRecordById(medicalRecordId);
		prescriptions = this.prescriptionService.findManyByMedicalRecord(medicalRecord);

		if (medicalRecord == null) {
			throw new NullPointerException(MedicalRecordController.NOT_FOUND_ERROR);
		}

		model.put(MedicalRecordController.MEDICAL_RECORD, medicalRecord);
		model.put("prescriptions", prescriptions);

		return MedicalRecordController.SHOW_VIEW;
	}

	@GetMapping("/owners/*/pets/{petId}/medical-history")
	public String showVetList(@PathVariable("petId") final int petId, final MedicalRecord medicalRecord, final BindingResult result, final ModelMap model) {
		Collection<MedicalRecord> res = this.medicalRecordService.findMedicalRecordByPetId(petId);

		model.put(MedicalRecordController.MEDICAL_RECORD, res);

		return "medicalRecord/list";
	}

	@GetMapping(MedicalRecordController.VIEW + "/update")
	public String initUpdateForm(@RequestParam(value = "id", required = true) final Integer medicalRecordId, final ModelMap model) {
		MedicalRecord medicalRecord;

		medicalRecord = this.medicalRecordService.findMedicalRecordById(medicalRecordId);

		if (medicalRecord == null) {
			throw new NullPointerException(MedicalRecordController.NOT_FOUND_ERROR);
		}

		model.put(MedicalRecordController.MEDICAL_RECORD, medicalRecord);

		return MedicalRecordController.CREATE_OR_UPDATE_VIEW;
	}

	@PostMapping(MedicalRecordController.VIEW + "/update")
	public String proccessUpdateForm(@PathVariable("visitId") final int visitId, @RequestParam(value = "id", required = true) final Integer medicalRecordId, @Valid final MedicalRecord medicalRecord, final BindingResult result, final ModelMap model) {
		String redirection;
		MedicalRecord medicalRecordToUpdate;
		Visit visit;

		if (medicalRecord == null) {
			throw new NullPointerException(MedicalRecordController.NOT_FOUND_ERROR);
		}

		visit = this.visitService.findVisitById(visitId).get();
		medicalRecord.setVisit(visit);

		if (result.hasErrors()) {
			model.put(MedicalRecordController.MEDICAL_RECORD, medicalRecord);
			return MedicalRecordController.CREATE_OR_UPDATE_VIEW;
		}

		medicalRecordToUpdate = this.medicalRecordService.findMedicalRecordById(medicalRecordId);

		BeanUtils.copyProperties(medicalRecord, medicalRecordToUpdate, "id", "name");
		this.medicalRecordService.saveMedicalRecord(medicalRecordToUpdate);

		String petId = visit.getPet().getId().toString();
		String ownerId = visit.getPet().getOwner().getId().toString();
		redirection = MedicalRecordController.REDIRECT_QUERY + ownerId + MedicalRecordController.PET_QUERY + petId + "/visits/" + visitId + "/medical-record/show?id=" + medicalRecordId;

		return redirection;
	}

	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/*/medical-record/delete")
	public String deleteMedicalReport(@PathVariable("ownerId") final int ownerId, @PathVariable("petId") final int petId, @RequestParam(value = "id", required = true) final Integer medicalRecordId, final ModelMap model) {
		MedicalRecord medicalRecord;

		medicalRecord = this.medicalRecordService.findMedicalRecordById(medicalRecordId);

		if (medicalRecord == null) {
			throw new NullPointerException("Medicine not found");
		}

		this.prescriptionService.deleteAllAssociated(medicalRecord);
		this.medicalRecordService.deleteMedicalRecord(medicalRecord);

		return MedicalRecordController.REDIRECT_QUERY + ownerId + MedicalRecordController.PET_QUERY + petId + "/medical-history";
	}

}
