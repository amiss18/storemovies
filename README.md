# Activité JAVA EE sur le sous-titrage des films

Soit on installe l'appli soit on la lance directement avec docker


**Manuel d'installation**


* Modifier les paramètres de connexion à la BD dans le fichier `db.properties`(le fichier `db.properties` se trouve dans `/WEB-INF/classes/`)
* Création de la BD et de ses tables
 `mysql -u root -p root < db.sql`
 Bien évidemment vous pouvez charger la BD avec **phpmyadmin**
 (le fichier `db.sql` se trouve dans la racine du projet)

**Emplacement des fichiers SRT uploadés**
Les fichiers SRT de se trouvent dans le dossier `/WEB-INF/uploadir/`

**Requirements**: Java 11, tomcat 9, MySQL 5.7, firefox 68+ (ou n'importe quel navigateur 
réçent compatible html5)

**Lancer l'application avec Docker**

```
docker-compose up
```
On accède à l'application avec le lien suivant:

[http://localhost:82/storemovies/](http://localhost:82/storemovies/)

tomcat -> 82

mysql  -> 3307

** Lien démo **

[movies.designapp.fr](http://movies.designapp.fr/)

**Exemple de fichier SRT**

```
1
00:03:55,578 --> 00:03:57,438
I am a professor of computer science and
computer engineering

2
00:03:58,248 --> 00:04:01,945
at Carnegie Mellon,
```