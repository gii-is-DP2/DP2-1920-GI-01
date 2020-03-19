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
  	  <h2><c:if test="${intervention['new']}">New </c:if>Intervention</h2>
  	  
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
                <td><c:out value="${intervention.pet.name}"/></td>
                <td><petclinic:localDate date="${intervention.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${intervention.pet.type.name}"/></td>
                <td><c:out value="${intervention.pet.owner.firstName} ${intervention.pet.owner.lastName}"/></td>
            </tr>
        </table>
        
         <form:form modelAttribute="intervention" class="form-horizontal">
            <div class="form-group has-feedback">

                <petclinic:inputField label="InterventionDescription" name="interventionDescription"/>
                <petclinic:inputField label="InterventionDate" name="interventionDate"/>
                <petclinic:inputField label="InterventionTime" name="interventionTime"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="petId" value="${intervention.pet.id}"/>
                    <button class="btn btn-default" type="submit">Add Intervention</button>
                </div>
            </div>
        </form:form>
        
        <br/>
        <b>Previous Interventions</b>
        <table class="table table-striped">
            <tr>
                <th>InterventionDate</th>
                <th>InterventionTime</th>
                <th>InterventionDescription</th>

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
    </jsp:body>
    
</petclinic:layout>