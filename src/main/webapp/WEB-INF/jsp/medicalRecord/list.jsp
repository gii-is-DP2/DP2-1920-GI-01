<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix ="c"  uri ="http://java.sun.com/jsp/jstl/core"%>

<petclinic:layout pageName="medical record">
	<h2>Find Medicines</h2>
	
	    <table id="medicalRecordsTable" class="table table-striped">
	        <thead>
	        <tr>
	        	<th>Name</th>
	            <th>Status</th>
	            <th>Visit Date</th>
	        </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${medicalRecords}" var="medicalRecord">
	            <tr>
	                <td>
	                    <spring:url value="/owners/*/pets/*/visits/{visitId}/medical-record/show?id={medicalRecordId}" var="medicalRecordURL">
	                        <spring:param name="medicalRecordId" value="${medicalRecord.id}"/>
	                         <spring:param name="visitId" value="${medicalRecord.visit.id}"/>
	                    </spring:url>
	                    <strong><a href="${fn:escapeXml(medicalRecordURL)}"><c:out value="${medicalRecord.name}"/></a></strong>
	                </td>
	                <td>
	                    <c:out value="${medicalRecord.status}"/>
	                </td>
	                <td>
	                    <c:out value="${medicalRecord.visit.date}"/>
	                </td>
	            </tr>
	        </c:forEach>
	        </tbody>
	    </table>
</petclinic:layout>