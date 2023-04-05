DROP DATABASE  IF EXISTS `gamification`;

CREATE DATABASE  IF NOT EXISTS `gamification`;
USE `gamification`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `user_detail`;


CREATE TABLE `user_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `display_name` varchar(50) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `badge_id` int(11) DEFAULT NULL,
  `tokens` int(11) NOT NULL,
  `quests` int(11) NOT NULL,
  `image` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` char(80) NOT NULL,
  `email` varchar(50) NOT NULL,
  `enabled` int(1) DEFAULT NULL,
  `suspended` int(1) DEFAULT NULL,
  `email_token` varchar(30) DEFAULT NULL,
  `password_token` varchar(30) DEFAULT NULL,
  `confirmation_token` varchar(50) DEFAULT NULL,
  `user_detail_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_DETAIL_id1` (`user_detail_id`),
  CONSTRAINT `FK_DETAIL` FOREIGN KEY (`user_detail_id`) REFERENCES `user_detail` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



INSERT INTO `role` (name)
VALUES 
('ROLE_USER'),('ROLE_ADMIN');


DROP TABLE IF EXISTS `badge`;

CREATE TABLE `badge` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `picture` varchar(50) DEFAULT NULL,

  PRIMARY KEY (`id`)

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `quest`;

CREATE TABLE `quest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `answer` varchar(500) DEFAULT NULL,
  `tokens` int(11) NOT NULL,
  `badge_id` int(11) DEFAULT NULL,
  `approved` int(1) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,

  PRIMARY KEY (`id`),

  KEY `FK_USER_ID_id3` (`user_id`),

  CONSTRAINT `FK_USER_QUEST` 
  FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 

  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `submission`;

CREATE TABLE `submission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `quest_id` int(11) NOT NULL,
  `answer` varchar(500) DEFAULT NULL,
  `status` int(1) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `submit_id` int(11) DEFAULT NULL,

  PRIMARY KEY (`id`),

  KEY `FK_USER_ID_id4` (`user_id`),

  CONSTRAINT `FK_USER_SUBMISSION` 
  FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 


  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `users_roles`;

CREATE TABLE `users_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  
  PRIMARY KEY (`user_id`,`role_id`),
  
  KEY `FK_ROLE_idx` (`role_id`),
  
  CONSTRAINT `FK_USER_05` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) 
  REFERENCES `role` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `users_badges`;

CREATE TABLE `users_badges` (
  `user_id` int(11) NOT NULL,
  `badge_id` int(11) NOT NULL,
  
  PRIMARY KEY (`user_id`,`badge_id`),
  
  KEY `FK_BADGE_idx` (`badge_id`),
  
  CONSTRAINT `FK_USER_06` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  CONSTRAINT `FK_BADGE` FOREIGN KEY (`badge_id`) 
  REFERENCES `badge` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;

