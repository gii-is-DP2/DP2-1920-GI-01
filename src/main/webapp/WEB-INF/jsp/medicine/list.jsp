<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix ="c"  uri ="http://java.sun.com/jsp/jstl/core"%>

<petclinic:layout pageName="medicines">
	<h2>Find Medicines</h2>
	
	<form:form modelAttribute="medicine" action="/medicine/list" method="get" class="form-horizontal" id="search-medicine-form">
        <div class="form-group">
            <div class="control-group" id="name">
                <label class="col-sm-2 control-label">Name</label>
                <div class="col-sm-10">
                    <form:input class="form-control" path="name" size="30" maxlength="80"/>
                    <span class="help-inline"><form:errors path="*"/></span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Find Medicine</button>
            </div>
        </div>
    </form:form>
    
    <br/>
    
    <c:if test="${not empty results}">
	    <table id="medicinesTable" class="table table-striped">
	        <thead>
	        <tr>
	            <th>Name</th>
	            <th>maker</th>
	            <th>Expiration Date</th>
	            <th>Pet Type</th>
	        </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${results}" var="medicine">
	            <tr>
	                <td>
	                    <spring:url value="/medicine/show?id={medicineId}" var="medicineURL">
	                        <spring:param name="medicineId" value="${medicine.id}"/>
	                    </spring:url>
	                    <strong><a href="${fn:escapeXml(medicineURL)}"><c:out value="${medicine.name}"/></a></strong>
	                </td>
	                <td>
	                    <c:out value="${medicine.maker}"/>
	                </td>
	                <td>
	                    <c:out value="${medicine.expirationDate}"/>
	                </td>
	                <td>
	                    <c:out value="${medicine.petType}"/>
	                </td>
	            </tr>
	        </c:forEach>
	        </tbody>
	    </table>
    </c:if>
    
    <br/>
    
    <a class="btn btn-default" href='<spring:url value="/medicine/create" htmlEscape="true"/>'>Add Medicine</a>
</petclinic:layout>