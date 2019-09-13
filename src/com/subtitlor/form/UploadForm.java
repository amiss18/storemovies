package com.subtitlor.form;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.subtitlor.beans.FileBean;
import com.subtitlor.beans.Movie;

public class UploadForm {
	

    private static final String INPUT_FILE     = "file"; // champ fichier
    private static final int    DEFAULT_BUFFER_SIZE     = 10240;        // 10 ko
    
    private static final String CONTENT_DISPOSITION = "content-disposition";
    private static final String CONTENT_DISPOSITION_FILENAME = "filename";
    
    private static final String CONTENT_TYPE = "application/x-subrip";
    
    private static final long MAX_SIZE_UPLOAD= 1024 * 1024 * 10 * 10;//100Mo
    
    private String              result;

    private Map<String, String> errors           = new HashMap<>();

    //langue d'origine d'origine du film
   // private String  language;
    /*
	public UploadForm(HttpServletRequest request ) throws IOException, ServletException {
		if( request.getPart("file").getSize() ==0 ) {
			setErrors(INPUT_FILE, "+Veuillez sélectionner un fichier srt");
		}
	}
	*/

    /**
     * Validation du fichier uploadé selon son volume, son type
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     */
	public boolean validate(HttpServletRequest request ) throws IOException, ServletException {
		if( request.getPart("file").getSize() ==0 ) {
			setErrors(INPUT_FILE, "*Veuillez sélectionner un fichier srt");
			return false;
		}
		
		if( request.getPart("file").getSize() >= MAX_SIZE_UPLOAD ) {
			setErrors(INPUT_FILE, "*Fichier trop volumineux");
			return false;
		}
		//le fichier envoyé n'est pas de type srt
		if( !request.getPart("file").getContentType().contains( CONTENT_TYPE) ) {
			setErrors(INPUT_FILE, "*Veuillez sélectionner un fichier SRT valide");
			return false;
		}
		
		return true;
	}
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public Map<String, String> getErrors() {
        return errors;
    }
	
	

	/**
	 * Ajoute un message correspondant au champ spécifié à la map des erreurs.
	 * 
	 * @param field 	champ du formulaire
	 * @param message	message d'erreurs
	 */
    private void setErrors( String field, String message ) {
        errors.put( field, message );
    }
    

    

    /**
     * Méthode utilitaire qui retourne null si un champ est vide, et son contenu
     * sinon.
     * @param request
     * @param field
     * @return
     */
    private static String getValue( HttpServletRequest request, String field ) {
        String value = request.getParameter( field );
        if ( value == null || value.trim().length() == 0 ) {
            return null;

        } else {

            return value;

        }
    }
    
    
/**
 * 
 * 
 * Méthode utilitaire qui a pour unique but d'analyser l'en-tête
 * "content-disposition", et de vérifier si le paramètre "filename" y est
 * présent. Si oui, alors le champ traité est de type File et la méthode
 * retourne son nom, sinon il s'agit d'un champ de formulaire classique et
 * la méthode retourne null.
 * 
 * @param part
 * @return
 */
    private String getFilename(Part part) {
	
        for (String cd : part.getHeader(CONTENT_DISPOSITION).split(";")) {
            if (cd.trim().startsWith(CONTENT_DISPOSITION_FILENAME)) {
            	/*
                 * Si "filename" est présent, alors renvoi de sa valeur,
                 * c'est-à-dire du nom de fichier sans guillemets.
                 */
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        // Et pour terminer, si rien n'a été trouvé... 
        return null;
        
        
       
    }

    
    
    
    /**
     *  
     * @param request
     * @param chemin
     * @return
     */
    public FileBean saveFile( HttpServletRequest request, String chemin ) {
    	
    	 /* Initialisation du bean représentant un fichier */

        FileBean fichier = new FileBean();
        
        



        /*
         * Récupération du contenu du champ fichier du formulaire. Il faut ici
         * utiliser la méthode getPart(), comme nous l'avions fait dans notre
         * servlet auparavant.
         */

        String nomFichier = null;

        InputStream contenuFichier = null;
        

        try {
            Part part = request.getPart( INPUT_FILE );
            
           
            
            
            /*
             * Il faut déterminer s'il s'agit bien d'un champ de type fichier :
             * on délègue cette opération à la méthode utilitaire
             * getNomFichier().
             */

            nomFichier = getFilename( part );

            /*
             * Si la méthode a renvoyé quelque chose, il s'agit donc d'un
             * champ de type fichier (input type="file").
             */

            if ( nomFichier != null && !nomFichier.isEmpty() ) {
                /*
                 * Antibug pour Internet Explorer, qui transmet pour une
                 * raison mystique le chemin du fichier local à la machine
                 * du client...
                 * Ex : C:/dossier/sous-dossier/fichier.ext
                 * On doit donc faire en sorte de ne sélectionner que le nom

                 * et l'extension du fichier, et de se débarrasser du superflu.
                 */

                nomFichier = nomFichier.substring( nomFichier.lastIndexOf( '/' ) + 1 )

                        .substring( nomFichier.lastIndexOf( '\\' ) + 1 );


                /* Récupération du contenu du fichier */
                contenuFichier = part.getInputStream();
            }

        } catch ( IllegalStateException e ) {
        /*
         * Exception retournée si la taille des données dépasse les limites
         * définies dans la section <multipart-config> de la déclaration de
         * notre servlet d'upload dans le fichier web.xml
         */
            e.printStackTrace();
            setErrors( INPUT_FILE, "Les données envoyées sont trop volumineuses." );
        } catch ( IOException e ) {

            /*
             * Exception retournée si une erreur au niveau des répertoires de
             * stockage survient (répertoire inexistant, droits d'accès
             * insuffisants, etc.)
             */

            e.printStackTrace();

            setErrors( INPUT_FILE, "Erreur de configuration du serveur." );

        } catch ( ServletException e ) {

            /*
             * Exception retournée si la requête n'est pas de type
             * multipart/form-data. Cela ne peut arriver que si l'utilisateur
             * essaie de contacter la servlet d'upload par un formulaire
             * différent de celui qu'on lui propose... pirate ! :|
             */
            e.printStackTrace();
            setErrors( INPUT_FILE,

                    "Ce type de requête n'est pas supporté, merci d'utiliser le formulaire prévu pour envoyer votre fichier." );
        }


        /* Si aucune erreur n'est survenue jusqu'à présent */

        if ( errors.isEmpty() ) {
            /* Validation du champ fichier. */

            try {
                validationFichier( nomFichier, contenuFichier );
                nomFichier=generateUniqueFileName(nomFichier);

            } catch ( Exception e ) {
                setErrors( INPUT_FILE, e.getMessage() );
            }
            //==========================change nom
            fichier.setName( nomFichier );

        }


        // Si aucune erreur n'est survenue jusqu'à présent 
        if ( errors.isEmpty() ) {

            /* Écriture du fichier sur le disque */
            try {
                writeFile( contenuFichier, nomFichier, chemin );
            } catch ( Exception e ) {
                setErrors( INPUT_FILE, "Erreur lors de l'écriture du fichier sur le disque." );

            }

        }


        /* Initialisation du résultat global de la validation. */

        if ( errors.isEmpty() ) {
            result = "Succès de l'envoi du fichier.";
        } else {
            result = "Échec de l'envoi du fichier.";
        }

        return fichier;
    }
    
   
   
    /**
     *  Méthode utilitaire qui a pour but d'écrire le fichier passé en paramètre
     * sur le disque, dans le répertoire donné et avec le nom donné.
     * 
     * @param contenu
     * @param nomFichier
     * @param chemin
     * @throws Exception
     */
    private void writeFile( InputStream contenu, String nomFichier, String chemin ) throws Exception {

        try (  
        		/* Ouvre les flux. */
        		BufferedInputStream entree = new BufferedInputStream( contenu, DEFAULT_BUFFER_SIZE );
        		BufferedOutputStream sortie =  new BufferedOutputStream( new FileOutputStream( new File( chemin + nomFichier ) ),
                		DEFAULT_BUFFER_SIZE );
        		){
           //  Lit le fichier reçu et écrit son contenu dans un fichier sur le
          //  disque.
             

            byte[] tampon = new byte[DEFAULT_BUFFER_SIZE];
            int longueur = 0;
            
            while ( ( longueur = entree.read( tampon ) ) > 0 ) {
                sortie.write( tampon, 0, longueur );
            }

        } catch ( IOException ignore ) {

        }

    }
    
    
    /**
     * Valide le fichier envoyé
     * @param nomFichier
     * @param contenuFichier
     * @throws Exception
     */
    private void validationFichier( String nomFichier, InputStream contenuFichier ) throws Exception {
        if ( nomFichier == null || contenuFichier == null  ) {
            throw new Exception( "Merci de sélectionner un fichier à envoyer." );
        }
    }

   
    /**
     *  Génère un nom unique de fichier selon le timestamp.
     *  le préfixe du fichier géneré se termine par "code langue"_"code pays"(fr_FR)
     * @param filename nom du fichier
     * @return 
     */
   public  String generateUniqueFileName( String filename ) {
        long millis = System.currentTimeMillis();
        //position du dernier '.'
        int  index = filename.lastIndexOf("."); 
        //préfixe du fichier
        String file= filename.substring(0, index).trim().replaceAll(" ", ""); //nom du fichier
        String ext = filename.substring(index).trim(); //extension du fichier
        file = file+"_"+millis+ext;
      //String languageCode= language.toLowerCase();
     // String countryCode = language.toUpperCase();
        //file = file+"_"+millis+"."+languageCode+"_"+countryCode+ext;
       // file = replaceLatinCharsToChars( file );
        
        return  file ;
    }
   
   /**
    * remplace les caractères accentués par leurs équivalents unicode sans accents
    * @param input
    * @return
    */
   public  static String replaceLatinCharsToChars(String s) {
	   
	   if(s == null || s.trim().length() == 0)
           return "";
   return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
	   
   }
}
