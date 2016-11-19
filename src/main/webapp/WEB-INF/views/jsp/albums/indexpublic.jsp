<%@ include file="../../layout/taglib.jsp"%>

<c:url var="baseUrl" value="/public/albums?page=" />
<tiles:putAttribute name="basePaginationUrl" value="${baseUrl}" cascade="true"/>
<link rel="stylesheet" href="<c:url value='/resources_1_9_5/plugins/cube-portfolio/cubeportfolio/css/cubeportfolio.min.css'/>">
<link rel="stylesheet" href="<c:url value='/resources_1_9_5/plugins/cube-portfolio/cubeportfolio/custom/custom-cubeportfolio.css'/>">

<fmt:parseNumber var="count" value="0" />
<fmt:parseNumber var="noColumns" value="4" />
<fmt:parseNumber var="starter" value="0" />
<fmt:parseNumber var="unit" value="1" />
<fmt:parseNumber var="even" value="2" />
<fmt:parseNumber var="albumsSize" value="${fn:length(albums)}" />

<div class="headline">
<h2>All Albums - <c:out value="${page.totalRecords}" /></h2>
</div>


<c:forEach items="${albums}" var="album">

	

		<c:if test="${count == starter || (count%noColumns)==starter}">
					<div class="container content-md team-v1"><ul class="list-unstyled row">
		</c:if>
		
		<li class="col-sm-3 col-xs-6 md-margin-bottom-30">
			<div class="team-img">
				<c:choose>
					<c:when test="${not empty album.uploads && fn:length(album.uploads) >= 1}">
						<img alt="" src="<c:url value='/uploads/download?key=${album.uploads.iterator().next().keyIdentification}'/>" class="img-responsive">
					</c:when>
					<c:otherwise>
						<img alt="" src="<c:url value='/resources_1_9_5/img/team/img10-md.jpg'/>" class="img-responsive">
					</c:otherwise>
				</c:choose>
			
			
				<ul>
					<li><a href="#"><i class="icon-custom icon-sm rounded-x icon-line icon-like"></i></a></li>
					<li><a href="#"><i class="icon-custom icon-sm rounded-x icon-line icon-share"></i></a></li>
					<li><a href="#"><i class="icon-custom icon-sm rounded-x fa fa-facebook"></i></a></li>
					<li><a href="#"><i class="icon-custom icon-sm rounded-x fa fa-google-plus"></i></a></li>
				</ul>
			</div>
			<h3><a href="<c:url value='/albums/view/${album.id}/' />"><c:out value="${album.name}" /></a></h3>
			<h4><c:out value="${fn:length(album.uploads)} photos by ${album.profileOwner.name}" /> | <fmt:formatDate pattern="dd MMMM yy" value="${album.created}" /></h4>
			<p><c:out value="${album.description}" /></p>
			<p><c:out value="${album.place}" /></p>
		</li>
		


		<c:if test="${((count+unit) == albumsSize) || ((count+unit)%noColumns==starter)}">
			</ul></div>
		</c:if>
		

	

	<fmt:parseNumber var="count" value="${count + 1}" />

</c:forEach>

<tiles:insertAttribute name="pagination" ignore="true"/>

<script type="text/javascript" src="<c:url value='/resources_1_9_5/plugins/cube-portfolio/cubeportfolio/js/jquery.cubeportfolio.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources_1_9_5/js/plugins/cube-portfolio/cube-portfolio-lightbox.js'/>"></script>
