
<!DOCTYPE>
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
					<strong>Oups!</strong> La page demandée n'existe pas
				</h4>
			</div>

		</div>
		
		<p class="text-center">
			<a href="<%=application.getContextPath()%>"> >>Home</a>
		</p>
	</div>
	<!-- /.container -->


</body>
</html>