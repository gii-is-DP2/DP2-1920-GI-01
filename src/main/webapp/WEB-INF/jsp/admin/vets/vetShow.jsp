<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">

    <h2>Vet Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${vet.firstName} ${vet.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Specialties</th>
            <td>
            	<c:forEach var="specialty" items="${vet.specialties}">
                	<c:out value="${specialty.name} "/>
                </c:forEach>
                <c:if test="${vet.nrOfSpecialties == 0}">none</c:if>
            </td>
        </tr>
    </table>

    <spring:url value="/admin/vets/{vetId}/edit" var="vetUrl">
    	<spring:param name="vetId" value="${vet.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(vetUrl)}">Edit</a>
    <spring:url value="/admin/vets/{vetId}/delete" var="vetUrl2">
        <spring:param name="vetId" value="${vet.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(vetUrl2)}">Delete</a>

</petclinic:layout>
