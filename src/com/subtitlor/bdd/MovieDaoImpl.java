package com.subtitlor.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.subtitlor.beans.Movie;
import com.subtitlor.beans.Subtitle;
import com.subtitlor.exceptions.BeanException;
import com.subtitlor.exceptions.DaoException;

/**
 * Dao des sous-titres.
 * Cette classe manipule la table des films(movie).
 * Elle effectue le mapping entre les données extraites de la base de données
 * et les objets Movie et Subtitle
 * 
 * @author armel
 *
 */

public class MovieDaoImpl implements MovieDao {
	
	private static Logger logger = Logger.getLogger(MovieDaoImpl.class.getName());


   private DaoFactory daoFactory;

	public MovieDaoImpl( DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	

	/**
	 *  Sélection un film à partir de son id
	 */
	@Override
	public Optional<Movie> get(int id) throws DaoException {
		String SELECT_ID="SELECT * FROM movie WHERE id = ?";
		
		Movie m = new Movie();
		try (
				Connection	connection = daoFactory.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ID);
				
				){
				ps.setInt(1, id);
							
				try( 
					 ResultSet rs = ps.executeQuery();  ) {
					 rs.next();
					 m.setId( rs.getInt("id") );
					 m.setFilename( rs.getString("filename" ) );
					 m.setReleaseDate( rs.getObject("releaseDate", LocalDate.class) );
					 m.setSynopsis( rs.getString("synopsis" ) );
					 m.setTitle( rs.getString("title" )  );
					 m.setGenre( rs.getString("genre") );
					 m.setOriginalLanguage( rs.getString("originalLanguage") );
				   
				}catch (SQLException e) {
					e.printStackTrace();
					logger.log(Level.WARNING, "### Impossible de lire la ligne de la table movie  ###");
					throw new DaoException("Impossible de lire la ligne de la table movie");
				}
				
				
		    	
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "### Impossible de communiquer avec la BD ###");
			throw new DaoException("Impossible de communiquer avec la BD");
		}
		return Optional.ofNullable(m);
	}

	/**
	 * Liste tous les films
	 */
	@Override
	public List<Movie> getAll() {
		String SELECT_ALL="SELECT * FROM movie";
		List<Movie> movies = new ArrayList<>();
		
			try (
					Connection	connection = daoFactory.getConnection();
					Statement st = connection.createStatement();
					ResultSet rs = st.executeQuery(SELECT_ALL)
					){
				
				while ( rs.next() ) {					
					Movie movie = extractData(rs);
					movies.add(movie);				
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				logger.log(Level.SEVERE, "### Impossible de communiquer avec la BD ###");
				
			}
		
		return movies;
	}

	/**
	 * Insère plusieurs sous-titres s'ils sont déjà présent en BD
	 * sinon met à jour les sous-titres existants
	 * 
	 * @param movie
	 */
	/*
	public void addSubtitles(Movie movie) {
		String sql_insert_or_update = "INSERT INTO subtitle( id, content, contentTranslate, startTime, endTime, movie_id,language) "+
				" VALUES(?, ?, ?, ?, ?, ? ) ON DUPLICATE KEY UPDATE id=VALUES(id),  content=VALUES(content),"
				+ " contentTranslate=VALUES(contentTranslate), startTime=VALUES(startTime), movie_id=VALUES(movie_id), language=VALUES(language)";

		try (
				Connection	connection = daoFactory.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql_insert_or_update);
				
				){
			    for( Subtitle subtitle: movie.getSubtitles() ) {
			    	ps.setInt(1, subtitle.getId() );
			    	ps.setString( 2, subtitle.getContent());
			    	ps.setString( 3, subtitle.getContentTranslate() );
			    	ps.setObject( 4, subtitle.getStartTime());
			    	ps.setObject( 5, subtitle.getEndTime());
			    	ps.setInt(6, movie.getId() );
			    	ps.setString( 7, subtitle.getLanguage() );
			    	ps.executeUpdate();
			    }
				
		    	
			logger.log(Level.INFO, "### Exécution de la requête d'insertion ###");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "### Problème requête INSERT à la BDD ###");
		}
	}
	*/
	/**
	 * Insère un film
	 */
	@Override
	public void save(Movie movie) {
		String sql_insert = "INSERT INTO movie( id, title, filename, synopsis, releaseDate, genre, originalLanguage ) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

		try (
				Connection	connection = daoFactory.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql_insert);
				
				){
				
				ps.setInt( 1,  movie.getId() );
				ps.setString( 2,  movie.getTitle() );
				ps.setString( 3,  movie.getFilename() );
				ps.setString( 4,  movie.getSynopsis() );
				ps.setDate(5,  java.sql.Date.valueOf( movie.getReleaseDate()) );
				ps.setString( 6,  movie.getGenre() );
				ps.setString( 7,  movie.getOriginalLanguage() );
			
			int status=	ps.executeUpdate();
			if( status == 0 ) {
				logger.log(Level.INFO, "### Echec de la création du film, ###");
				throw new BeanException("Echec de la création du film, aucune ligne ajoutée dans la table");
			}
		    	
			logger.log(Level.INFO, "### Exécution de la requête d'insertion d'un film ###");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.WARNING, "### Impossible de communiquer avec la BDD ###");
			throw new BeanException("Impossible de communiquer avec la BD");
			
		}
		
	}

	@Override
	public void update(Movie movie) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Movie movie) {
		// TODO Auto-generated method stub
		
	}
	
	
	private Movie extractData(ResultSet rs) throws SQLException {

		Movie m = new Movie();
		
		 m.setId( rs.getInt("id") );
		 m.setFilename( rs.getString("filename" ) );
		 m.setReleaseDate( rs.getObject("releaseDate", LocalDate.class) );
		 m.setSynopsis( rs.getString("synopsis" ) );
		 m.setTitle( rs.getString("title" )  );
		 m.setGenre( rs.getString("genre") );
		 m.setOriginalLanguage( rs.getString("originalLanguage") );
		 
		 /*
		
		 Subtitle subtitle = new Subtitle();
		 
		 subtitle.setId( rs.getInt("id" ) );
		 subtitle.setContent( rs.getString("content" ) );
		 subtitle.setContentTranslate( rs.getString("contentTranslate" ) );
		 subtitle.setEndTime( rs.getObject("endTime", LocalTime.class));
		 subtitle.setStartTime( rs.getObject("startTime", LocalTime.class));
		 subtitle.setLanguage( rs.getString("language") );
		 
		 m.addSubtitle(subtitle);
		 */
		 
		 
	    return m;

	}

}
