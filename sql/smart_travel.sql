-- MySQL dump 10.13  Distrib 9.6.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: smart_travel
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '8da42352-abe2-11f1-9db8-1c8341c1d054:1-49,
fe10060e-333d-11f1-b6e9-1c8341c1d054:1-126';

--
-- Current Database: `smart_travel`
--

/*!40000 DROP DATABASE IF EXISTS `smart_travel`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `smart_travel` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `smart_travel`;

--
-- Table structure for table `t_comment`
--

DROP TABLE IF EXISTS `t_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL COMMENT '评论内容',
  `user_id` int NOT NULL COMMENT '评价人ID',
  `username` varchar(100) DEFAULT NULL COMMENT '评价人冗余字段',
  `spot_id` int NOT NULL COMMENT '所属景区ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `score` int DEFAULT '5' COMMENT '评分1-5星',
  `parent_id` int DEFAULT NULL COMMENT '父评论ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_comment`
--

LOCK TABLES `t_comment` WRITE;
/*!40000 ALTER TABLE `t_comment` DISABLE KEYS */;
INSERT INTO `t_comment` VALUES (5,'@222222 是的',9,'admin',3,'2026-04-14 02:15:53',5,NULL),(6,'@admin 哈哈哈\n',9,'admin',3,'2026-04-14 03:12:02',5,5),(7,'哈哈哈哈',6,'222222',8,'2026-04-14 13:38:38',5,NULL),(8,'@222222 假的',6,'222222',8,'2026-04-14 13:38:45',5,7),(9,'45665',6,'222222',9,'2026-04-14 18:42:21',5,NULL),(10,'这个路线不错',6,'222222',8,'2026-04-20 02:04:49',5,NULL),(11,'啊啊啊',6,'222222',8,'2026-04-20 02:05:00',5,NULL),(12,'佩奇是猪',6,'222222',7,'2026-05-16 13:45:27',5,NULL),(13,'@222222 我也喜欢去观看南方兵马雕像',13,'777777',8,'2026-05-16 15:34:39',5,8);
/*!40000 ALTER TABLE `t_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_order`
--

DROP TABLE IF EXISTS `t_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '预约人姓名',
  `spot_name` varchar(100) NOT NULL COMMENT '景区名称',
  `order_date` date NOT NULL COMMENT '预约日期',
  `ticket_count` int DEFAULT '1' COMMENT '购票张数',
  `status` varchar(20) DEFAULT '待使用' COMMENT '订单状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_order`
--

LOCK TABLES `t_order` WRITE;
/*!40000 ALTER TABLE `t_order` DISABLE KEYS */;
INSERT INTO `t_order` VALUES (5,4,'222222','荆楚古城','2026-04-14',1,'已核销','2026-04-12 22:54:09'),(6,4,NULL,'数字博物馆','2026-04-13',1,'已核销','2026-04-13 00:40:21'),(9,5,NULL,'荆楚理工','2026-04-13',1,'预约成功','2026-04-13 01:12:39'),(10,4,NULL,'数字博物馆','2026-04-13',1,'已预约','2026-04-13 01:26:45'),(11,4,NULL,'智慧湿地','2026-04-13',1,'已预约','2026-04-13 01:28:16'),(12,4,NULL,'荆楚古城','2026-04-13',1,'已预约','2026-04-13 01:28:19'),(13,4,NULL,'数字博物馆','2026-04-13',1,'已预约','2026-04-13 01:28:19'),(14,4,NULL,'帅照','2026-04-13',1,'已预约','2026-04-13 01:28:32'),(15,5,NULL,'荆楚古城','2026-04-13',1,'已预约','2026-04-13 01:36:27'),(16,5,NULL,'数字博物馆','2026-04-13',1,'已预约','2026-04-13 01:50:36'),(17,5,NULL,'荆楚理工','2026-04-13',1,'已核销','2026-04-13 02:15:22'),(18,6,NULL,'数字博物馆','2026-04-13',1,'已预约','2026-04-13 02:31:23'),(19,6,NULL,'智慧湿地','2026-04-13',1,'已核销','2026-04-13 03:01:29'),(21,6,NULL,'智慧湿地','2026-04-13',1,'已预约','2026-04-13 04:22:35'),(22,9,NULL,'智慧湿地','2026-04-14',1,'已核销','2026-04-14 01:29:05'),(23,6,NULL,'智慧湿地','2026-04-14',1,'已核销','2026-04-14 03:04:57'),(24,6,NULL,'楚王车马阵驻地','2026-04-14',1,'已预约','2026-04-14 13:38:51'),(26,6,NULL,'星光滨江文化长廊','2026-04-14',1,'已核销','2026-04-14 18:42:23'),(27,6,NULL,'楚王车马阵驻地','2026-04-14',1,'已核销','2026-04-14 19:15:00'),(28,9,NULL,'楚王车马阵驻地','2026-04-18',1,'已预约','2026-04-18 15:40:07'),(29,6,NULL,'楚王车马阵驻地','2026-04-20',1,'已预约','2026-04-20 01:57:52'),(30,6,NULL,'楚王车马阵驻地','2026-04-20',1,'已预约','2026-04-20 02:12:03'),(31,6,NULL,'楚王车马阵驻地','2026-05-17',1,'已预约','2026-05-17 08:43:10'),(32,9,NULL,'云梦泽生态风景区','2026-05-17',1,'已核销','2026-05-17 10:37:44'),(33,9,NULL,'楚王车马阵驻地','2026-06-25',1,'已预约','2026-06-25 02:19:46');
/*!40000 ALTER TABLE `t_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_spot`
--

DROP TABLE IF EXISTS `t_spot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_spot` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '景区名称',
  `description` varchar(500) DEFAULT NULL COMMENT '景区描述',
  `image_url` varchar(500) DEFAULT NULL COMMENT '图片链接',
  `strategy` text COMMENT '游玩推荐攻略',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_spot`
--

LOCK TABLES `t_spot` WRITE;
/*!40000 ALTER TABLE `t_spot` DISABLE KEYS */;
INSERT INTO `t_spot` VALUES (1,'荆楚古城','AR智慧导览核心区，感知千年文化。','https://picsum.photos/600/400?r=11','【晨间：穿越千年】\n09:00 - 从迎宾门入城，建议在此租借一套汉服，带上系统提供的 AR 导览眼镜。\n10:00 - 漫步于古城主街，参与“古城开城仪”沉浸式演艺，感受楚国风韵。\n\n【午间：品味荆楚】\n12:00 - 前往古城美食街，推荐品尝地道的荆州鱼糕和公安锅盔。\n13:30 - 在“智慧茶馆”小憩，体验全息投影评书，顺便在手机端查看下午的排队情况。\n\n【午后：登高望远】\n15:00 - 登上古城墙（宾阳楼），俯瞰整座城市的护城河美景，此时光线最适合拍照。\n17:00 - 乘坐无人驾驶观光车环城一周，结束一天的文化之旅。'),(2,'数字博物馆','5G高清云看展，穿越时空的对话。','https://picsum.photos/600/400?r=22',NULL),(3,'智慧湿地','实时客流监控区，享受自然静谧。','https://picsum.photos/600/400?r=33','【早晨：拥抱自然】\n08:30 - 趁着晨光微露进入湿地公园，此时是观鸟的最佳时机。\n09:30 - 乘坐“5G无人游船”穿梭于芦苇荡中，扫码即可听取沿途动植物的智能语音讲解。\n\n【中午：生态野餐】\n12:00 - 在指定的生态露营区享用自带午餐，请带走垃圾，响应我们的“绿色旅游”倡议。\n\n【下午：科技体验】\n14:00 - 参观“湿地科普馆”，通过 VR 沉浸式体验一滴水的湿地净化之旅。\n16:00 - 沿着智能步道散步，步道大屏会实时显示您的运动消耗和沿途负氧离子浓度。'),(4,'荆楚理工','名城古校','https://picsum.photos/600/400?r=0.1708433180450044',NULL),(6,'东方神话元宇宙乐园','全国首个结合AR/VR技术的沉浸式神话主题乐园，带你全景体验《山海经》的奇幻世界。','https://picsum.photos/800/450?random=21','【参与路线推荐】\n09:00 - 时空之门：戴上园区提供的 AR 眼镜，观看开园全息大秀。\n 10:30 - 夸父追日过山车：结合失重感与 VR 景观的极限体验（建议提前在 APP 取虚拟排队号）。\n 12:30 - 赛博天庭餐厅：体验机器人送餐服务，更换“蟠桃宴”主题套餐。\n 15:00 -女娲补天剧场： 5D沉浸式动感影院，必刷项目！ 🔴温馨提示：居住面积增加，建议穿运动鞋，全程开启手机蓝牙接收智能导览护士。'),(7,'云梦泽生态风景区','拥有万亩水上森林与珍稀候鸟栖息地，配备无人机与水面实时监测系统的智慧生态示范区。','https://picsum.photos/800/450?random=22','生态吸氧之旅】\n🌿上午：水上森林迷宫\n乘坐纯电动无感静音游船，穿梭在水杉林中。扫码船上的NFC标签，可随时收听当前区域的植物科普语音。🏕️\n中午：湖畔生态营地\n在指定区域进行无痕露营，营地提供智能仓储柜和共享充电设施。🔭\n下午：候鸟塔\n预备使用塔顶的高清AI遥控器，屏幕会自动识别视野的鸟类种类并显示科普信息。'),(8,'楚王车马阵驻地','崇尚“南方兵马雕像”的楚国高等级贵族墓地，通过3D全息投影再现两千年前的楚国盛况。','https://picsum.photos/800/450?random=23','【寻根楚文化】\n一、出土文物陈列馆\n这里可以精美的楚国漆器和青铜器。每个展柜旁边都有智能交互屏，可以360度旋转文物细节。\n二、车马阵看到现场\n震撼的真车真马殉葬坑。每天上午10点和下午3点，坑内会上演🔵 《楚王大阅》全息投影光影，千万别走！\n三、互动体验区\n游客可参与“AI”模拟考古”小游戏，体验考古文物的乐趣，积分文化遗产文创壁纸。'),(9,'星光滨江文化长廊','城市夜经济的核心地标，集灯光秀、数字夜市、智能音乐喷泉于一体的滨水休闲空间。','https://picsum.photos/800/450?random=24','【夜游最佳攻略】\n18:30 - 江景晚餐：在滨江沿线的观景餐厅用餐，欣赏落日余晖下的江面。\n 19:30 - 智慧音乐喷泉：喷泉水柱会随着游客在小程序里的投票实时切换轨迹和灯光气氛，互动感拉满\n。两岸楼体灯光秀：最佳观赏点位于🔴长廊中段的玻璃观景台。整点准时上演大型灯光秀。\n 21:00 - 数字文创夜市：逛一逛充满科技感的集市，支持数字人民币全场景支付。');
/*!40000 ALTER TABLE `t_spot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '账号/手机号',
  `password` varchar(100) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名用于实名核销',
  `id_card` varchar(20) DEFAULT NULL COMMENT '18位有效身份证号',
  `gender` varchar(10) DEFAULT NULL COMMENT '性别(男/女/保密)',
  `avatar` varchar(255) DEFAULT NULL COMMENT '用户头像地址',
  `role` varchar(20) DEFAULT 'user' COMMENT '角色',
  `remark` varchar(255) DEFAULT NULL COMMENT '开发备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,'13800138000','dc78ad13089ef5fcad49a2f7dc49cf18',NULL,'智慧游客01','2026-04-10 16:22:06',NULL,NULL,NULL,NULL,'user','测试账号，密码全是123456'),(2,'111111','dc78ad13089ef5fcad49a2f7dc49cf18','13877514257','新游客','2026-04-11 05:54:17',NULL,NULL,NULL,NULL,'user','测试账号，密码全是123456'),(3,'McClellan','dc78ad13089ef5fcad49a2f7dc49cf18','18669207458','智慧游客_7458','2026-04-11 09:28:51',NULL,NULL,NULL,NULL,'user','测试账号，密码全是123456'),(4,'333333','dc78ad13089ef5fcad49a2f7dc49cf18','18665207412',NULL,'2026-04-12 14:44:20',NULL,NULL,NULL,NULL,'user','测试账号，密码全是123456'),(6,'222222','666666','18554721475',NULL,'2026-04-12 18:25:44','罗麟','452177411023271214','保密',NULL,'user','测试账号，密码全是123456'),(9,'admin','dc78ad13089ef5fcad49a2f7dc49cf18','18888888888',NULL,'2026-04-12 18:48:26',NULL,NULL,NULL,'http://localhost:8080/uploads/c10bb395-1d90-4f9d-8ec6-c32ab8906143.jpeg','admin','测试账号，密码全是123456'),(12,'555555','dc78ad13089ef5fcad49a2f7dc49cf18','18557420459',NULL,'2026-04-13 18:46:21',NULL,NULL,NULL,NULL,'user','测试账号，密码全是123456'),(13,'777777','466656adffddcac2212ab23e6f8bd064','18669207451',NULL,'2026-05-16 07:32:44',NULL,NULL,NULL,NULL,'user',NULL);
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-09  8:41:32
