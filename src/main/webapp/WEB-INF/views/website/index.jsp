<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>




<div class="container">

	<h4 class="text text-danger">
		<span class="glyphicon glyphicon-warning-sign"></span> <b>توجه: </b>سایت
		در دست ساخت است. کلیه محتوای سایت آزمایشی و غیر واقعی هستند
	</h4>


	<c:if test="${not empty successMsg}">
		<div class="alert alert-success">
			<button type="button" class="close" data-dismiss="alert">
				<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
			</button>
			<p>${successMsg}</p>
		</div>

	</c:if>



	<c:if test="${not empty errorMsg}">
		<div class="alert alert-danger">
			<button type="button" class="close" data-dismiss="alert">
				<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
			</button>
			<p>${errorMsg}</p>
		</div>

	</c:if>


	<div class="">
		<div class="col-md-3 col-lg-3 hidden-sm hidden-xs">

		
				<ul class="ca-menu">
					<li><a style="color: black; font-size: 1em;"
						href="${pageContext.request.contextPath}/"><span class="ca-icon"><i
							class="glyphicon glyphicon-th"></i></span>
							
							 <span class="ca-content">
                               <spring:message
								code="deal.all" />
                              
                            </span>
							
							</a></li>
					<c:forEach items="${categories}" var="category">
						<li><a style="color: black; font-size: 1em;"
							href="${pageContext.request.contextPath}?category=${category.id}"><span class="ca-icon"><i
								class="glyphicon glyphicon-th"></i> </span>
								
								 <span class="ca-content">								
								 ${category.name}
								 </span>
								
								</a></li>
					</c:forEach>
				</ul>
	
		</div>

		<div class="col-xs-12 col-sm-12  col-md-9 col-lg-9">
			<c:if test="${not empty featureds[0].images}">
				<div class="row white box">
					<div class="col-sm-12 col-md-7 col-lg-7"
						style="margin: 0; padding: 0;">
						<ul class="pgwSlider">
							<c:forEach items="${featureds[0].images}" var="featured"
								varStatus="status">
								<li><img
									src="${pageContext.request.contextPath}/files/attachments/${featured.id}/dynamicImage?width=600&height=400" /></li>
							</c:forEach>
						</ul>
					</div>
					<div class="col-sm-12 col-md-5 col-lg-5"
						style="padding-right: 15px; padding-left: 15px; padding-top: 0;">

						<h3>${featureds[0].name}</h3>
						<h4>${featureds[0].description}</h4>
						<p class="text text-info" style="font-size: 1.4em">
							<spring:message code="kalatag.realPrice" />
							<fmt:formatNumber type="number" maxFractionDigits="0"
								value="${featureds[0].price}" />
							<spring:message code="kalatag.currency" />
						</p>
						<spring:message code="kalatag.yourpay" />
						<div class="featured">
							<span class="square"> <span class="text text-success"
								style="font-size: 1.6em;"> <fmt:formatNumber
										type="number" maxFractionDigits="0"
										value="${featureds[0].options[0].price}" />

							</span> <spring:message code="kalatag.currency" />
							</span> <span class="discount"> <fmt:formatNumber type="number"
									maxFractionDigits="0"
									value="${featureds[0].options[0].discount}" />%
							</span>
						</div>
						<div class="countdown hidden-md"
							data-countdown="<fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" 
            value="${featureds[0].validity}" />"></div>

						<a class="btn btn-success btn-block"
							href="${pageContext.request.contextPath}/detail?deal=${featureds[0].id}"><span
							class="glyphicon glyphicon-shopping-cart"></span> <spring:message
								code="kalatag.view" /></a>


					</div>

				</div>
				<hr />
			</c:if>
			<div class="row">
				<c:forEach items="${deals}" var="deal">
					<div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">

						<div class="productbox">
							<a
								href="${pageContext.request.contextPath}/detail?deal=${deal.id}">
								<div class="imgthumb img-responsive">
									<img
										src="${pageContext.request.contextPath}/files/deals/${deal.id}/thumbnail?width=230&height=200">
								</div>
								<div class="caption">
									<p>${deal.name}
										<!-- 									<span class="pull-right label label-success"><i
										class="glyphicon glyphicon-shopping-cart"></i> <span class="">10</span></span> -->
									</p>
									<s class="text-muted"><fmt:formatNumber type="number"
											maxFractionDigits="0" value="${deal.price}" /></s>
									<c:if test="${not empty deal.options}">
										<b class="finalprice"> <fmt:formatNumber type="number"
												maxFractionDigits="0" value="${deal.options[0].price}" /> <spring:message
												code="kalatag.currency" />
										</b>
										<!-- from <b>Amazon</b> -->
									</c:if>




								</div> <c:if test="${not empty deal.options}">
									<div class="saleoffrate">
										<b><fmt:formatNumber type="number" maxFractionDigits="0"
												value=" ${deal.options[0].discount}" /> %</b><br>
										<%-- 	<spring:message code="kalatag.off" /> --%>
									</div>
								</c:if> <%-- 						<button type="button" class="btn btn-success btn-md btn-block">
							<spring:message code="kalatag.viewdeal" />
						</button> --%>
							</a>
						</div>

					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</div>

<script>
	$(document).ready(function() {
		$('.pgwSlider').pgwSlider({
			displayList : false,
			displayControls : true,
			intervalDuration : 5000
		});

		startCountdown();
	});
</script>


