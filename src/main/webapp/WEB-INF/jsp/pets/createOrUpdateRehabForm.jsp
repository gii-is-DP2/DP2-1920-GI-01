<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2><c:if test="${rehab['new']}">New </c:if>Rehabilitation</h2>
        <b>Pet</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th>Birth Date</th>
                <th>Type</th>
                <th>Owner</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${rehab.pet.name}"/></td>
                <td><petclinic:localDate date="${rehab.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${rehab.pet.type.name}"/></td>
                <td><c:out value="${rehab.pet.owner.firstName} ${rehab.pet.owner.lastName}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="rehab" class="form-horizontal">
            <div class="form-group has-feedback">
          
                    
                <petclinic:inputField label="Date" name="date"/>
		<petclinic:inputField label="Time" name="time"/>
                <petclinic:inputField label="Description" name="description"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="petId" value="${rehab.pet.id}"/>    
                    
                    
                    <c:choose>
	                        <c:when test="${rehab['new']}">
	                            <button class="btn btn-default" type="submit">Add Rehabilitation</button>
	                        </c:when>
	                      
	                        <c:otherwise>
	                            <button class="btn btn-default" type="submit">Update Rehabilitation</button>
	                        </c:otherwise>
	                      
	                    </c:choose>                              
                 </div>
            </div>
        </form:form>

        <br/>
        <b>Previous rehabilitations</b>
        <table class="table table-striped">
            <tr>
                <th>Date</th>
                <th>Time</th>
                <th>Description</th>
                 <th>Trainer </th>
            </tr>
            <c:forEach var="rehab" items="${rehab.pet.rehabs}"> 
                <c:if test="${!rehab['new']}">
                    <tr>
                        <td><petclinic:localDate date="${rehab.date}" pattern="yyyy/MM/dd"/></td>
                  		<td><c:out value="${rehab.time}"/></td>
                        <td><c:out value="${rehab.description}"/></td>
                        <td><c:out value="${rehab.trainer.firstName} ${rehab.trainer.lastName}"/></td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </jsp:body>

</petclinic:layout>


