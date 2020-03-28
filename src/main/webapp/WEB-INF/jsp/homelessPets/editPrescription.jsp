<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="prescription">
	<jsp:body>
		<h2>Add prescription</h2>
        <h3>Medical record of <c:out value="${prescription.medicalRecord.visit.date}"/></h3>
		<form:form modelAttribute="prescription" class="form-horizontal">
			<div class="form-group has-feedback">
				<input type="hidden" name="id" value="${prescription.id}" />
				<div class="control-group">
					<petclinic:selectField label="Medicine" name="medicine" size="15" names="${medicines}" />
				</div>
				<petclinic:inputField label="Dose" name="dose" />
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button class="btn btn-default" type="submit" >Add prescription</button>
				</div>
			</div>
		</form:form>
	</jsp:body>
</petclinic:layout>