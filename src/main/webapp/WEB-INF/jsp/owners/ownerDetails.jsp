<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec"
    uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">

    <h2>Owner Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Address</th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th>City</th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th>Telephone</th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
    </table>

    <spring:url value="{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Owner</a>

    <spring:url value="{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Pet</a>

    <br/>
    <br/>
    <br/>
    <h2>Pets and Visits</h2>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Visit Date</th>
                            <th>Description</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                                <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/{visitId}/medical-record/new" var="medicalRecordUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                    <spring:param name="visitId" value="${visit.id}"/>
                                </spring:url>
                                <sec:authorize access="hasAuthority('veterinarian')">
                                <a href="${fn:escapeXml(medicalRecordUrl)}">Add Medical Record</a>
                                </sec:authorize>
                            </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Edit Pet</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Add Visit</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
			<td>
            	<spring:url value="/owners/{ownerId}/pets/{petId}/medical-history" var="medicalHistoryUrl">
                <spring:param name="ownerId" value="${owner.id}"/>
                <spring:param name="petId" value="${pet.id}"/>
                </spring:url>
            <a href="${fn:escapeXml(medicalHistoryUrl)}">Medical History</a>
            <spring:url value="/owners/{ownerId}/pets/{petId}/adoption-history" var="adoptionHistoryUrl">
                <spring:param name="ownerId" value="${owner.id}"/>
                <spring:param name="petId" value="${pet.id}"/>
            </spring:url>
            <a href="${fn:escapeXml(adoptionHistoryUrl)}">Adoption History</a>
            </td>
        </c:forEach>
    </table>
    
    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Intervention Date</th>
                            <th>Intervention Description</th>
                            <th>Intervention Time</th>
                            <th>Intervention Vet</th>
                        </tr>
                        </thead>
                        <c:forEach var="intervention" items="${pet.interventions}">
                            <tr>
                                <td><petclinic:localDate date="${intervention.interventionDate}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${intervention.interventionDescription}"/></td>
                                <td><c:out value="${intervention.interventionTime}"/></td>
                                <td><c:out value="${intervention.vet.firstName} ${intervention.vet.lastName}"/></td>
                            </tr>
                        </c:forEach>
                        <tr>
                             <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/interventions/new" var="interventionUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(interventionUrl)}">Add Intervention</a>
                            </td>
                        </tr>
                    </table>
                </td>
                
            </tr>

        </c:forEach>
    </table>

<h2>Pets and Rehabilitation</h2>
    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">
        
        
            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>

                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Rehabilitation Date</th>
                  			<th>Rehabilitation Time</th>
                            <th>Rehabilitation Description</th>
                            <th>Rehabilitation Trainer </th>
                        </tr>
                        </thead>
               
                            <c:forEach var="rehab" items="${pet.rehabs}">
                            <tr>
                                <td><petclinic:localDate date="${rehab.date}" pattern="yyyy-MM-dd"/></td>
                                         <td><c:out value="${rehab.time}"/></td>
                                <td><c:out value="${rehab.description}"/></td>
                                <td><c:out value="${rehab.trainer.firstName} ${rehab.trainer.lastName}"/></td>
                                	<td>
                                	
                                	
                                	
                	
                	
                            </tr>
                        </c:forEach>         
           
                        <tr>
                             <td>
                             <sec:authorize access="hasAuthority('trainer')">
                                <spring:url value="/owners/{ownerId}/pets/{petId}/rehab/new" var="rehabUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                    </spring:url>
                                <a href="${fn:escapeXml(rehabUrl)}">Add Rehab</a>     
                                </sec:authorize>
                                
                                
                                                    
                                                                                        
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </c:forEach>
    </table>
    
<h2>Adoptions</h2>
    <table class="table table-striped">
        <c:forEach var="adoption" items="${owner.adoptions}">
            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${adoption.pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${adoption.pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${adoption.pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        	<tr>
                            	<th>Adoption Date</th>
                        	</tr>
                        </thead>
                            <tr>
                                <td><petclinic:localDate date="${adoption.date}" pattern="yyyy-MM-dd"/></td>
                            </tr>
                    </table>
                </td>
            </tr>
        </c:forEach>
    </table>
</petclinic:layout>



