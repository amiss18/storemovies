# Activité JAVA EE sur le sous-titrage des films

**Manuel d'installation**
* Modifier les paramètres de connexion à la BD dans le fichier `db.properties`(le fichier `db.properties` se trouve dans `/WEB-INF/classes/`)
* Création de la BD et de ses tables
 `mysql -u root -p root < db.sql`
 Bien évidemment vous pouvez chargé la BD avec **phpmyadmin**
 (le fichier `db.sql` se trouve dans la racine du projet)

**Emplacement des fichiers SRT uploadés**
Les fichiers SRT de se trouvent dans le dossier `/WEB-INF/uploadir/`

**Requirements**: Java 11, tomcat 9, MySQL 5.7, firefox 68+ (ou n'importe quel navigateur 
réçent compatible html5)
