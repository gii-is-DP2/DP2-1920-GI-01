<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="medical-record">
	<h2>Medical Record</h2>
	
	<c:if test="${message != 'There was no medical record found for this visit.'}">
		<table class="table table-striped">
			<tr>
				<th>Description</th>
				<td><b><c:out value="${medicalRecord.description}"/></b></td>
			</tr>
			<tr>
				<th>Status</th>
				<td><c:out value="${medicalRecord.status}"/></td>
			</tr>
			<tr>
				<th>Visit date</th>
				<td><b><c:out value="${medicalRecord.visit.date}"/></b></td>
			</tr>
			<tr>
				<th>Visit description</th>
				<td><b><c:out value="${medicalRecord.visit.description}"/></b></td>
			</tr>
			<tr>
				<th>Pet</th>
				<td><b><c:out value="${medicalRecord.visit.pet.name}"/></b></td>
			</tr>
		</table>
		
		<sec:authorize access="hasAuthority('veterinarian')">
		   	<spring:url value="/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/edit" var="editURL">
		        <spring:param name="petId" value="${medicalRecord.visit.pet.id}"/>
				<spring:param name="visitId" value="${medicalRecord.visit.id}"/>
				<spring:param name="medicalRecordId" value="${medicalRecord.id}"/>
		    </spring:url>
		    <a href="${fn:escapeXml(editURL)}" class="btn btn-default">Edit</a>
		    
		   	<spring:url value="/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/delete" var="deleteURL">
		    	<spring:param name="petId" value="${medicalRecord.visit.pet.id}"/>
				<spring:param name="visitId" value="${medicalRecord.visit.id}"/>
				<spring:param name="medicalRecordId" value="${medicalRecord.id}"/>
		    </spring:url>
		    <a href="${fn:escapeXml(deleteURL)}" class="btn btn-default">Delete</a>
		</sec:authorize>
	    
	    <br/>
	    <br/>
	    <br/>
	    
	    <h3>Prescriptions</h3>
	    
	    <table id="prescriptionsTable" class="table table-striped">
	        <thead>
	        <tr>
	            <th>Name</th>
	            <th style="width: 70%;">Dose</th>
	        </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${prescriptions}" var="prescription">
	            <tr>
	                <td>
	                    <spring:url value="/medicine/show?id={medicineId}" var="medicineURL">
	                        <spring:param name="medicineId" value="${prescription.medicine.id}"/>
	                    </spring:url>
	                    <strong><a href="${fn:escapeXml(medicineURL)}"><c:out value="${prescription.medicine.name}"/></a></strong>
	                </td>
	                <td>
	                    <c:out value="${prescription.dose}"/>
	                </td>
	            </tr>
	        </c:forEach>
	        </tbody>
	    </table>
	    
	    <sec:authorize access="hasAuthority('veterinarian')">
		    <spring:url value="/homeless-pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/prescriptions/new" var="prescriptionUrl">
				<spring:param name="petId" value="${medicalRecord.visit.pet.id}"/>
				<spring:param name="visitId" value="${medicalRecord.visit.id}"/>
				<spring:param name="medicalRecordId" value="${medicalRecord.id}"/>
		    </spring:url>
			<a href="${fn:escapeXml(prescriptionUrl)}">Add prescription</a>
		</sec:authorize>
	</c:if>
	<c:if test="${message == 'There was no medical record found for this visit.'}">
		<h3><c:out value="${message}"></c:out></h3><br/>
		<sec:authorize access="hasAuthority('veterinarian')">
		    <spring:url value="/homeless-pets/{petId}/visits/{visitId}/medical-record/new" var="medicalRecordNewUrl">
				<spring:param name="petId" value="${petId}"/>
				<spring:param name="visitId" value="${visitId}"/>
		    </spring:url>
			<a href="${fn:escapeXml(medicalRecordNewUrl)}">Add Medical Record</a>
		</sec:authorize>
	</c:if>
	
</petclinic:layout>