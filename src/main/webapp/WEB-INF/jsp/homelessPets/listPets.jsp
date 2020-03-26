<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">

    <h2>Homeless Pets and their Visits</h2>
    
    <c:if test="${message != null}"><h3><c:out value="${message}"></c:out></h3></c:if>

    <table class="table table-striped">
        <c:forEach var="pet" items="${homelessPets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Visit Date</th>
                            <th>Description</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                                <td>
                                	<spring:url value="/homeless-pets/{petId}/visits/{visitId}/edit" var="visitEditUrl">
                                    	<spring:param name="petId" value="${pet.id}"/>
                                    	<spring:param name="visitId" value="${visit.id}"/>
                                	</spring:url>
                                	<a href="${fn:escapeXml(visitEditUrl)}">Edit</a>
                                	<spring:url value="/homeless-pets/{petId}/visits/{visitId}/delete" var="visitDeleteUrl">
                                    	<spring:param name="petId" value="${pet.id}"/>
                                    	<spring:param name="visitId" value="${visit.id}"/>
                                	</spring:url>
                                	<a href="${fn:escapeXml(visitDeleteUrl)}">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <tr>
                        	<td>
                        		<spring:url value="/homeless-pets/{petId}/delete" var="petDeleteUrl">
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petDeleteUrl)}">Delete Pet</a>
                        	</td>
                            <td>
                                <spring:url value="/homeless-pets/{petId}/edit" var="petUrl">
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Edit Pet</a>
                            </td>
                            <td>
                                <spring:url value="/homeless-pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Add Visit</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>
    
    <spring:url value="/homeless-pets/new" var="petUrl2">
    </spring:url>
    <a href="${fn:escapeXml(petUrl2)}">Add a Pet</a>

</petclinic:layout>
