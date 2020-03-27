<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
	
	<c:if test="${message != 'Vet not found!'}">
	    <h2>Vet Information</h2>
	
	
	    <table class="table table-striped">
	        <tr>
	            <th>Name</th>
	            <td><b><c:out value="${vet.firstName} ${vet.lastName}"/></b></td>
	        </tr>
	        <tr>
	        	<th>Specialty</th>
	        	<td>
	        	 	<c:forEach var="specialty" items="${vet.specialties}">
	                        <c:out value="${specialty.name}"/>
	                    </c:forEach>
	                    <c:if test="${vet.nrOfSpecialties == 0}">none</c:if>
	             </td>
	        </tr>
	    </table>
	</c:if>
	<c:if test="${message == 'Vet not found!'}">
		<h3>Vet not found!</h3>
	</c:if>
   </petclinic:layout>