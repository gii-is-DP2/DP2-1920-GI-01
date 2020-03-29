<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="medical record">
	<jsp:body>
		<h2>
            <c:if test="${medicalRecord['new']}">New </c:if>Medical Record
        </h2>
        <div class="form-group">
        	<h3>Visit <c:out value=" on ${medicalRecord.visit.date} about ${medicalRecord.visit.description} of the pet ${medicalRecord.visit.pet.name}"/></h3>
     	</div>
		<form:form modelAttribute="medicalRecord" class="form-horizontal">
			<div class="form-group has-feedback">
				<input type="hidden" name="id" value="${medicalRecord.id}" />
				<petclinic:inputField label="Description" name="description" />
				<petclinic:inputField label="Status" name="status" />
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<c:choose>
						<c:when test="${medicalRecord['new']}">
							<button class="btn btn-default" type="submit" >Add medical record</button>
						</c:when>
						<c:otherwise>
							<button class="btn btn-default" type="submit" >Update medical record</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</form:form>
	</jsp:body>
</petclinic:layout>