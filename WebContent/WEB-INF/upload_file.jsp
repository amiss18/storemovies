<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<c:import url="/WEB-INF/inc/cssLoader.jsp"></c:import>

<title>Uploader fichier srt</title>

</head>
<body>
	<%/* ========== MENU ==========   */ %>  
	<c:import url="/WEB-INF/inc/menu.jsp"></c:import>

	<div class="container" style="width: 900px">
	
		<%/* ========== Message de validation ==========   */ %>  
		<c:choose>
			<c:when test="${ !empty success }">
				<div class="alert alert-success mt-5">
					<c:out value="${success }"></c:out>
					<br /> Chemin du fichier: <small><c:out
							value="${upload_path }"></c:out></small>
				</div>
			</c:when>
			<c:when test="${ !empty errors || !empty form.errors }">
				<div class="alert alert-danger mt-5">
					<c:forEach items="${errors }" var="entry" varStatus="i">
						<c:out value='${entry.value}'></c:out>
					</c:forEach>
					<br />
					<c:out value='${form.errors["file"]}'></c:out>

				</div>
			</c:when>

		</c:choose>

		<%/* ========== bloc form ==========   */ %>  
		
		<form action='<c:url value="/upload" />' class="form form-upload"
			method="post" enctype="multipart/form-data">
			<h2>Envoi d'un fichiet SRT et details du film</h2>
			<p class="text-muted"><small>Le film ajouté(+ fichier SRT) apparaîtra sur la page d'accueil pour la traduction ou l'export</small></p>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label for="title">Titre du film</label> <input type="text"
							name="title" required="required" value='<c:out value="${movie.title }"/>'
							class="form-control" placeholder="" id="title">
					</div>
				</div>
				<!--  col-md-6   -->

				<div class="col-md-6">
					<div class="form-group">
						<label for="date">Date de parution</label> <input type="date"
							name="date" value='<c:out value="${movie.releaseDate }"/>'
							required="required" class="form-control" placeholder="" id="date">
					</div>
				</div>
				<!--  col-md-6   -->
			</div>


			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label for="file">Fichier SRT</label> <input name="file"
							required="required" type="file" class="form-control-file" id="file">
					</div>
				</div>
				<!--  col-md-6   -->

				<div class="col-md-6">
					<div class="form-group">
						<label for="phone">Genre</label> <select name="genre"
							class="form-control" id="genre" size="0">
							<c:forEach items="${genres }" var="item" varStatus="i">
								<option value="<c:out value='${item}'/>"
									<c:out value='${item == param.genre ? "selected": ""}'/>><c:out
										value='${item}' /></option>
							</c:forEach>
						</select>
					</div>
				</div>
				<!--  col-md-6   -->
			</div>

			<!--  row   -->
			<div class="row">
				<div class="col-md-6">

					<div class="form-group">
						<label for="language">Langue d'origine</label> <select
							name="language" class="form-control" id="user_time_zone" size="0">
							<c:forEach items="${languages }" var="item" varStatus="i">
								<option value="<c:out value='${item.name()}'/>"
									<c:out value='${item.name() == param.language ? "selected":""}'/>>
									<c:out value='${item}' /></option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
			<!--  row   -->



			<!--  row   -->
			<div class="row">
				<div class="col-md-10">
					<div class="form-group">
						<label for="inputComments">Synopsis</label>
						<textarea name="synopsis" id="inputComments" required minlength="5" class="form-control">
						 <c:out value="${movie.synopsis }">${movie.synopsis }</c:out>
						</textarea>
					</div>
				</div>
				<!--  col-md-10   -->
			</div>

			<button type="submit" class="btn btn-primary">Submit</button>
		</form>


	</div>

   <%/* ========== FOOTER ==========   */ %>  
	<c:import url="/WEB-INF/inc/footer.jsp"></c:import>

	<script type="text/javascript">	</script>
</body>
</html>