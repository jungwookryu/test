CREATE DATABASE  IF NOT EXISTS `HT_IOT_CNND_HOME` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `HT_IOT_CNND_HOME`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 192.168.2.112    Database: HT_IOT_CNND_HOME
-- ------------------------------------------------------
-- Server version	5.5.50-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(128) NOT NULL DEFAULT 'uuid' COMMENT '유저아이디',
  `username` varchar(128) DEFAULT NULL COMMENT '유저이메일',
  `password` varchar(255) DEFAULT NULL COMMENT '로그인비밀번호',
  `password_set` char(1) DEFAULT 'N' COMMENT '비밀번호 생성여부',
  `nick_name` varchar(32) DEFAULT NULL COMMENT '닉네임',
  `token` varchar(255) DEFAULT NULL COMMENT '사용자 토큰정보',
  `status` tinyint(1) DEFAULT '1' COMMENT '회원:1, 공유대기:2, 인증대기:0',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '사용자 생성시간',
  `lastmodified_time` timestamp NULL DEFAULT NULL COMMENT '변경일시',
  `active` int(11) DEFAULT NULL,
  `admin` int(11) DEFAULT NULL,
  `authority` int(11) DEFAULT '0' COMMENT '권한',
  `authorities` varchar(45) DEFAULT NULL COMMENT '권한들',
  `authority_code` varchar(45) DEFAULT NULL COMMENT '권한들',
  `locale` varchar(20) DEFAULT '1' COMMENT '출처',
  `redirectied_code` varchar(45) DEFAULT NULL COMMENT '인증코드',
  `user_email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='사용자';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-13  9:33:11
