<%@taglib uri="http://subtitlor.com/functions" prefix="f"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<c:import url="/WEB-INF/inc/cssLoader.jsp" />
<title>Editer les sous-titres</title>
</head>
<body>
	<%
		/* ========== MENU ==========   */
	%>
	<c:import url="/WEB-INF/inc/menu.jsp"></c:import>

	<div class="container mt-5 form-edit" style="width: 880px">
		<%
			/* ========== Message de validation ==========   */
		%>
		<c:choose>
			<c:when test="${ !empty success }">
				<div class="alert alert-success">
					<c:out value="${success }"></c:out>
	
				</div>
			</c:when>
			<c:when test="${ !empty errors }">
				<div class="alert alert-danger">
					<c:out value='${errors.get("0")}'></c:out>
				</div>
			</c:when>

		</c:choose>

		<h1 class="h4 text-center text-muted">
			Traduction en <strong>${language}</strong>
		</h1>

		<%
			/* ========== form ==========   */
		%>
		<form method="post" class="form form-edit">
			<input class="btn btn-primary btn-lg" type="submit"
				style="position: fixed; top: 120px; right: 30px;" />

			<%-- <fieldset> --%>
			<c:forEach items="${ subtitles }" var="subtitle" varStatus="status">

				<div class="row ">
					<div class="p-2 col bg-white mb-2 line">
						<c:out escapeXml="false" value="${ f:nl2br(subtitle.content) }" />
					</div>
					<div class="p-2 col bg-white mb-2 ml-1 line">
						<textarea class="form-control" name="line${ status.index }"
							id="line${ status.index }">
		        			<c:out value="${subtitle.contentTranslate }">${translatedSubtitles[status.index].contentTranslate}</c:out>
	        		</textarea>
					</div>
				</div>

			</c:forEach>
		</form>

	</div>

	<%
		/* ========== FOOTER ==========   */
	%>
	<c:import url="/WEB-INF/inc/footer.jsp"></c:import>

</body>

<script type="text/javascript">
	let doc = document.querySelector("textarea")
	doc.value.trim()
	function resetCursor(txtElement) {
		if (txtElement.setSelectionRange) {
			txtElement.focus();
			txtElement.setSelectionRange(0, 0);
		} else if (txtElement.createTextRange) {
			var range = txtElement.createTextRange();
			range.moveStart('character', 0);
			range.select();
		}
	}
</script>

</html>