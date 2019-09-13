package com.subtitlor.bdd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DaoFactory {

	private static String url;
	private static String username;
	private static String password;
	private static String driver;
	
	
	
     // WEB-INF/classes/db.properties
	//informations de config de la BD
	 private static final String PROPERTIE_FILE       = "db.properties";
	
	
	private static Logger logger = Logger.getLogger(DaoFactory.class.getName());

	/*
	  public DaoFactory(String url, String username, String password) { 
		  this.url = url; 
		  this.username = username; 
		  this.password = password;
	  }
	 */
	

	/**
	 *  Récupéreration des informations de connexion à la base de
     *  données et chargement du driver JDBC 
	 *  
	 * @param filname   nom du fichier
	 */
	public DaoFactory( ) {
		Properties prop = new Properties();
		ClassLoader classLoader = getClass().getClassLoader();
    	
    	//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();    	   	
    	
    	try(
    		InputStream inputStream = classLoader.getResourceAsStream(PROPERTIE_FILE); 
    			){
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("Le fichier est" + PROPERTIE_FILE + "introuvable");
			}
    		prop.load(inputStream);   		
    		
            url = prop.getProperty("db.url");
            username = prop.getProperty("db.username");
            password = prop.getProperty("db.password");
            driver = prop.getProperty("db.driver.class");
            logger.log(Level.INFO, "### Chargement du fichier de config de la BD ###");
           
            
    	}catch( IOException e) {
    		logger.log(Level.SEVERE, String.format("### Le fichier %s est introuvable ###", PROPERTIE_FILE));
    		e.printStackTrace();  		
    	}
    	
    	//chargement du driver du moteur de la BD
    	try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, String.format("### Classe %s non trouvée ###", driver));
		}
    	
	}
	
	 /**
	  * Méthode retournant une instance de la Factory
	  * 
	  * @return
	  */
	public static DaoFactory getInstance() {			
		DaoFactory instance = new DaoFactory( );
		return instance;
	}
	
	/**
	 * Méthode chargée de fournir une connexion à la base de donnée
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		
		return DriverManager.getConnection(url, username, password);
	}
	
	
	/**
	 *  Récupération de l'implémentation MovieDao
	 * @return MovieDao
	 */
	 public MovieDao getMovieDao() {
		
		return new MovieDaoImpl(this);
	}
	 
	/**
	 *  Récupération de l'implémentation SubtitleDao
	 * @return SubtitleDao
	 */
	 public SubtitleDao getSubtitleDao() {
		
		return new SubtitleDaoImpl(this);
	}

}
