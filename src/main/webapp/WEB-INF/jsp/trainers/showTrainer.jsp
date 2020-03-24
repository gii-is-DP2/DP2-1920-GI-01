<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="trainers">

    <h2>Trainer Information</h2>

	<c:if test="${message != 'Trainer not found!'}">
		<table class="table table-striped">
	        <tr>
	            <th>Name</th>
	            <td><b><c:out value="${trainer.firstName} ${trainer.lastName}"/></b></td>
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
	</c:if>
	<c:if test="${message == 'Trainer not found!'}">
		<h3>Trainer not found!</h3>
	</c:if>

</petclinic:layout>
