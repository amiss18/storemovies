package com.subtitlor.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.subtitlor.beans.Movie;
import com.subtitlor.beans.Subtitle;

/**
 * classe utilitaire.
 * Parse le fichier SRT et charge son contenu dans 2 listes
 * (liste des sous-titres et liste des traduction)
 * Les données de ce fichier sont mappées dans les objets Movie et Subtitle
 * 
 * @author armel
 *
 */

public class SubtitlesHandler {

	private List<Subtitle> subtitles = null;
	
	private ArrayList<Subtitle> translatedSubtitles = null;
	
	private Movie movie;
	
	//fichier de traduction
	private String filename;
	
	/**
	 * chargement du contenu du fichier src dans la liste
	 * 
	 * @param fileName
	 * @param subTitle
	 */
	public SubtitlesHandler() {
		subtitles = new ArrayList<>();
		translatedSubtitles = new ArrayList<>();
	}
	
	public SubtitlesHandler( Movie movie, String filename ) {
		this.movie = movie;
		this.filename = filename;
		subtitles = new ArrayList<>();
		translatedSubtitles = new ArrayList<>();
		//readFile( movie.getFilename() );
	}
	
	public ArrayList<Subtitle> getTranslatedSubtitles() {
		return translatedSubtitles;
	}

	public void setTranslatedSubtitles(ArrayList<Subtitle> translatedSubtitles) {
		this.translatedSubtitles = translatedSubtitles;
	}

	public static final String NEW_LINE = System.getProperty("line.separator");


	private static Logger logger = Logger.getLogger(SubtitlesHandler.class.getName());

	// Logger.getLogger(this.getClass().getSimpleName());

	

	/**
	 * Lecture du fichier et chargement de son contenu dans la liste
	 * 
	 * @param fileName
	 * @param subTitle
	 */
	public void readFile() {
	//	String fileName = movie.getFilename();
		try (BufferedReader br = new BufferedReader(new FileReader( filename ));) {

			logger.log(Level.INFO, "Démarrage de lecture du fichier " + filename );

			String line;
			while ((line = br.readLine()) != null) {
				
				Subtitle subTitle = new Subtitle();
				subTitle.setFilename( filename );
				
				String nextLine=null;
				StringBuilder srt = new StringBuilder();

				// si la ligne parcourue correspond au nombre alors il s'agit de l'id
				if (isNumeric(line)) {
					try {
						int id = Integer.parseInt(line.trim());
						subTitle.setId(id);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						logger.log(Level.WARNING, "Impossible de caster l'ID en nombre  ");
					}

				} 
				
				// On avance d'une ligne pour récupérer la durée
				nextLine = br.readLine();
				// matche "hh:mm:ss,ms --> hh:mm:ss,ms" qui correspond à la durée
				if (isTime( nextLine )) { 

					//éclatement de la durée en 2 parties: debut et fin
					String[] duruation = nextLine.split("-->");
					
					//début de la durée de traduction
					subTitle.setStartTime(parseStringToLocalTime(duruation[0].trim()));
					
					//millisecond correspondant au début de la traduction
					subTitle.setStartMillis( extractMillis( duruation[0].trim().split("\\,") ));
					
					
					//fin de la durée de traduction
					subTitle.setEndTime( parseStringToLocalTime(duruation[1].trim() ));
					
					///millisecondes correspondant à la fin du message de traduction
					subTitle.setEndMillis( extractMillis( duruation[1].trim().split("\\,") ));

				} 
				
				/**
				 * le texte à traduire étant sur plusieurs lignes, 
				 * on boucle sur ces lignes jusqu'à la prochaine ligne vide 
				 */
				for( nextLine  = br.readLine(); nextLine!=null && !nextLine.isEmpty(); nextLine=br.readLine() ){
                    srt.append(nextLine+"\n");                    
                }
				subTitle.setContent(srt.toString());
				
				//ajout des Objets sous-titres dans la liste
				subtitles.add(subTitle);
				
				

			}
			translatedSubtitles.addAll(subtitles);
			logger.log(Level.INFO, "Fin de lecture du fichier" + filename);
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Problème de lecture du fichier " + filename);
		}

	}
	
	/**
	 * Création et écriture du fichier des sous-titres
	 * @param filename
	 * @param language
	 */
	/*
	public void writeSubtitles(List<Subtitle> subtitles,  String language) {
		String file=generateSubtitleFileName(filename, language);
    
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter( file ))) {
			for(Subtitle subtitle: subtitles) {
				if( subtitle!=null) {
					String startTime = subtitle.getStartTime().toString() + "," + subtitle.getStartMillis();
					String endTime = subtitle.getEndTime().toString() + "," + subtitle.getEndMillis();
					String periode = startTime + " --> " + endTime;

					StringBuilder sb = new StringBuilder();

					sb.append( subtitle.getId() + "\n");
					sb.append( periode + "\n");
					sb.append( subtitle.getContentTranslate() + "\n");

					bw.write(sb.toString());
					//ligne vide après chaque section de traduction
					bw.newLine();
				}
			}
			
			logger.log(Level.INFO, "###Fin écriture dans le fichier SRT ###");
		} catch (IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "###Impossible d'écrire dans le fichier###");
		}

		// });
	}
	*/
	
	
	
	


	/**
	 * Vérifie qu'un String est un nombre
	 * 
	 * @param strNum
	 * @return
	 */
	public static boolean isNumeric(String strNum) {
		return strNum.matches("-?\\d+(\\.\\d+)?");
	}

	/**
	 * Vérification du format de la durée du texte à traduire dans le fichier.
	 * 
	 * Format attendu:    hh:mm:ss.ms --> hh:mm:ss.ms
	 * 
	 * @param strNum durée de traduction
	 * @return
	 */
	public static boolean isTime(String strNum) {
		return strNum.matches("[\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3}.*[\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3}");
	}

	/**
	 * converti une string en LocalTime
	 *
	 * @param time
	 * @return LocalTime  le temps en chaîne de caractères
	 */
	public static LocalTime parseStringToLocalTime(String time) {

		// éclatement de la durée en 2 parties à savoir "hh:mm:ss" et "millisecondes"
		String[] temp = time.split(",");
		LocalTime.parse(temp[0]); // hh:mm:ss
		return LocalTime.parse(temp[0]);
	}
	
	
	/**
	 * conversion d'une chaîne en millisecondes
	 * @param tab
	 * @return nombre de millisecondes
	 */
	public static int extractMillis( String[] tab ) {
		int millis =0;		
		if( tab!=null && tab.length >0 ) {
			try {
				millis = Integer.parseInt( tab[1].trim());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return millis;
			}
		}
		
		return millis;
	}
	

	public List<Subtitle> getSubtitles() {
		return subtitles;
	}

	public void setSubtitles(List<Subtitle> subtitles) {
		this.subtitles = subtitles;
	}
	
	

}
