<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
    <h2>Veterinarians</h2>
    
    <c:if test="${message != null}"><h3><c:out value="${message}"></c:out></h3></c:if>

	<c:if test="${message == null}">
	    <table id="vetsTable" class="table table-striped">
	        <thead>
	        <tr>
	            <th>Name</th>
	            <th>Specialties</th>
	        </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${vets.vetList}" var="vet">
	            <tr>
	                <td>
	                	<spring:url value="/admin/vets/{vetId}" var="vetUrl">
	                        <spring:param name="vetId" value="${vet.id}"/>
	                    </spring:url>
	                	<a href="${fn:escapeXml(vetUrl)}"><c:out value="${vet.firstName} ${vet.lastName}"/></a>
	                </td>
	                <td>
	                    <c:forEach var="specialty" items="${vet.specialties}">
	                        <c:out value="${specialty.name} "/>
	                    </c:forEach>
	                    <c:if test="${vet.nrOfSpecialties == 0}">none</c:if>
	                </td>
	            </tr>
	        </c:forEach>
	        </tbody>
	    </table>
	</c:if>
    
    <spring:url value="/admin/vets/new" var="newVetUrl">
    </spring:url>
    <a href="${fn:escapeXml(newVetUrl)}">Add a Vet</a>

    <table class="table-buttons">
        <tr>
            <td>
                <a href="<spring:url value="/vets.xml" htmlEscape="true" />">View as XML</a>
            </td>            
        </tr>
    </table>
</petclinic:layout>
