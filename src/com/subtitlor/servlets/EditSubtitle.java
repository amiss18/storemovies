package com.subtitlor.servlets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subtitlor.bdd.DaoFactory;
import com.subtitlor.bdd.MovieDao;
import com.subtitlor.bdd.SubtitleDao;
import com.subtitlor.beans.Movie;
import com.subtitlor.beans.Subtitle;
import com.subtitlor.exceptions.DaoException;
import com.subtitlor.utilities.Language;
import com.subtitlor.utilities.SubtitlesHandler;

/**
 * Servlet qui charge le formulaire d'édition d'un film et s'occuper de la
 * sauvegarde des traductions en BD
 * 
 * @author armel
 *
 */
@WebServlet("/edit")
public class EditSubtitle extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// dossier des fichiers des sous-titres SRT
	private static final String UPLOAD_DIR = "/WEB-INF/upload_dir";
	// gestionnaire des logs de la servlet EditSubtitle
	private static Logger logger = Logger.getLogger(EditSubtitle.class.getName());

	private MovieDao movieDao;
	private SubtitleDao subtitleDao;

	// liste des sous-titres en langue d'origine
	private List<Subtitle> subtitlesList;

	// liste des taductions
	private List<Subtitle> translatedSubtitles;

	// traductions extraites de la base de données
	List<Subtitle> listeBD;

	// stocke les erreurs à afficher dans la page jsp
	private Map<String, String> errors = new HashMap<String, String>();

	@Override
	public void init() throws ServletException {
		super.init();
		DaoFactory daoFactory = DaoFactory.getInstance();
		movieDao = daoFactory.getMovieDao();
		subtitleDao = daoFactory.getSubtitleDao();
	}

	/**
	 * Affichage le formulaire d'édition d'un film selon langue et l'id de ce film
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = getServletContext();

		String idMovie = request.getParameter("id");
		String lang = "";
		if (request.getParameter("lang") != null)
			lang = request.getParameter("lang");

		lang = Language.valueOf(lang.toUpperCase()).name();

		// id du film en int
		int id = Integer.parseInt(idMovie);

		Movie movie = null;
		// on vérifie l'existence du film pour lequel on veut traduire
		// s'il n'existe pas une page 404 sera appelée
		try {
			if (movieDao.get(id).isPresent()) {
				// on recupère le film
				movie = movieDao.get(id).get();

				String srtFile = context.getRealPath(UPLOAD_DIR + File.separator + movie.getFilename());

				SubtitlesHandler handler = new SubtitlesHandler(movie, srtFile);
				handler.readFile();

				subtitlesList = handler.getSubtitles();

				listeBD = subtitleDao.findBy(movie, lang);

				translatedSubtitles = handler.getTranslatedSubtitles();

				/**
				 * Synchronisation de la liste des traductions avec les données de la de BD.
				 */
				for (Subtitle subtitle : listeBD) {
					// on compare l"élement récuperé en BD avec celui de la liste des traductions
					// s'il est trouvé on recupère son indice dans cette liste
					int index = translatedSubtitles.indexOf(subtitle);
					// on met à jour la liste des traductions
					if (index != -1) {
						translatedSubtitles.set(index, subtitle);
					}
				}

				//transmission de la liste des sous-titres dans le formulaire(représent les labels)
				request.setAttribute("subtitles", subtitlesList);
				//transmissions des traductions dans le form( hydratation des champs textarea du form)
				request.setAttribute("translatedSubtitles", translatedSubtitles);

				// Affichage de la langue traduction demandée dans le formulaire d'édition
				request.setAttribute("language", Language.valueOf(request.getParameter("lang").toUpperCase()).name);

				this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);
			}
		} catch (DaoException e) {
			e.printStackTrace();
			this.getServletContext().getRequestDispatcher("/WEB-INF/errors/404.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id = request.getParameter("id");

		ServletContext context = getServletContext();

		if (id != null && !id.equals("")) {
			processPostData(request, context);
		}
		// envoie de liste de traduction dans la vue
		request.setAttribute("subtitles", subtitlesList);
		request.setAttribute("translatedSubtitles", translatedSubtitles);
		// Affichage de traduction demandée dans le formulaire d'édition
		request.setAttribute("language", Language.valueOf(request.getParameter("lang").toUpperCase()).name);

		// réaffichage du formulaire
		this.getServletContext().getRequestDispatcher("/WEB-INF/edit_subtitle.jsp").forward(request, response);

	}

	/**
	 * traitement des données envoyées avec la méthode POST
	 * 
	 * @param request
	 */
	public void processPostData(HttpServletRequest request, ServletContext context) {
		// langue de traduction
		String lang = request.getParameter("lang");

		try {
			lang = Language.valueOf(lang.toUpperCase()).name();
		} catch (IllegalArgumentException e) {
			logger.log(Level.WARNING, " Cette langue n'est pas disponible");
		}

		// map contenant tous les champs du formulaire
		Map<String, String[]> map = request.getParameterMap();

		// liste contenant tous les messages traduits du formulaire
		List<Subtitle> dataFromForm = new ArrayList<Subtitle>();
		// id film
		String idMovie = request.getParameter("id");
		int id = Integer.parseInt(idMovie);
		Movie movie = null;

		SubtitlesHandler handler = null;
		String srtFile = null;
		//
		try {
			// vérification film demandé pour l'édition des sous-tires
			if (movieDao.get(id).isEmpty())
				throw new RuntimeException(String.format("Le film %s n'existe pas", id));
			else {// film existant
				movie = movieDao.get(id).get();

				// chemin du fichier SRT du film demandé
				srtFile = context.getRealPath(UPLOAD_DIR + File.separator + movie.getFilename());
				handler = new SubtitlesHandler(movie, srtFile);
				// Lecture du fichier srt du film
				handler.readFile();

			}
		} catch (DaoException e1) {
			e1.printStackTrace();
			errors.put("1", e1.getMessage());
		}

		/*
		 * Parcours de tous les champs du formulaire d'édition du film On ne s'interesse
		 * qu'aux champs non vides
		 */
		for (Map.Entry<String, String[]> entry : map.entrySet()) {

			for (String v : entry.getValue()) {
				if (v != null) {
					String value = v.trim(); // translateSubtitle

					// Récupération des messages traduits(et valorisés) dans les champs lineX du
					// formulaire
					if (!value.isEmpty() && entry.getKey().startsWith("line")) {
						// récuperation du numéro de champ(line...) du formulaire
						int k = Integer.parseInt(entry.getKey().substring(4));
						/**
						 * on met à jour la liste des messages traduits en fonction des index(line0,
						 * line1...lineN) envoyés depuis le formulaire
						 */
						// message de traduction
						// Objet Subtitle correspondant à l'indice k du champ du formulaire
						Subtitle subtitle = subtitlesList.get(k);
						// mise à jour de la liste des traductions
						subtitle.setContentTranslate(value);
						// code de la langue : FR, ES, EN, DE
						subtitle.setLanguage(Language.valueOf(lang).name());
						subtitle.setMovie(movie);

						// ajout de la traduction dans liste
						dataFromForm.add(subtitle);

					}

				}
			}

			try {
				subtitleDao.save(dataFromForm);
				// handler.writeSubtitles(dataFromForm, Language.valueOf(lang).name());
				// transmission du chemin du fichier de traduction à la vue
				request.setAttribute("success", "Cette opération a été exécutée avec succès");
				// request.setAttribute("srt", handler.generateSubtitleFileName(srtFile,
				// Language.valueOf(lang).name()));

			} catch (Exception e) { //
				e.printStackTrace();
				errors.put("0", e.getMessage());
				request.setAttribute("success", null);
				request.setAttribute("errors", errors);
			}
		}

	}

}
