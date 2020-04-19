<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
	<jsp:body>
		<h2>
           New Adoption
        </h2>
		<form:form modelAttribute="adoption" class="form-horizontal">
			<div class="form-group has-feedback">
				<div class="control-group">
					<petclinic:selectField label="Owner" name="owner" size="15" names="${owners}" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button class="btn btn-default" type="submit">Adopt</button>
				</div>
			</div>
		</form:form>
	</jsp:body>
</petclinic:layout>