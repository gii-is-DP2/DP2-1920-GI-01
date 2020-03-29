<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="trainers">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
    	<c:if test="${message != 'Rehab not found!'}">
	        <h2><c:if test="${rehab['new']}">New </c:if>Rehab</h2>
	
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
	                <td><c:out value="${rehab.pet.name}"/></td>
	                <td><petclinic:localDate date="${rehab.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
	                <td><c:out value="${rehab.pet.type.name}"/></td>
	            </tr>
	        </table>
	
	        <form:form modelAttribute="rehab" class="form-horizontal">
	        	<input type="hidden" name="petId" value="${rehab.pet.id}"/>
	        	<input type="hidden" name="id" value="${rehab.id}"/>
	            <div class="form-group has-feedback">
	                <petclinic:inputField label="Date" name="date"/>
	                <petclinic:inputField label="Duration (in hours)" name="time"/>
	                <petclinic:inputField label="Description" name="description"/>
	            </div>
	
	            <div class="form-group">
	                <div class="col-sm-offset-2 col-sm-10">
	                	<c:choose>
	                        <c:when test="${rehab['new']}">
	                            <button class="btn btn-default" type="submit">Add Rehab</button>
	                        </c:when>
	                        <c:otherwise>
	                            <button class="btn btn-default" type="submit">Update Rehab</button>
	                        </c:otherwise>
	                    </c:choose>
	                </div>
	            </div>
	        </form:form>
	
	        <br/>
	        <b>Previous Rehabs</b>
	        <table class="table table-striped">
	            <tr>
	                <th>Date</th>
	                <th>Duration</th>
	                <th>Description</th>
	            </tr>
	            <c:forEach var="rehab" items="${rehab.pet.rehabs}">
	                <c:if test="${!rehab['new']}">
	                    <tr>
	                        <td><petclinic:localDate date="${rehab.date}" pattern="yyyy/MM/dd"/></td>
	                        <td><c:out value="${rehab.time}"/></td>
	                        <td><c:out value="${rehab.description}"/></td>
	                    </tr>
	                </c:if>
	            </c:forEach>
	        </table>
	    </c:if>
	    <c:if test="${message == 'Rehab not found!'}">
			<h3>Rehab not found!</h3>
		</c:if>
    </jsp:body>

</petclinic:layout>