<%@taglib uri="http://subtitlor.com/functions" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<c:import url="/WEB-INF/inc/cssLoader.jsp" />
<meta charset="UTF-8">
<title>Accueil- Liste des films</title>
</head>
<body>

	<%
		/* ========== MENU  ==========   */
	%>
	<c:import url="/WEB-INF/inc/menu.jsp"></c:import>

	<section id="team" class="pb-5">
		<div class="container">
			<h5 class="section-title h1">Films à traduire et à exporter</h5>
			<div class="row">

			 <%/* ========== Home: Liste tous les films ==========   */ %>  
				<c:forEach items="${movies }" var="movie" varStatus="indice">
					<!-- === Movie=== -->
					<div class="col-xs-12 col-sm-6 col-md-4">
						<div class="image-flip"
							ontouchstart="this.classList.toggle('hover');">
							<div class="mainflip">

								<div class="backside">
									<div class="card">
										<div class="card-body text-center mt-4">
											<h5 class="card-title">
												<c:out value="${f:truncate( movie.title,32) }..."></c:out>
											</h5>
											<p class="card-text movie-synopsis">
												<c:out value="${f:truncate( movie.synopsis,190) }..." />
											</p>
											<ul class="list-inline">

												<%
													/* ========== Bloc export ==========   */
												%>

												<c:forEach items="${languages }" var="lang" varStatus="st">
													<li class="list-inline-item"><a
														class="social-icon text-xs-center"
														href='<c:url value="/export.srt?id=${movie.id}&lang=${lang.name().toLowerCase()}" />'>
															<c:choose>
																<c:when test="${lang.name() != movie.originalLanguage }">
                                               		Exportez(${lang.name()})
                                               	</c:when>
															</c:choose>
													</a></li>
												</c:forEach>

												<hr />

												<%
													/* ========== Bloc traduire ==========   */
												%>

												<c:forEach items="${languages }" var="lang" varStatus="st">
													<li class="list-inline-item"><a
														class="social-icon text-xs-center"
														href='<c:url value="/edit?id=${movie.id}&lang=${lang.name().toLowerCase()}" />'>
															<c:choose>
																<c:when test="${lang.name() != movie.originalLanguage }">
                                               		Traduire(${lang.name()})
                                               	</c:when>
																<c:otherwise>
																	<c:set value="${lang.name()}" var="origine"></c:set>
																</c:otherwise>

															</c:choose>
													</a></li>
												</c:forEach>
												<br />
												<abbr title="Langue d'origine du film"> Origine:<c:out
														value="${origine}" /></abbr>


											</ul>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- ./Movie -->

				</c:forEach>

			</div>
		</div>
	</section>

   <%/* ========== FOOTER ==========   */ %>  
	<c:import url="/WEB-INF/inc/footer.jsp"/>
	<script type="text/javascript"
		src="<c:url value="/resources/js/confirm.js"/>"></script>
</body>
</html>

