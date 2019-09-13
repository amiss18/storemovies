package com.subtitlor.beans;

import java.time.LocalTime;
import java.util.Optional;

import com.subtitlor.utilities.Language;

/**
 * bean qui va mapper les informations d'une section SRT telles que son id, sa
 * durée, et le texte sous-titré.
 * 
 * @author armel
 *
 */
public class Subtitle {

	private int id;
	private String content; // sous-titre

	private String contentTranslate; // sous-titre traduit

	private LocalTime startTime; // début de la traduction(durée hh:mm:ss)
	private LocalTime endTime;  // fin de la traduction(durée hh:mm:ss)

	private String language; //langue de traduction

	private Movie movie; // le film concerné par la traduction

	private String filename; // nom fichier SRT à traduire

	private int startMillis; 
	private int endMillis;
	
	

	// ======================================
	// = Getters & setters =
	// ======================================

	public LocalTime getStartTime() {
		return startTime;
		
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
		
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (content != null && !content.equals(" "))
			this.content = content.trim();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentTranslate() {
		return contentTranslate;
	}

	public void setContentTranslate(String contentTranslate) {
		this.contentTranslate = contentTranslate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		try {
			this.language = Language.valueOf(language.toUpperCase()).name();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	

	public int getStartMillis() {
		return startMillis;
	}

	public void setStartMillis(int startMillis) {
		
		this.startMillis = startMillis;
	}
	
	

	public int getEndMillis() {
		return endMillis;
	}

	public void setEndMillis(int endMillis) {
		this.endMillis = endMillis;
	}

	// ======================================
	// = toString & equals & hash =
	// ======================================
	@Override
	public String toString() {
		return "Subtitle {" + "Id='" + this.id + '\'' + ", startTime='" + startTime + '\'' + "," + " endTime='" + endTime
				+ '\'' 
				+ " Content Trans='" + contentTranslate + '\''
				+ " Content='" + content + '\''
				+ " start millis ='" + startMillis + '\''
				+ " end millis ='" + endMillis + '\''
				+ '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Subtitle subtitle = (Subtitle) o;
		return id == subtitle.id;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.id;
	}

}
