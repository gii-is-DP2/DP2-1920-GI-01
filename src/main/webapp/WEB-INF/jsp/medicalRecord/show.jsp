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
	
   	<spring:url value="update?id={medicineId}" var="editURL">
        <spring:param name="medicalRecordId" value="${medicalRecord.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editURL)}" class="btn btn-default">Edit</a>
    
   	<spring:url value="delete?id={medicineId}" var="deleteURL">
        <spring:param name="medicalRecordId" value="${medicalRecord.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(deleteURL)}" class="btn btn-default">Delete</a>
	
</petclinic:layout>