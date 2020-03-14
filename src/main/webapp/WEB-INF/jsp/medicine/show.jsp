<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="medicines">
	<h2>Medicine information</h2>
	<table class="table table-striped">
		<tr>
			<th>Product name</th>
			<td><b><c:out value="${medicine.name}"/></b></td>
		</tr>
		<tr>
			<th>Expiration Date</th>
			<td><c:out value="${medicine.expirationDate}"/></td>
		</tr>
		<tr>
			<th>Maker</th>
			<td><b><c:out value="${medicine.maker}"/></b></td>
		</tr>
		<tr>
			<th>Pet type</th>
			<td><b><c:out value="${medicine.petType}"/></b></td>
		</tr>
	</table>
</petclinic:layout>