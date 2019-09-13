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
public interface MovieDao {

	Optional<Movie> get( int id) throws DaoException; 
	//Optional<Movie> findMovieBy( int id); 
	
    List<Movie> getAll();    
    
    void save( Movie movie );
    
    //void save(  List<Subtitle> subtitles );
     
    void update( Movie movie );
     
    void delete(Movie movie );
	
}
