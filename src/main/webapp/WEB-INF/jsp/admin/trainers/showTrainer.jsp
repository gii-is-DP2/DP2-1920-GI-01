<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="trainers">

    <h2>Trainer Information</h2>

    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${trainer.name} ${trainer.surname}"/></b></td>
        </tr>
        <tr>
            <th>Email</th>
            <td><c:out value="${trainer.email}"/></td>
        </tr>
        <tr>
            <th>Phone</th>
            <td><c:out value="${trainer.phone}"/></td>
        </tr>
    </table>
    
    <spring:url value="/admin/trainers/{trainerId}/edit" var="trainerUrl">
    	<spring:param name="trainerId" value="${trainer.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(trainerUrl)}">Edit</a>
    <spring:url value="/admin/trainers/{trainerId}/delete" var="trainerUrl2">
        <spring:param name="trainerId" value="${trainer.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(trainerUrl2)}">Delete</a>

</petclinic:layout>
