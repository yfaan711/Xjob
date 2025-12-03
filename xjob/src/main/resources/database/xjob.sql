-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: xjob
-- ------------------------------------------------------
-- Server version	8.0.42

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

--
-- Table structure for table `tb_blog`
--

DROP TABLE IF EXISTS `tb_blog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_blog` (
                           `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `user_id` bigint unsigned NOT NULL COMMENT '发布者id',
                           `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子标题',
                           `images` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子的照片，最多9张，多张以","隔开',
                           `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子内容',
                           `blog_type` enum('seek','offer','share') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '帖子类型：seek-找服务，offer-提供服务，share-分享',
                           `category_id` bigint unsigned DEFAULT NULL COMMENT '关联服务类别id',
                           `view_count` int DEFAULT '0' COMMENT '浏览量',
                           `status` tinyint DEFAULT '1' COMMENT '状态：0-删除，1-正常，2-审核中，3-违规下架',
                           `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                           `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`) USING BTREE,
                           KEY `idx_user_id` (`user_id`) USING BTREE,
                           KEY `idx_category_id` (`category_id`) USING BTREE,
                           KEY `idx_blog_type_status` (`blog_type`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='帖子表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_blog_comment`
--

DROP TABLE IF EXISTS `tb_blog_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_blog_comment` (
                                   `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `blog_id` bigint unsigned NOT NULL COMMENT '关联帖子id',
                                   `user_id` bigint unsigned NOT NULL COMMENT '评论者用户id',
                                   `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
                                   `status` tinyint DEFAULT '1' COMMENT '状态：0-删除，1-正常，2-审核中，3-违规隐藏',
                                   `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
                                   `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`) USING BTREE,
                                   KEY `idx_blog_id` (`blog_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_category`
--

DROP TABLE IF EXISTS `tb_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_category` (
                               `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别名称（如摄影、化妆）',
                               `parent_id` bigint unsigned DEFAULT '0' COMMENT '父类别ID（0表示一级类别）',
                               `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类别图标URL',
                               `sort_order` int DEFAULT '0' COMMENT '排序权重（数字越大越靠前）',
                               `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE KEY `uk_name` (`name`) USING BTREE,
                               KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='服务类别表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_comment_reply`
--

DROP TABLE IF EXISTS `tb_comment_reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_comment_reply` (
                                    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `comment_id` bigint unsigned NOT NULL COMMENT '关联评论id',
                                    `user_id` bigint unsigned NOT NULL COMMENT '回复者用户id',
                                    `reply_to_user_id` bigint unsigned NOT NULL COMMENT '被回复用户id',
                                    `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '回复内容',
                                    `status` tinyint DEFAULT '1' COMMENT '状态：0-删除，1-正常，2-审核中，3-违规隐藏',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
                                    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    KEY `idx_comment_id` (`comment_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='评论回复表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_follow`
--

DROP TABLE IF EXISTS `tb_follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_follow` (
                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `user_id` bigint unsigned NOT NULL COMMENT '用户id',
                             `follow_user_id` bigint unsigned NOT NULL COMMENT '关联的用户id',
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE KEY `uk_user_follow` (`user_id`,`follow_user_id`) USING BTREE,
                             KEY `idx_user_id` (`user_id`) USING BTREE,
                             KEY `idx_follow_user_id` (`follow_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='用户关注表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_job`
--

DROP TABLE IF EXISTS `tb_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_job` (
                          `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                          `user_id` int NOT NULL,
                          `job` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '职位名称',
                          `skill` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '擅长领域',
                          `introduce` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '简介',
                          'type_id' int unsigned NOT NULL COMMENT '职位类别id',
                          `price` int DEFAULT NULL COMMENT '单价/次',
                          `work_duration` int unsigned NOT NULL COMMENT '从业时长（月）',
                          `work_city` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务城市，JSON格式存储多个城市',
                          `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (`id`),
                          KEY `idx_job` (`job`),
                          KEY `idx_work_duration` (`work_duration`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='职位信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_message`
--

DROP TABLE IF EXISTS `tb_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_message` (
                              `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `sender_id` bigint unsigned NOT NULL COMMENT '发送者用户id',
                              `receiver_id` bigint unsigned NOT NULL COMMENT '接收者用户id',
                              `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
                              `is_read` tinyint DEFAULT '0' COMMENT '是否已读：0-未读，1-已读',
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              KEY `idx_sender_receiver` (`sender_id`,`receiver_id`) USING BTREE,
                              KEY `idx_receiver_is_read` (`receiver_id`,`is_read`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_order`
--

DROP TABLE IF EXISTS `tb_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_order` (
                            `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号（如ORD20251026001，主键）',
                            `demand_user_id` bigint unsigned NOT NULL COMMENT '需求方用户id',
                            `provider_id` bigint unsigned NOT NULL COMMENT '服务提供者id',
                            `category_id` bigint unsigned NOT NULL COMMENT '服务类别id',
                            `skill_id` bigint unsigned DEFAULT NULL COMMENT '技能id',
                            `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单标题',
                            `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '服务详情描述',
                            `amount` decimal(10,2) NOT NULL COMMENT '订单金额（元）',
                            `status` enum('pending','confirmed','in_progress','completed','cancelled') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单状态',
                            `start_time` datetime DEFAULT NULL COMMENT '服务开始时间',
                            `end_time` datetime DEFAULT NULL COMMENT '服务结束时间',
                            `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `idx_demand_user_id` (`demand_user_id`) USING BTREE,
                            KEY `idx_provider_id` (`provider_id`) USING BTREE,
                            KEY `idx_status_create_time` (`status`,`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_order_payment`
--

DROP TABLE IF EXISTS `tb_order_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_order_payment` (
                                    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联订单id',
                                    `amount` decimal(10,2) NOT NULL COMMENT '支付金额（元）',
                                    `payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付方式（微信、支付宝等）',
                                    `transaction_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '第三方支付流水号',
                                    `status` enum('pending','success','failed') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支付状态',
                                    `paid_at` datetime DEFAULT NULL COMMENT '支付成功时间',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE KEY `uk_transaction_id` (`transaction_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='订单支付表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_review`
--

DROP TABLE IF EXISTS `tb_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_review` (
                             `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联订单id',
                             `demand_user_id` bigint unsigned NOT NULL COMMENT '评价者用户id',
                             `provider_id` bigint unsigned NOT NULL COMMENT '被评价服务提供者id',
                             `rating` tinyint NOT NULL COMMENT '评分（1-5分）',
                             `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评价内容',
                             `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
                             `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE KEY `uk_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='评价表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user` (
                           `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `nick_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名（登录用）',
                           `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT '',
                           `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
                           `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像URL',
                           `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                           `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`) USING BTREE,
                           UNIQUE KEY `uk_username` (`nick_name`) USING BTREE,
                           UNIQUE KEY `uk_phone` (`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_user_info`
--

DROP TABLE IF EXISTS `tb_user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_info` (
                                `user_id` bigint unsigned NOT NULL COMMENT '主键，用户id',
                                `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '城市名称',
                                `introduce` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '个人介绍，不要超过128个字符',
                                `fans` int unsigned DEFAULT '0' COMMENT '粉丝数量',
                                `followee` int unsigned DEFAULT '0' COMMENT '关注的人的数量',
                                `gender` tinyint unsigned DEFAULT '0' COMMENT '性别，0：男，1：女',
                                `birthday` date DEFAULT NULL COMMENT '生日',
                                `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_voucher`
--

DROP TABLE IF EXISTS `tb_voucher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher` (
                              `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `provider_id` bigint unsigned DEFAULT NULL COMMENT '关联服务提供者id（NULL表示平台通用）',
                              `category_id` bigint unsigned DEFAULT NULL COMMENT '关联服务类别id（NULL表示全类别）',
                              `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代金券标题',
                              `sub_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '副标题',
                              `rules` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '使用规则',
                              `pay_value` bigint unsigned NOT NULL COMMENT '支付金额，单位是分',
                              `actual_value` bigint NOT NULL COMMENT '抵扣金额，单位是分',
                              `total_quantity` int NOT NULL DEFAULT '0' COMMENT '总发行量（0表示不限量）',
                              `remaining_quantity` int NOT NULL DEFAULT '0' COMMENT '剩余数量',
                              `type` tinyint NOT NULL DEFAULT '0' COMMENT '类型：0-普通券，1-秒杀券，2-平台券',
                              `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：1-上架，2-下架，3-过期',
                              `start_time` datetime NOT NULL COMMENT '生效时间',
                              `end_time` datetime NOT NULL COMMENT '过期时间',
                              `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE,
                              KEY `idx_provider_id` (`provider_id`) USING BTREE,
                              KEY `idx_category_id` (`category_id`) USING BTREE,
                              KEY `idx_status_time` (`status`,`start_time`,`end_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='代金券表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_voucher_order`
--

DROP TABLE IF EXISTS `tb_voucher_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_voucher_order` (
                                    `id` bigint unsigned NOT NULL COMMENT '主键',
                                    `user_id` bigint unsigned NOT NULL COMMENT '下单的用户id',
                                    `voucher_id` bigint unsigned NOT NULL COMMENT '购买的代金券id',
                                    `pay_type` tinyint DEFAULT '1' COMMENT '支付方式：0-免费，1-余额，2-支付宝，3-微信',
                                    `pay_amount` bigint NOT NULL DEFAULT '0' COMMENT '实际支付金额（分）',
                                    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：1-未支付，2-已领取/购买，3-已核销，4-已取消，5-已过期',
                                    `order_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取/下单时间',
                                    `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
                                    `use_time` datetime DEFAULT NULL COMMENT '核销时间',
                                    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    KEY `idx_user_id` (`user_id`) USING BTREE,
                                    KEY `idx_voucher_id` (`voucher_id`) USING BTREE,
                                    KEY `idx_status_order_time` (`status`,`order_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='代金券订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tb_job_type`
--

DROP TABLE IF EXISTS `tb_job_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_job_type` (
                               `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型名称',
                               `sort` int DEFAULT '0' COMMENT '排序优先级',
                               `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='职位类型表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, '+00:00') */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-13 22:28:44
