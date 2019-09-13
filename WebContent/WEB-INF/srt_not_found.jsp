<!DOCTYPE html>
<html>
<head>

<meta charset="utf-8" />
<c:import url="/WEB-INF/inc/cssLoader.jsp"></c:import>
<title>Accueil</title>
</head>
<body>

	<!-- ========== Menu ========== -->
	<c:import url="/WEB-INF/inc/menu.jsp"></c:import>
	


	<!-- ========== bloc content ========== -->

	<div class="container mt-5">
		<div class="d-flex justify-content-center mt-5">

			<div class="alert  alert-danger mt-10">
				<h4 class="text-center">
					<strong>Oups!</strong> Il n'y a pas encore de traduction en
					 <strong><c:out value="${lang }"></c:out></strong> pour ce film
				</h4>
			</div>
			
			

		</div>
		
		<div class="row">
		
			<div class="col-md-2 col-md-offset-1"></div>
			<div class="col-md-7">
			
			<p class="py-2"> Veuillez cliquer sur le lien <a	class=" text-xs-center" href='<c:url value="/edit?id=${movie.id}&lang=${param.lang}" />'>
							traduire
			</a> pour commencer la traduction.</p>		
			</div>
		
		</div>
		
		<p class="text-center mt-5 py-5">
			<a href="<%=application.getContextPath()%>"> >>Home</a>
		</p>
	</div>
	<!-- /.container -->


</body>
</html>