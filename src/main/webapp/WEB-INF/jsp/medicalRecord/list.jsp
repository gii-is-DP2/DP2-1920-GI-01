<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix ="c"  uri ="http://java.sun.com/jsp/jstl/core"%>

<petclinic:layout pageName="medical record">
	<h2>Medical Records</h2>
	
	    <table id="medicalRecordsTable" class="table table-striped">
	        <thead>
	        <tr>
	        	<th>Visit Date</th>
	            <th>Status</th>
	        </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${medicalRecords}" var="medicalRecord">
	            <tr>
	                <td>
	                    <spring:url value="/owners/{ownerId}/pets/{petId}/visits/{visitId}/medical-record/show?id={medicalRecordId}" var="medicalRecordURL">
	                    	<spring:param name="ownerId" value="${medicalRecord.visit.pet.owner.id}"/>
	                    	<spring:param name="petId" value="${medicalRecord.visit.pet.id}"/>
	                        <spring:param name="medicalRecordId" value="${medicalRecord.id}"/>
	                        <spring:param name="visitId" value="${medicalRecord.visit.id}"/>
	                    </spring:url>
	                    <strong><a href="${fn:escapeXml(medicalRecordURL)}"><c:out value="${medicalRecord.visit.date}"/></a></strong>
	                </td>
	                <td>
	                    <c:out value="${medicalRecord.status}"/>
	                </td>
	            </tr>
	        </c:forEach>
	        </tbody>
	    </table>
</petclinic:layout>