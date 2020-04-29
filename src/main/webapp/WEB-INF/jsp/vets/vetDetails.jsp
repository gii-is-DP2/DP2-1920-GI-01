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
	    
	    <br>
	    <h2>Interventions</h2>
	    <br>
	    <c:if test="${message2 != 'This vet has no interventions'}">
	    
	    <table class="table table-striped">
	    	<c:forEach var="intervention" items="${vet.interventions}">
	    		
	    	
                            <th>Intervention Date</th>
                            <td><petclinic:localDate date="${intervention.interventionDate}" pattern="yyyy-MM-dd"/></td>
                            <th>Intervention Description</th>
                             <td><c:out value="${intervention.interventionDescription}"/></td>
                            <th>Intervention Time</th>
                            <td><c:out value="${intervention.interventionTime}"/></td>
        	
	    	</c:forEach>
	    </table>
	    </c:if>
	    <c:if test="${message2 != 'This vet has no interventions'}">
	    <h4>This vet has no Interventions</h4>
	    </c:if>
	</c:if>
	<c:if test="${message == 'Vet not found!'}">
		<h3>Vet not found!</h3>
	</c:if>
   </petclinic:layout>