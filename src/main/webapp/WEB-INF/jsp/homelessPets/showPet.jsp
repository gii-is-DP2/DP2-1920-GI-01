<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="home">

    <h1>Information about the <c:out value="${homelessPet.type.name}"/>, <c:out value="${homelessPet.name}"/>, born on <petclinic:localDate date="${homelessPet.birthDate}" pattern="yyyy-MM-dd"/></h1>
    
    <h2>Its visits</h2>
    
    <table class="table table-striped">
    	<thead>
	        <tr>
	        	<th style="width: 200px;">Date</th>
	            <th style="width: 500px;">Description</th>
	            <th style="width: 200px;">Actions</th>
	        </tr>
	    </thead>
	    <tbody>
	        <c:forEach var="visit" items="${homelessPet.visits}">
	        	<tr>
                	<td>
                		<petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/>
                	</td>
                	<td>
                   		<c:out value="${visit.description}"/>
                	</td>
                	<td>
                		<sec:authorize access="hasAuthority('veterinarian')">
	                		<spring:url value="/homeless-pets/{petId}/visits/{visitId}/edit" var="visitEditUrl">
	                        	<spring:param name="petId" value="${homelessPet.id}"/>
	                        	<spring:param name="visitId" value="${visit.id}"/>
	                        </spring:url>
	                    	<a href="${fn:escapeXml(visitEditUrl)}">Edit Visit</a><br/>
	                    	<spring:url value="/homeless-pets/{petId}/visits/{visitId}/delete" var="visitDeleteUrl">
	                        	<spring:param name="petId" value="${homelessPet.id}"/>
	                        	<spring:param name="visitId" value="${visit.id}"/>
	                        </spring:url>
	                    	<a href="${fn:escapeXml(visitDeleteUrl)}">Delete Visit</a><br/>
	                    </sec:authorize>
                    	<spring:url value="/homeless-pets/{petId}/visits/{visitId}/medical-record" var="medicalRecordShowUrl">
                        	<spring:param name="petId" value="${homelessPet.id}"/>
                        	<spring:param name="visitId" value="${visit.id}"/>
                        </spring:url>
                    	<a href="${fn:escapeXml(medicalRecordShowUrl)}">Show Medical Record</a>
                	</td>
            	</tr>
			</c:forEach>
	    </tbody>
    </table>
    
    <sec:authorize access="hasAuthority('veterinarian')">
	    <spring:url value="/homeless-pets/{petId}/visits/new" var="visitCreateUrl">
	    	<spring:param name="petId" value="${homelessPet.id}"/>
		</spring:url>
		<a href="${fn:escapeXml(visitCreateUrl)}">Add Visit</a>
	</sec:authorize>
    
    <br/>
    <br/>
    <h2>Its interventions</h2>
    
    <table class="table table-striped">
    	<thead>
	        <tr>
	        	<th style="width: 200px;">Date</th>
	        	<th style="width: 200px;">Duration (in hours)</th>
	            <th style="width: 300px;">Description</th>
	            <th style="width: 200px;">Actions</th>
	        </tr>
	    </thead>
	    <tbody>
	        <c:forEach var="intervention" items="${homelessPet.interventions}">
	        	<tr>
                	<td>
                		<petclinic:localDate date="${intervention.interventionDate}" pattern="yyyy-MM-dd"/>
                	</td>
                	<td>
                   		<c:out value="${intervention.interventionTime}"/>
                	</td>
                	<td>
                   		<c:out value="${intervention.interventionDescription}"/>
                	</td>
                	<td>
                		<sec:authorize access="hasAuthority('veterinarian')">
	                		<spring:url value="/homeless-pets/{petId}/interventions/{interventionId}/edit" var="interventionEditUrl">
	                        	<spring:param name="petId" value="${homelessPet.id}"/>
	                        	<spring:param name="interventionId" value="${intervention.id}"/>
	                        </spring:url>
	                    	<a href="${fn:escapeXml(interventionEditUrl)}">Edit Intervention</a><br/>
	                    	<spring:url value="/homeless-pets/{petId}/interventions/{interventionId}/delete" var="interventionDeleteUrl">
	                        	<spring:param name="petId" value="${homelessPet.id}"/>
	                        	<spring:param name="interventionId" value="${intervention.id}"/>
	                        </spring:url>
	                    	<a href="${fn:escapeXml(interventionDeleteUrl)}">Delete Intervention</a>
	                    </sec:authorize>
                	</td>
            	</tr>
			</c:forEach>
	    </tbody>
    </table>
    
    <sec:authorize access="hasAuthority('veterinarian')">
	    <spring:url value="/homeless-pets/{petId}/interventions/new" var="interventionCreateUrl">
	    	<spring:param name="petId" value="${homelessPet.id}"/>
		</spring:url>
		<a href="${fn:escapeXml(interventionCreateUrl)}">Add Intervention</a>
	</sec:authorize>

    <br/>
    <br/>
    <h2>Its rehab sessions</h2>
    
    <table class="table table-striped">
    	<thead>
	        <tr>
	        	<th style="width: 200px;">Date</th>
	        	<th style="width: 200px;">Duration (in hours)</th>
	            <th style="width: 300px;">Description</th>
	           	<th style="width: 200px;">Actions</th>
	        </tr>
	    </thead>
	    <tbody>
	        <c:forEach var="rehab" items="${homelessPet.rehabs}">
	        	<tr>
                	<td>
                		<petclinic:localDate date="${rehab.date}" pattern="yyyy-MM-dd"/>
                	</td>
                	<td>
                   		<c:out value="${rehab.time}"/>
                	</td>
                	<td>
                   		<c:out value="${rehab.description}"/>
                	</td>
                	<td>
                		<sec:authorize access="hasAuthority('trainer')">
	                		<spring:url value="/homeless-pets/{petId}/rehabs/{rehabId}/edit" var="rehabEditUrl">
	                        	<spring:param name="petId" value="${homelessPet.id}"/>
	                        	<spring:param name="rehabId" value="${rehab.id}"/>
	                        </spring:url>
	                    	<a href="${fn:escapeXml(rehabEditUrl)}">Edit Rehab</a><br/>
	                    	<spring:url value="/homeless-pets/{petId}/rehabs/{rehabId}/delete" var="rehabDeleteUrl">
	                        	<spring:param name="petId" value="${homelessPet.id}"/>
	                        	<spring:param name="rehabId" value="${rehab.id}"/>
	                        </spring:url>
	                    	<a href="${fn:escapeXml(rehabDeleteUrl)}">Delete Rehab</a>
	                    </sec:authorize>
                	</td>
            	</tr>
			</c:forEach>
	    </tbody>
    </table>
    
    <sec:authorize access="hasAuthority('trainer')">
	    <spring:url value="/homeless-pets/{petId}/rehabs/new" var="rehabCreateUrl">
	    	<spring:param name="petId" value="${homelessPet.id}"/>
		</spring:url>
		<a href="${fn:escapeXml(rehabCreateUrl)}">Add Rehab</a>
	</sec:authorize>
    
</petclinic:layout>