
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

	private static final String			SHOW_VIEW				= "medicalRecord/show";
	private static final String			CREATE_OR_UPDATE_VIEW	= "medicalRecord/form";
	
	@Autowired
	private VisitService			visitService;
	
	@Autowired
	private MedicalRecordService	medicalRecordService;

	@Autowired
	private PrescriptionService 	prescriptionService;

	@Autowired
	public MedicalRecordController(final MedicalRecordService medicalRecordService, final VisitService visitService) {
		this.medicalRecordService = medicalRecordService;
		this.visitService = visitService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/owners/*/pets/*/visits/{visitId}/medical-record/new")
	public String initCreationForm(@PathVariable("visitId") final int visitId, final ModelMap model) {
		MedicalRecord medicalRecord;
		Visit visit;
		String result;
		
		medicalRecord = new MedicalRecord();
		visit = this.visitService.findVisitById(visitId).get();
		result = MedicalRecordController.CREATE_OR_UPDATE_VIEW;
		
		medicalRecord.setVisit(visit);
		model.put("medicalRecord", medicalRecord);

		return result;
	}

	@PostMapping("/owners/*/pets/*/visits/{visitId}/medical-record/new")
	public String proccessCreationForm(@PathVariable("visitId") final int visitId, @Valid final MedicalRecord medicalRecord, final BindingResult result, final ModelMap model) {
		Integer medicalRecordId;
		String redirection;
		Visit visit;

		if (result.hasErrors()) {

			model.put("medicalRecord", medicalRecord);
			return MedicalRecordController.CREATE_OR_UPDATE_VIEW;
		}

		visit = this.visitService.findVisitById(visitId).get();
		medicalRecord.setVisit(visit);
		this.medicalRecordService.saveMedicalRecord(medicalRecord);

		medicalRecordId = medicalRecord.getId();
		String petId = medicalRecord.getVisit().getPet().getId().toString();
		String ownerId = medicalRecord.getVisit().getPet().getOwner().getId().toString();
		redirection = "redirect:/owners/" + ownerId + "/pets/" + petId + "/visits/" + visitId + "/medical-record/show?id=" + medicalRecordId;

		return redirection;
	}

	@GetMapping("/owners/*/pets/*/visits/{visitId}/medical-record/show")
	public String showMedicalRecord(@RequestParam(value = "id", required = true) final Integer medicalRecordId, final ModelMap model) {
		MedicalRecord medicalRecord;
		Collection<Prescription> prescriptions;

		medicalRecord = this.medicalRecordService.findMedicalRecordById(medicalRecordId);
		prescriptions = prescriptionService.findManyByMedicalRecord(medicalRecord);

		if (medicalRecord == null) {
			throw new NullPointerException("MedicalRecord not found");
		}

		model.put("medicalRecord", medicalRecord);
		model.put("prescriptions", prescriptions);

		return MedicalRecordController.SHOW_VIEW;
	}

	@GetMapping("/owners/*/pets/{petId}/medical-history")
	public String showVetList(@PathVariable("petId") final int petId, final MedicalRecord medicalRecord, final BindingResult result, final ModelMap model) {
		Collection<MedicalRecord> res = this.medicalRecordService.findMedicalRecordByPetId(petId);
		
		model.put("medicalRecords", res);
		
		return "medicalRecord/list";
	}

	
	@GetMapping("/owners/*/pets/*/visits/{visitId}/medical-record/update")
	public String initUpdateForm(@RequestParam(value = "id", required = true) Integer medicalRecordId, ModelMap model) {
		MedicalRecord medicalRecord;
		
		medicalRecord = medicalRecordService.findMedicalRecordById(medicalRecordId);
		
		if(medicalRecord == null) {
			throw new NullPointerException("MedicalRecord not found");
		}
		
		model.put("medicalRecord", medicalRecord);
		
		return CREATE_OR_UPDATE_VIEW;
	}
		 
	@PostMapping("/owners/*/pets/*/visits/{visitId}/medical-record/update")
	public String proccessUpdateForm(@PathVariable("visitId") final int visitId, @RequestParam(value = "id", required = true) Integer medicalRecordId, @Valid MedicalRecord medicalRecord, BindingResult result, ModelMap model) {
		String redirection;
		MedicalRecord medicalRecordToUpdate;
		Visit visit;
		
		if(medicalRecord == null) {
			throw new NullPointerException("MedicalRecord not found");
		}
		
		visit = visitService.findVisitById(visitId).get();
		medicalRecord.setVisit(visit);
		
		if(result.hasErrors()) {
			model.put("medicalRecord", medicalRecord);
			return CREATE_OR_UPDATE_VIEW;
		}
		
		medicalRecordToUpdate = medicalRecordService.findMedicalRecordById(medicalRecordId);
		
		BeanUtils.copyProperties(medicalRecord, medicalRecordToUpdate, "id", "name");
		medicalRecordService.saveMedicalRecord(medicalRecordToUpdate);
		
		String petId = visit.getPet().getId().toString();
		String ownerId = visit.getPet().getOwner().getId().toString();
		redirection = "redirect:/owners/" + ownerId + "/pets/" + petId + "/visits/" + visitId + "/medical-record/show?id=" + medicalRecordId;
		
		return redirection;
	}
	
	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/*/medical-record/delete")
	public String deleteMedicalReport(@PathVariable("ownerId") final int ownerId, @PathVariable("petId") final int petId, @RequestParam(value = "id", required = true) Integer medicalRecordId, ModelMap model) {
		MedicalRecord medicalRecord;
		
		medicalRecord = medicalRecordService.findMedicalRecordById(medicalRecordId);
		
		if(medicalRecord == null) {
			throw new NullPointerException("Medicine not found");
		}
		
		prescriptionService.deleteAllAssociated(medicalRecord);
		medicalRecordService.deleteMedicalRecord(medicalRecord);
		
		return "redirect:/owners/" + ownerId + "/pets/" + petId + "/medical-history";
	}

}
