<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="organizations">

    <h2>Information about <c:out value="${organization.name}"/></h2>

    <table class="table table-striped">
        <tr>
            <th>Contact information</th>
            <td><c:out value="Email: ${organization.email}"/><br/>
            <c:out value="Phone: ${organization.phone}"/><br/>
            <c:out value="Address: ${organization.address.city} ${organization.address.state} ${organization.address.country}"/></td>
        </tr>
    </table>
    <a href="${organization.url}" target="_blank"><c:out value="Feel free to visit ${organization.name}'s website and learn more about their work!"/></a>
</petclinic:layout>
