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
-- Table structure for table `gateway`
--

DROP TABLE IF EXISTS `gateway`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gateway` (
  `no` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nicname` varchar(45) DEFAULT NULL COMMENT 'gateway id',
  `id` varchar(20) NOT NULL COMMENT 'gateway id',
  `ip` varchar(30) DEFAULT NULL COMMENT 'Host의사설IP주소',
  `connected_control_code` int(11) DEFAULT NULL COMMENT '외부접속등록기기정보',
  `ssid` varchar(20) DEFAULT NULL COMMENT '세대내인터넷공유기',
  `bssid` varchar(20) DEFAULT NULL COMMENT 'Host MAC주소',
  `serial` varchar(45) DEFAULT NULL COMMENT 'Host serial',
  `active` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '1:정상, 2:침입발생',
  `update_no` int(11) DEFAULT NULL,
  `security_mode` tinyint(4) unsigned NOT NULL DEFAULT '3' COMMENT '외출모드:1, 재택모드:2, 방범해제:3, 외출 방범 설정 중:4, 재택 방범 설정 중:5, 비상 모드:6, 외출 해제 대기 모드:7, 재택 해제 대기 모드:8',
  `security_event` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '1:정상, 2:침입발생',
  `security_user_no` int(10) unsigned DEFAULT NULL,
  `security_in_time` int(9) DEFAULT NULL,
  `security_out_time` int(9) DEFAULT NULL,
  `unread_push_message_count` int(11) unsigned NOT NULL COMMENT '미확인 푸시(알림)메세지 갯수',
  `alive_wakeup` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '부팅 또는 alive메세지수신시 갱신',
  `created_user_id` timestamp NULL DEFAULT NULL COMMENT 'created_user_id',
  `created_time` timestamp NULL DEFAULT NULL,
  `lastmodified_time` timestamp NULL DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='호스트 제품 세대내 운영정보';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-13  9:33:10
