
<!-- ========== Menu ========== -->

<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top bg-main  ">
	<div class="container">

		<a class="navbar-brand" href="<%=application.getContextPath()%>">Movies st</a>

		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#navbarResponsive" aria-controls="navbarResponsive"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		
		<div class="collapse navbar-collapse" id="navbarText">
		  <ul class="navbar-nav mr-auto">
		    <li class="nav-item active">
		      <a class="nav-link" href='<c:url value="upload"/>'>Upload fichier SRT</a>
		    </li>
		  </ul>
		 <!--  <span class="navbar-text">
		    Navbar text with an inline element
		  </span> -->
		</div>
 
		<div class="collapse navbar-collapse" id="navbarResponsive">
			<ul class="navbar-nav ml-auto">
				<li class="nav-item active">
					<a class="nav-link" href="https://github.com/amiss18/storemovies" target="_blank">Accès github
							<span class="sr-only">(current)</span>
					</a>
				</li>
				
			</ul>
		</div> 

	</div>
</nav>