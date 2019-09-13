package com.subtitlor.utilities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

import com.subtitlor.exceptions.BeanException;

public class Dates {

    
    /**
     * converti une chaîne en LocalDate
     * 
     * @param str datetime ( ex: 2000-11-19 )
     * @return LocalDateTime
     */
    public static LocalDate parseLocaleDate( String str) throws DateTimeParseException {
    	
    	LocalDate date=null;
    	
    	try {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            date = LocalDate.parse(str, formatter);
          //  LocalDate.p
		} catch (DateTimeParseException  e) {
			throw new BeanException("La date entrée n'est pas valide");
		}
    	
    	return date;
    }
}
