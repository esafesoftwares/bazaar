<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="container">


	<div class="col-md-8">
		<div class="panel panel-default">
			<div class="panel-heading">

				<span class="glyphicon glyphicon-plus-sign"></span>
				<spring:message code="merchant.insert.message" />

			</div>
			<div class="panel-body">
				<form:form method="POST" commandName="merchant" class="form"
					action="${pageContext.request.contextPath}/admin/merchant/add">

					<div class="row">
						<div class="form-group  col-sm-8">

							<spring:message code="merchant.name" />
							<form:input class="form-control input-sm" path="name"></form:input>
							<form:errors path="name" cssClass="text text-danger" />
						</div>
					</div>
					<h4>
						<spring:message code="merchant.contactpoint" />
					</h4>
					<div class="row">

						<div class="col-sm-4">

							<div class="form-group">
								<spring:message code="person.firstname" />
								<form:input class="form-control input-sm"
									path="contactPoint.firstName"></form:input>
								<form:errors path="contactPoint.firstName"
									cssClass="text text-danger" />
							</div>
							<div class="form-group">
								<spring:message code="person.lastname" />
								<form:input class="form-control input-sm"
									path="contactPoint.lastName"></form:input>
								<form:errors path="contactPoint.lastName"
									cssClass="text text-danger" />
							</div>

						</div>

						<div class="col-sm-4">
							<div class="form-group">
								<spring:message code="person.birthday" />
								<form:input class="form-control input-sm"
									placeholder="e.g 1983/10/23" path="contactPoint.birthday"></form:input>
								<form:errors path="contactPoint.birthday"
									cssClass="text text-danger" />
							</div>
							<div class="form-group">
								<spring:message code="person.gender" />
								<form:select class="form-control input-sm"
									path="contactPoint.gender">
									<form:options />
								</form:select>
								<form:errors path="contactPoint.gender"
									cssClass="text text-danger" />
							</div>
						</div>
						<div class="col-sm-4">
							<div class="form-group">
								<label></label>
								<div class="checkbox">
									<label> <input type="checkbox"
										name="contactPoint.enabled" checked> <spring:message
											code="person.enabled" />
									</label>
								</div>

							</div>
						</div>
						<%-- 						<div class="col-sm-4">
							<div class="form-group">
								<spring:message code="person.username" />
								<form:input class="form-control input-sm"
									path="contactPoint.username"></form:input>
										<form:errors path="contactPoint.username" cssClass="text text-danger" />
							</div>
							<div class="form-group">
								<spring:message code="person.password" />
								<form:input type="password" class="form-control input-sm"
									path="contactPoint.password"></form:input>
									<form:errors path="contactPoint.password" cssClass="text text-danger" />
							</div>
						</div> --%>
					</div>
					<div class="row">
						<div class="col-sm-4">
							<div class="form-group">
								<spring:message code="contact.mobile" />
								<form:input class="form-control input-sm" path="contact.mobile"></form:input>
								<form:errors path="contact.mobile" cssClass="text text-danger" />
							</div>
							<div class="form-group">
								<spring:message code="contact.phone" />
								<form:input class="form-control input-sm" path="contact.phone"></form:input>
								<form:errors path="contact.phone" cssClass="text text-danger" />
							</div>
						</div>
						<div class="col-sm-4">
							<div class="form-group">
								<spring:message code="contact.fax" />
								<form:input class="form-control input-sm" path="contact.fax"></form:input>
								<form:errors path="contact.fax" cssClass="text text-danger" />
							</div>
							<div class="form-group">
								<spring:message code="contact.email" />
								<form:input class="form-control input-sm" path="contact.email"></form:input>
								<form:errors path="contact.email" cssClass="text text-danger" />
							</div>
						</div>
						<div class="col-sm-4">
							<div class="form-group">
								<spring:message code="contact.website" />
								<form:input class="form-control input-sm" path="contact.website"></form:input>
								<form:errors path="contact.website" cssClass="text text-danger" />
							</div>
							<div class="form-group">
								<spring:message code="contact.city" />
								<form:select class="form-control input-sm" path="contact.city"
									items="${cities}" itemValue="id" itemLabel="name">
									<form:errors path="contact.city" cssClass="text text-danger" />

								</form:select>
							</div>

						</div>
						<div class="col-sm-6">
							<div class="form-group">
								<spring:message code="contact.geoLocation" />
								<a href="https://www.google.com/maps" target="_blank">Get
									location from google map...</a>
								<form:input class="form-control input-sm"
									path="contact.geoLocation"></form:input>
								<form:errors path="contact.geoLocation"
									cssClass="text text-danger" />
							</div>
						</div>
					</div>
					<div class="col-sm-12">
						<div class="form-group">
							<spring:message code="contact.address" />
							<form:input class="form-control input-sm" path="contact.address"></form:input>
							<form:errors path="contact.address" cssClass="text text-danger" />
						</div>
					</div>
			</div>

			<div class="form-group">

				<input class="btn btn-danger btn-sm"
					value="<spring:message code="submit"/>" type="submit"> <a
					href="${pageContext.request.contextPath}/admin/merchant"
					class="btn btn-default btn-sm"><spring:message code="cancel" /></a>
			</div>


			</form:form>
		</div>
	</div>
</div>
</div>



