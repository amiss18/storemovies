package com.subtitlor.beans;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.subtitlor.exceptions.BeanException;
import com.subtitlor.utilities.Language;

/**
 * Bean qui va mapper les informations liées au film( id, titre, fichier SRT,...)
 * @author armel
 *
 */
public class Movie {

	
	private int id;
	
	private String title;
	
	private String filename;
	
	private String synopsis;
	
	private LocalDate releaseDate;
	
	private String genre;
	private String originalLanguage;

	
	public enum Genre { Historique,Comédie, Action, Documentaire, Fiction, Aventure, Inconnu	}
	
	private List<Subtitle> subtitles = new ArrayList<Subtitle>();

			
	
	
	// ======================================
	// = Getters & setters					=
	// ======================================
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) throws BeanException {
		if( title ==null || title.trim().length() ==0 ) 
			throw new BeanException("Le titre n'est pas valide");
		this.title = title;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
		
	public List<Subtitle> getSubtitles() {
		return subtitles;
	}

	public void setSubtitles(List<Subtitle> subtitles) {
		this.subtitles = subtitles;
	}
	
	public void addSubtitle( Subtitle subtitle ) {
		subtitles.add(subtitle);
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) throws BeanException {
		if( synopsis ==null || synopsis.trim().length() < 5 ) 
			throw new BeanException("Le synopsis n'est pas valide");
		this.synopsis = synopsis;
	}


	
	
	public String getOriginalLanguage() {
		return originalLanguage;
	}

	public void setOriginalLanguage(String originalLanguage) throws BeanException {
		if( originalLanguage ==null || originalLanguage.trim().isEmpty() ) 
			throw new BeanException("La langue n'est pas valide");
		
		try {
			this.originalLanguage = Language.valueOf( originalLanguage.toUpperCase()).name();
		} catch ( IllegalArgumentException e) {
			this.originalLanguage = Language.EN.name();
			e.printStackTrace();
			throw new BeanException("La langue n'est pas valide ");
		}
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) throws BeanException {
		
		if( genre ==null || genre.trim().length() < 3 ) 
			throw new BeanException("Le genre est requis");
		
		try {
			this.genre = Genre.valueOf(genre ).name();
		} catch ( IllegalArgumentException e) {
			throw new BeanException("Le genre n'est pas valide");
		}
	}

	
	@Override
	public String toString() {
		return "Subtitle {" + "Id='" + id + '\'' +
				", Date de sortie='" + releaseDate + '\'' +
				", Genre='" + Genre.values() + '\''+
				", Titre ='" + title + '\''
				+ '}';
	}
	

	
	
	

}
