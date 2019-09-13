package com.subtitlor.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subtitlor.bdd.DaoFactory;
import com.subtitlor.bdd.MovieDao;
import com.subtitlor.beans.FileBean;
import com.subtitlor.beans.Movie;
import com.subtitlor.form.UploadForm;
import com.subtitlor.utilities.Dates;
import com.subtitlor.utilities.Language;

/**
 * Création d'un film et upload de son fichier de traduction.
 * 
 * URL: http://localhost:8080/storemovies/upload
 */
@WebServlet("/upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 10 * 10, maxRequestSize = 1024 * 1024 * 15
		* 10)
public class UploadSrtFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// dossier upload des fichiers SRT
	private static final String UPLOAD_DIRECTORY = "/WEB-INF/upload_dir/";
	// formulaire upload
	private final static String VIEW_FORM_UPLOAD = "/WEB-INF/upload_file.jsp";
	private Map<String, String> errors = new HashMap<String, String>();

	// champ file du formulaire
	public static final String ATT_FICHIER = "file";

	public static final String ATT_FORM = "form";

	// *****
	private MovieDao movieDao;

	@Override
	public void init() throws ServletException {
		super.init();
		DaoFactory daoFactory = DaoFactory.getInstance();
		this.movieDao = daoFactory.getMovieDao();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Affichage des genres du film
		request.setAttribute("genres", Movie.Genre.values());

		// Affichage des langues disponibles dans le formulaire
		request.setAttribute("languages", Language.values());

		// Affichage du formulaire d'upload à la reception d'une requête GET
		this.getServletContext().getRequestDispatcher(VIEW_FORM_UPLOAD).forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/**
		 * champs du formulaire
		 */
		String title = request.getParameter("title");
		String genre = request.getParameter("genre");
		String date = request.getParameter("date");
		String synopsis = request.getParameter("synopsis");
		String language = request.getParameter("language");

		/* Préparation de l'objet formulaire */
		UploadForm form = new UploadForm();
		Movie movie = new Movie();

		ServletContext context = getServletContext();
		String uploadDir = context.getRealPath(UPLOAD_DIRECTORY);
		// date.matches("^\\d{4}(-\\d{1,2}){2}$")
		FileBean file = null;

		/***
		 * on hydrate les setters avec les données du formulaire soumis
		 * si ce dernier ne comporte pas d'erreurs
		 */
		try {
			movie.setTitle(title);
			movie.setGenre(genre);
			movie.setReleaseDate(Dates.parseLocaleDate(date));
			movie.setOriginalLanguage(Language.valueOf(language).name());
			movie.setSynopsis(synopsis);

			//vérifie que le formulaire soumis comporte un fichier SRT
			if (!form.getErrors().isEmpty() || !form.validate(request)) {
				request.setAttribute("errors", errors);
				request.setAttribute("form", form);
				request.setAttribute("movie", movie);

			} else {// un fichier srt a bien été sélectionné
				//uload du fichier dans le dossier WEB-INF/upload_dir
				file = form.saveFile(request, uploadDir);

			}

			movie.setFilename(file.getName());


			// sauvergarde d'un film
			movieDao.save(movie);
			request.setAttribute("success", "Vos données ont été ajoutées avec succès");
			request.setAttribute("movie", movie);

			// Stockage du formulaire et du bean dans l'objet request
			request.setAttribute(ATT_FORM, form);
			//transmission du chemin où a été stocké le fichier uploadé
			request.setAttribute("upload_path", uploadDir + movie.getFilename());

		} catch (Exception e) {// le formulaire comport des erreurs
			e.printStackTrace();
			request.setAttribute("movie", movie);
			errors.put("movieErrors", e.getMessage());
			request.setAttribute("errors", errors);
			request.setAttribute("form", form);

		}

		doGet(request, response);
	}



}
