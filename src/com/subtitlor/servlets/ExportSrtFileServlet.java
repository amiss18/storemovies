package com.subtitlor.servlets;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
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
 * Export des fichiers srt.
 * 
 */
@WebServlet(urlPatterns = { "/export.srt" })
public class ExportSrtFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// gestionnaire des logs de la servlet EditSubtitle
	private static Logger logger = Logger.getLogger(ExportSrtFileServlet.class.getName());

	private MovieDao movieDao;
	private SubtitleDao subtitleDao;
	//private List<Subtitle> subtitlesList;

	private List<Subtitle> translatedSubtitles;
	private static final String UPLOAD_DIR = "/WEB-INF/upload_dir";

	// type de contenu de la réponse http envoyée au navigateur
	private static final String CONTENT_TYPE = "application/x-subrip";

	List<Subtitle> listeBD;

	@Override
	public void init() throws ServletException {
		super.init();
		DaoFactory daoFactory = DaoFactory.getInstance();
		movieDao = daoFactory.getMovieDao();
		subtitleDao = daoFactory.getSubtitleDao();

	}

	/**
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ServletContext context = getServletContext();
		String idMovie = request.getParameter("id");
		String language = request.getParameter("lang");

		int id = Integer.parseInt(idMovie);
		Movie movie = null;
		try {
			//sélection du film à partir de son id
			movie = movieDao.get(id).get();
		} catch (DaoException e) {
			e.printStackTrace();
		}

		//chemin d'accès au fichier SRT lié au film demandé par l'internaute
		String srtFile = context.getRealPath(UPLOAD_DIR + File.separator + movie.getFilename());
	
		SubtitlesHandler handler = new SubtitlesHandler(movie, srtFile);
		
		// lecture des sous titres depuis le fichier SRT rélatif au film demandé
		handler.readFile();

		//chargement de liste des sous-titres du film demandé
		List<Subtitle> subtitlesList = handler.getSubtitles();

		//récuperation dans la base des traductions réalisées d'un film à partir de l'id et
		//de la langue transmise par l'internaute
		listeBD = subtitleDao.findBy(movie, Language.valueOf(language.toUpperCase()).name());
	
		//chargement de la liste des sous-titres traduits( sans encore les traductions)
		translatedSubtitles = handler.getTranslatedSubtitles();

		/**
		 * Synchronisation de la liste des traductions avec les données de la de BD.
		 */
		for (Subtitle subtitle : listeBD) {

			// indice du sous-titre dans la liste
			int index = translatedSubtitles.indexOf(subtitle);
			// on met à jour la liste initiale dès
			if (index != -1) {
				translatedSubtitles.set(index, subtitle);
			}

		}
		// suppression de toutes les sections non encore traduites qui pointent à null
		translatedSubtitles.removeIf(s -> s.getContentTranslate() == null);
		
		if(translatedSubtitles.isEmpty() ) {
			request.setAttribute("lang", Language.valueOf(language.toUpperCase()).name );
			request.setAttribute("movie", movie );
			// affichage de la home page
			this.getServletContext().getRequestDispatcher("/WEB-INF/srt_not_found.jsp").forward(request, response);
		}else {
			// écriture du type de contenu(application/x-subrip) à envoyer au navigateur
			response.setContentType(CONTENT_TYPE);

			//transmission et écriture du flux de sortie(contenant toutes les traduction)
			//à envoyer au navigateur
			generateSRTfile(response, translatedSubtitles);
		}

		
		
	}

	/**
	 * Ecriture dans le flux de tous les sous-titres traduits
	 * 
	 * @param response
	 * @param translates  liste des sous-titres traduits
	 * @throws IOException
	 */
	public static void generateSRTfile(HttpServletResponse response, List<Subtitle> translates) throws IOException {

		String str = "";// contenu de toutes les sections de tradutions d'un fichier SRT
		for (Subtitle subtitle : translates) {

			//formattage de la durée de debut de traduction avec les millisecondes sur 3 chiffres
			String startTime = subtitle.getStartTime().toString() + ","
					+ String.format("%03d", subtitle.getStartMillis());
			//formattage de la durée de fin de traduction avec les millisecondes sur 3 chiffres
			String endTime = subtitle.getEndTime().toString() + "," + String.format("%03d", subtitle.getEndMillis());
			//formattage de la durée de traduction
			String periode = startTime + " --> " + endTime;

			StringBuilder sb = new StringBuilder();

			sb.append(subtitle.getId() + "\n"); //id
			sb.append(periode + "\n"); // durée
			sb.append(subtitle.getContentTranslate() + "\n"); // sous-titre traduit

			//section d'un fichier SRT: id + durée + sous-titre
			str += sb.toString();
			// ligne vide après chaque section de traduction
			str += "\n";

		}
		// 
		byte[] bytes = str.getBytes();
		try (OutputStream out = response.getOutputStream()) {
			out.write(bytes);
		}catch(IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Impossible d'exporter le fichier SRT");
		}

	}

}
