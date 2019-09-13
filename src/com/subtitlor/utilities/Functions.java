package com.subtitlor.utilities;

/**
 * Interprète les retours à la ligne du texte
 * en remplaçant \n par la balise <br/>
 * 
 * @author armel
 *
 */
public final class Functions {

	
	private Functions() {}

    public static String nl2br(String string) {
        return (string != null) ? string.replace("\n", "<br/>") : null;
    }
    
    /**
     * découpe un texte
     * @param input
     * @param size
     * @return
     */
    public static String truncate(String input, int size ) {
    	if (input.length() < size-3) return input;
        
    	return  input !=null ? input.substring(0, size-3):"";
    }
}
