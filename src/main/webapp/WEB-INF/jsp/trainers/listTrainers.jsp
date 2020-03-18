<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="events">
    <h2>Trainers</h2>

    <table id="trainersTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 300px;">Name</th>
            <th>Email</th>
            <th>Phone</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${trainers}" var="trainer">
            <tr>
                <td>
                	<spring:url value="/trainers/{trainerId}" var="trainerUrl">
                        <spring:param name="trainerId" value="${trainer.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(trainerUrl)}">
                    	<c:out value="${trainer.name} ${trainer.surname}"/>
                    </a>
                </td>
                <td>
                    <c:out value="${trainer.email}"/>
                </td>
                <td>
                    <c:out value="${trainer.phone}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
