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
-- Table structure for table `zwave`
--

DROP TABLE IF EXISTS `zwave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zwave` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `nicname` varchar(45) NOT NULL,
  `updaete_no` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `commd_calss_name` varchar(45) DEFAULT NULL COMMENT 'Command Class Name',
  `intifier` varchar(45) DEFAULT NULL COMMENT 'Intifier',
  `category` varchar(45) DEFAULT NULL COMMENT 'category',
  `status` varchar(45) DEFAULT NULL COMMENT 'status',
  `value` varchar(45) DEFAULT NULL COMMENT 'value',
  `zwave_type` int(11) DEFAULT NULL COMMENT 'zwave_type',
  `zwave_type_name` varchar(45) DEFAULT NULL COMMENT 'zwave_type_name',
  `created_time` timestamp NULL DEFAULT NULL COMMENT 'created_time',
  `lastmodified_date` timestamp NULL DEFAULT NULL COMMENT 'lastmodified_date',
  `sub_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='device정보';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-13  9:33:12
