<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="medical-record">
	<h2>Medical Record</h2>
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
	
   	<spring:url value="update?id={medicalRecordId}" var="editURL">
        <spring:param name="medicalRecordId" value="${medicalRecord.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editURL)}" class="btn btn-default">Edit</a>
    
   	<spring:url value="delete?id={medicalRecordId}" var="deleteURL">
        <spring:param name="medicalRecordId" value="${medicalRecord.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(deleteURL)}" class="btn btn-default">Delete</a>
    
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
    
    <spring:url value="/owners/{ownerId}/pets/{petId}/visits/{visitId}/medical-record/{medicalRecordId}/prescription/create" var="prescriptionUrl">
		<spring:param name="ownerId" value="${medicalRecord.visit.pet.owner.id}"/>
		<spring:param name="petId" value="${medicalRecord.visit.pet.id}"/>
		<spring:param name="visitId" value="${medicalRecord.visit.id}"/>
		<spring:param name="medicalRecordId" value="${medicalRecord.id}"/>
    </spring:url>
	<a href="${fn:escapeXml(prescriptionUrl)}">Add prescription</a>
	
</petclinic:layout>