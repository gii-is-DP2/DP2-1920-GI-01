<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="vets">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#interventionDate").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
    	<c:if test="${message != 'Intervention not found!'}">
	        <h2><c:if test="${intervention['new']}">New </c:if>Intervention</h2>
	
	        <b>Pet</b>
	        
	        <table class="table table-striped">
	            <thead>
	            <tr>
	                <th>Name</th>
	                <th>Birth Date</th>
	                <th>Type</th>
	            </tr>
	            </thead>
	            <tr>
	                <td><c:out value="${intervention.pet.name}"/></td>
	                <td><petclinic:localDate date="${intervention.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
	                <td><c:out value="${intervention.pet.type.name}"/></td>
	            </tr>
	        </table>
	
	        <form:form modelAttribute="intervention" class="form-horizontal">
	        	<input type="hidden" name="petId" value="${intervention.pet.id}"/>
	        	<input type="hidden" name="id" value="${intervention.id}"/>
	            <div class="form-group has-feedback">
	                <petclinic:inputField label="Date" name="interventionDate"/>
	                <petclinic:inputField label="Duration (in hours)" name="interventionTime"/>
	                <petclinic:inputField label="Description" name="interventionDescription"/>
	            </div>
	
	            <div class="form-group">
	                <div class="col-sm-offset-2 col-sm-10">
	                	<c:choose>
	                        <c:when test="${intervention['new']}">
	                            <button class="btn btn-default" type="submit">Add Intervention</button>
	                        </c:when>
	                        <c:otherwise>
	                            <button class="btn btn-default" type="submit">Update Intervention</button>
	                        </c:otherwise>
	                    </c:choose>
	                </div>
	            </div>
	        </form:form>
	
	        <br/>
	        <b>Previous Interventions</b>
	        <table class="table table-striped">
	            <tr>
	                <th>Date</th>
	                <th>Duration</th>
	                <th>Description</th>
	            </tr>
	            <c:forEach var="intervention" items="${intervention.pet.interventions}">
	                <c:if test="${!intervention['new']}">
	                    <tr>
	                        <td><petclinic:localDate date="${intervention.interventionDate}" pattern="yyyy/MM/dd"/></td>
	                        <td><c:out value="${intervention.interventionTime}"/></td>
	                        <td><c:out value="${intervention.interventionDescription}"/></td>
	                    </tr>
	                </c:if>
	            </c:forEach>
	        </table>
	    </c:if>
	    <c:if test="${message == 'Intervention not found!'}">
			<h3>Intervention not found!</h3>
		</c:if>
    </jsp:body>

</petclinic:layout>