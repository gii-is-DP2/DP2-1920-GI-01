<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="organizations">

    <h2>Organizations</h2>

    <table id="organizationsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Name</th>
            <th style="width: 120px">Email</th>
            <th>Phone</th>
            <th style="width: 200px;">URL</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${organizations}" var="organization">
            <tr>
                <td>
                    <spring:url value="/organizations/{id}" var="organizationUrl">
                        <spring:param name="id" value="${organization.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(organizationUrl)}"><c:out value="${organization.name}"/></a>
                </td>
                <td>
                    <c:out value="${organization.email}"/>
                </td>
                <td>
                    <c:out value="${organization.phone}"/>
                </td>
                <td>
                    <a href="${organization.url}"><c:out value="Visit this organization!"/></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</petclinic:layout>
