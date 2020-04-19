<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="medicines">
	<jsp:attribute name="customScript">
		<script>
			$(function() {
				$("#expirationDate").datepicker({dateFormat: 'yy/mm/dd'});
			});
		</script>
	</jsp:attribute>
	<jsp:body>
		<h2>
            <c:if test="${medicine['new']}">New </c:if>Medicine
        </h2>
		<form:form modelAttribute="medicine" class="form-horizontal">
			<div class="form-group has-feedback">
				<petclinic:inputField label="Product name" name="name" />
				<petclinic:inputField label="Expiration Date" name="expirationDate" />
				<petclinic:inputField label="Maker" name="maker" />
				<div class="control-group">
					<petclinic:selectField label="Pet Type" name="petType" size="5" names="${petTypes}" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<c:choose>
						<c:when test="${medicine['new']}">
							<button class="btn btn-default" type="submit" >Add medicine</button>
						</c:when>
						<c:otherwise>
							<button class="btn btn-default" type="submit" >Update medicine</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</form:form>
	</jsp:body>
</petclinic:layout>