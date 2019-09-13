/**
 * 
 */
package com.subtitlor.bdd;

import java.util.List;
import java.util.Optional;

import com.subtitlor.beans.Movie;
import com.subtitlor.beans.Subtitle;
import com.subtitlor.exceptions.DaoException;


/**
 * @author armel
 *
 */
public interface SubtitleDao {

	/**
	 * recupère un sous-titre
	 * 
	 * @param id
	 * @return
	 */
	Optional<Subtitle> get( int id);    
	
	/**
	 * Liste tous les sous-titre
	 * 
	 * @return List
	 */
    List<Subtitle> getAll();    
    
    /**
	 * Liste tous les sous-titre
	 * 
	 * @return List
	 */
    List<Subtitle> findBy( Movie movie, String language );
    
    void save( Subtitle subtitle );
    
    /**
     * sauvergarde des sous-titres s'ils n'existent pas 
     * sinon met à jour les sous-tres
     * 
     * @param subtitles
     * @throws DaoException 
     */
    void save(  List<Subtitle> subtitles ) throws DaoException;
     
    void update( Subtitle subtitle );
     
    void delete(Subtitle subtitle );
}
