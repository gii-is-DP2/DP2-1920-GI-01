<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="vets">

    <h2>Homeless Pets</h2>
    
    <c:if test="${message != null}"><h3><c:out value="${message}"></c:out></h3></c:if>

    <table class="table table-striped">
		<thead>
	        <tr>
	        	<th style="width: 300px;">Name</th>
	            <th>Birth Date</th>
	            <th>Type</th>
	            <sec:authorize access="hasAuthority('veterinarian')">
	            	<th>Actions</th>
	            </sec:authorize>
	        </tr>
	    </thead>
	    <tbody>
	        <c:forEach var="pet" items="${homelessPets}">
	        	<tr>
                	<td>
                		<spring:url value="/homeless-pets/{petId}" var="petUrl">
                        	<spring:param name="petId" value="${pet.id}"/>
                        </spring:url>
                    	<a href="${fn:escapeXml(petUrl)}"><c:out value="${pet.name}"/></a>
                	</td>
                	<td>
                   		<petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/>
                	</td>
                	<td>
                    	<c:out value="${pet.type.name}"/>
                	</td>
                	<sec:authorize access="hasAuthority('veterinarian')">
	                	<td>
	                		<spring:url value="/homeless-pets/{petId}/edit" var="petEditUrl">
	                        	<spring:param name="petId" value="${pet.id}"/>
	                        </spring:url>
	                    	<a href="${fn:escapeXml(petEditUrl)}">Edit Pet</a><br/>
	                    	<spring:url value="/homeless-pets/{petId}/delete" var="petDeleteUrl">
	                        	<spring:param name="petId" value="${pet.id}"/>
	                        </spring:url>
	                    	<a href="${fn:escapeXml(petDeleteUrl)}">Delete Pet</a>
	                	</td>
	                </sec:authorize>
            	</tr>
	        </c:forEach>
	    </tbody>
	</table>
	
	<sec:authorize access="hasAuthority('veterinarian')">
	    <spring:url value="/homeless-pets/new" var="petUrl2">
	    </spring:url>
	    <a href="${fn:escapeXml(petUrl2)}">Add a Pet</a>
	</sec:authorize>

</petclinic:layout>
