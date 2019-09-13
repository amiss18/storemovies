SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS `subtitles_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `subtitles_db`;

DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie` (
  `id` int(8) NOT NULL,
  `title` varchar(250) DEFAULT NULL,
  `filename` varchar(200) NOT NULL,
  `synopsis` text,
  `releaseDate` date NOT NULL,
  `genre` varchar(150) DEFAULT NULL,
  `originalLanguage` varchar(3) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `movie` (`id`, `title`, `filename`, `synopsis`, `releaseDate`, `genre`, `originalLanguage`) VALUES
(1, 'Password presentation', 'password_presentation.srt', 'Fichier SRT d\'exemple fourni avec le projet subtitlor de Mathieu N. d\'Openclassroom.', '2019-09-02', 'Documentaire', 'FR'),
(6, 'Written on the Wind', 'extrait_Written.On.The.Wind.1956_1568222785068.srt', '						 Written on the Wind is a 1956 American Technicolor melodrama film directed by Douglas Sirk and starring Rock Hudson, Lauren Bacall, Robert Stack and Dorothy Malone.\r\n\r\nThe screenplay by George Zuckerman was based on Robert Wilder\'s 1946 novel of the same name, a thinly disguised account of the real-life scandal involving torch singer Libby Holman and her husband, tobacco heir Zachary Smith Reynolds. Zuckerman shifted the locale from North Carolina to Texas, made the source of the family wealth oil rather than tobacco, and changed all the characters\' names. \r\n						', '1956-11-02', 'Comédie', 'EN');

DROP TABLE IF EXISTS `subtitle`;
CREATE TABLE `subtitle` (
  `id` int(8) NOT NULL,
  `content` text,
  `startTime` time NOT NULL,
  `endTime` time NOT NULL,
  `movie_id` int(8) NOT NULL,
  `startMillis` int(10) UNSIGNED NOT NULL,
  `endMillis` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `subtitle` (`id`, `content`, `startTime`, `endTime`, `movie_id`, `startMillis`, `endMillis`) VALUES
(1, 'Je suis professeur d\'informatique et\nd\'ingéniérie informatique', '00:00:00', '00:00:02', 1, 535, 462),
(1, 'Hello.', '00:03:55', '00:03:57', 6, 578, 438),
(2, 'à Carnegie Mellon,', '00:00:02', '00:00:04', 1, 462, 389),
(2, '- What happened to Mrs. Black?\n- My predecessor? She was paroled.', '00:03:58', '00:04:01', 6, 248, 945),
(3, 'So you\'re the new\nexecutive secretary, huh?', '00:04:02', '00:04:04', 6, 52, 418),
(4, 'Don\'t let that title deceive you.\nI do everything but wipe windshields.', '00:04:04', '00:04:08', 6, 521, 218);

DROP TABLE IF EXISTS `translate`;
CREATE TABLE `translate` (
  `movie_id` int(11) NOT NULL,
  `subtitle_id` int(11) NOT NULL,
  `contentTranslate` text,
  `language` varchar(3) NOT NULL DEFAULT 'FR'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `translate` (`movie_id`, `subtitle_id`, `contentTranslate`, `language`) VALUES
(1, 1, 'I am a professor of computer science and\r\ncomputer engineering', 'EN'),
(6, 1, 'Salut', 'FR'),
(1, 2, 'at Carnegie Mellon,', 'EN'),
(6, 2, '- Qu\'est-ce qui est arrivé à Mme Black?\r\n- Mon prédécesseur? Elle était en liberté conditionnelle.', 'FR'),
(6, 3, 'Une nuit de bonheur volé', 'FR'),
(6, 4, 'Donc tu es le nouveau\r\nsecrétaire de direction, hein?', 'FR');


ALTER TABLE `movie`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `fichier_index` (`filename`(155));

ALTER TABLE `subtitle`
  ADD PRIMARY KEY (`id`,`movie_id`),
  ADD UNIQUE KEY `unique_sub` (`id`,`movie_id`),
  ADD KEY `movie_id` (`movie_id`);

ALTER TABLE `translate`
  ADD PRIMARY KEY (`subtitle_id`,`language`,`movie_id`) USING BTREE;


ALTER TABLE `movie`
  MODIFY `id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;


ALTER TABLE `subtitle`
  ADD CONSTRAINT `subtitle_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movie` (`id`) ON DELETE CASCADE;

ALTER TABLE `translate`
  ADD CONSTRAINT `translate_ibfk_1` FOREIGN KEY (`subtitle_id`) REFERENCES `subtitle` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
SET FOREIGN_KEY_CHECKS=1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
