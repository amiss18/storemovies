package com.subtitlor.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.subtitlor.beans.Movie;
import com.subtitlor.beans.Subtitle;
import com.subtitlor.exceptions.DaoException;

/**
 * Dao des sous-titres.
 * Cette classe manipule les tables des sous-titres(subtitles et translates)
 * et effectue le mapping entre les données extraites de base la
 * et les beans Subtitle et Movie
 * 
 * @author armel
 *
 */
public class SubtitleDaoImpl implements SubtitleDao {

	private static Logger logger = Logger.getLogger(SubtitleDaoImpl.class.getName());

	private DaoFactory daoFactory;

	public SubtitleDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Optional<Subtitle> get(int id) {

		return null;
	}

	/**
	 * Liste les sous d'un titre d'un film à partir de son id et sa langue
	 * 
	 */
	public List<Subtitle> findBy( Movie movie, String language ) {
		
		
		String SELECT_ID = "SELECT m.filename, m.releaseDate, m.title, m.genre, "
				+ " m.synopsis, s.id AS suid, s.content, t.contentTranslate, s.endTime, "
				+ " s.startTime, s.startMillis, s.endMillis, t.movie_id,"
				+ " t.language"				
				+ " FROM movie AS m"
				+ " JOIN subtitle AS s ON m.id = s.movie_id "
				+ " JOIN translate AS t ON t.subtitle_id = s.id "
				+ " WHERE t.movie_id=?"
				+ " AND t.language LIKE ? ";
		List<Subtitle> subtitles = new ArrayList<>();

		try (
				Connection connection = daoFactory.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_ID);
						) {
			ps.setInt(1, movie.getId() );
			ps.setString(2, language );
			
			try ( ResultSet rs = ps.executeQuery()) {
				
					
					while (rs.next()) {
						Subtitle subtitle = extractData(rs);
					subtitles.add(subtitle);
				}

			}
						
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return subtitles;

	}
	

	

	@Override
	public List<Subtitle> getAll() {
		return null;
	}

	@Override
	public void save(Subtitle subtitle) {

	}

	/**
	 * Edition des sous-tires
	 * 
	 * Mise à jour des sous-titres s'ils sont déjà présent dans la base de données
	 * sinon insère des nouveaux titres en base.
	 */
	@Override
	public void save(List<Subtitle> subtitles ) throws DaoException {

		//insère ou update des sous-titres en langue d'origine
		String sql_insert_or_update = "INSERT INTO subtitle"
				+ " ( id, content,  startTime, endTime, movie_id, startMillis, endMillis) "
				+ " VALUES(?, ?, ?, ?, ?, ?, ? )  ON DUPLICATE KEY UPDATE id=VALUES(id), "
				+ " content=VALUES(content), startTime=VALUES(startTime),  endTime=VALUES(endTime),"
				+ " movie_id=VALUES(movie_id), startMillis=VALUES(startMillis),  endMillis=VALUES(endMillis)";
		
		// insère ou update des sous-titres traduits
		String SQL_INSERT_TRANSLATE_TABLE="INSERT INTO translate "
				+ " (movie_id, subtitle_id, contentTranslate, language)"
				+ " VALUES( ?, ?, ?, ? ) ON DUPLICATE KEY UPDATE "
				+ " movie_id=VALUES(movie_id), subtitle_id=VALUES(subtitle_id), "
				+ "contentTranslate=VALUES(contentTranslate) , language=VALUES(language)";
		
		try (Connection connection = daoFactory.getConnection(); ) {
			int status1=0, status2=0;
			
			// lancement de la transaction
            connection.setAutoCommit(false); // default true
            
            try (
    				PreparedStatement ps1 = connection.prepareStatement(sql_insert_or_update);
    				PreparedStatement ps2 = connection.prepareStatement(SQL_INSERT_TRANSLATE_TABLE);

    		) {
			for ( Subtitle subtitle : subtitles) {			
				//insertion table subtile
				ps1.setInt(1, subtitle.getId());
				ps1.setString(2, subtitle.getContent());
				ps1.setObject(3, subtitle.getStartTime() );
				ps1.setObject(4, subtitle.getEndTime());
				ps1.setInt(5, subtitle.getMovie().getId());
				ps1.setInt(6, subtitle.getStartMillis() );
				ps1.setInt(7, subtitle.getEndMillis() );
				status1 = ps1.executeUpdate();
				
				// Insertion table translate
				ps2.setInt(1, subtitle.getMovie().getId() );
				ps2.setInt(2, subtitle.getId());
				ps2.setString(3, subtitle.getContentTranslate());
				ps2.setString(4, subtitle.getLanguage());
				
				status2 = ps2.executeUpdate();
			}
			
					
            }catch (SQLException e) {
            	//annule la transaction
            	connection.rollback();
    			logger.log(Level.SEVERE, "### Impossible d'ajouter des lignes dans la BD ###");
    			e.printStackTrace();
    			throw new DaoException("Impossible d'ajouter des lignes dans la base de données");
    		}
							
			// fin de la transaction, on commit les modifs
            connection.commit();
            
			logger.log(Level.INFO,"### STATUS REQUETE INSERT: status1="+ status1 + ", status2=" +status2 );

            // good practice to set it back to default true
            connection.setAutoCommit(true);

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "### Impossible de communiquer avec la BD ###");
			e.printStackTrace();
			throw new DaoException("Impossible de communiquer avec la base de données");
		}
	}

	@Override
	public void update(Subtitle subtitle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Subtitle subtitle) {
		// TODO Auto-generated method stub

	}

	private Subtitle extractData(ResultSet rs) throws SQLException {

		Movie m = new Movie();
		
		m.setId(rs.getInt("movie_id"));
		
		
		m.setFilename(rs.getString("filename"));
		m.setReleaseDate(rs.getObject("releaseDate", LocalDate.class));
		m.setSynopsis(rs.getString("synopsis"));
		m.setTitle(rs.getString("title"));
		m.setGenre(rs.getString("genre"));
		//m.setOriginalLanguage(rs.getString("originalLanguage"));
		


		
		
		Subtitle subtitle = new Subtitle();

		subtitle.setId(rs.getInt("suid"));
		subtitle.setContent(rs.getString("content"));
		subtitle.setContentTranslate(rs.getString("contentTranslate"));
		subtitle.setEndTime(rs.getObject("endTime", LocalTime.class));
		subtitle.setStartTime(rs.getObject("startTime", LocalTime.class));
		subtitle.setLanguage(rs.getString("language"));
		subtitle.setStartMillis( rs.getInt("startMillis"));
		subtitle.setEndMillis(rs.getInt("endMillis"));
		
		
		

		subtitle.setMovie(m);
		m.addSubtitle(subtitle);

		return subtitle;

	}

}
