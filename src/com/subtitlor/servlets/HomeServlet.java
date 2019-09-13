package com.subtitlor.servlets;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subtitlor.bdd.DaoFactory;
import com.subtitlor.bdd.MovieDao;
import com.subtitlor.bdd.SubtitleDao;
import com.subtitlor.beans.Movie;
import com.subtitlor.utilities.Language;

/**
 * 
 * Servlet en charge de la home page (home.jsp).
 * Elle représente le point d'entrée de l'application.
 * 
 * URL: http://localhost:8080/storemovies
 */
@WebServlet(name = "index", urlPatterns = { "" })
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MovieDao movieDao;

	@Override
	public void init() throws ServletException {
		super.init();
		DaoFactory daoFactory = DaoFactory.getInstance();
		movieDao = daoFactory.getMovieDao();
	}

	/**
	 * Affiche l'ensemble des films présents en BD permettant
	 * à l'utiilisateur d'éffectuer la traduction et l'export de fichiers
	 *  SRT pour chacun des films
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// transmission des films à la vue
		request.setAttribute("movies", movieDao.getAll());
		// transmission des langues dispos à la vue
		request.setAttribute("languages", Language.values());
		// affichage de la home page
		this.getServletContext().getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
