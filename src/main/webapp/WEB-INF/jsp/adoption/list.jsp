<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix ="c"  uri ="http://java.sun.com/jsp/jstl/core"%>

<petclinic:layout pageName="owners">
	<h2>Adoption history</h2>
    
    <c:choose>
	    <c:when test="${not empty adoptions}">
			<table id="adoptionsTable" class="table table-striped">
		        <thead>
		        <tr>
		            <th>Date</th>
		            <th>Owner</th>
		        </tr>
		        </thead>
		        <tbody>
		        <c:forEach items="${adoptions}" var="adoption">
		            <tr>
		                <td>
		                    <c:out value="${adoption.date}"/>
		                </td>
		                <td>
		                    <c:out value="${adoption.owner.firstName} ${adoption.owner.lastName}"/>
		                </td>
		            </tr>
		        </c:forEach>
		        </tbody>
	    	</table>
	    </c:when>    
	    <c:otherwise>
	    	<br/>
			<h3>No adoptions found.</h3>
	    </c:otherwise>
	</c:choose>
    
</petclinic:layout>