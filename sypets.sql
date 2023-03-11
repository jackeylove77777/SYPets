

-- ----------------------------
-- Table structure for syslogger
-- ----------------------------
DROP TABLE IF EXISTS `syslogger`;
CREATE TABLE `syslogger` (
                             `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '记录ID',
                             `description` varchar(20) DEFAULT NULL,
                             `create_time` datetime NOT NULL COMMENT '创建时间',
                             `ip` varchar(30) DEFAULT NULL,
                             `uid` int(11) DEFAULT NULL,
                             `url` varchar(100) NOT NULL COMMENT '修改时间',
                             `request_type` varchar(10) NOT NULL DEFAULT '' COMMENT '执行的方法-类全命名.方法',
                             `class_method` varchar(100) DEFAULT NULL,
                             `params` text NOT NULL COMMENT '传入的参数',
                             `response` text NOT NULL COMMENT '主机',
                             `exc_time` int(11) DEFAULT NULL,
                             `log_type` int(11) DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `username` varchar(25) DEFAULT NULL,
                        `password` varchar(100) DEFAULT NULL,
                        `salt` varchar(10) DEFAULT NULL,
                        `email` varchar(20) DEFAULT NULL,
                        `avatar` varchar(100) DEFAULT NULL comment '头像',
                        `private_info` varchar(50) DEFAULT NULL comment '个人介绍',
                        `sex` int(11) DEFAULT '0',
                        `status` int(1) DEFAULT '1',
                        `create_time` datetime DEFAULT NULL,
                        `update_time` datetime DEFAULT NULL,
                        `del_flag` int(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 comment '用户表';


DROP TABLE IF EXISTS `advertisement`;
#
CREATE TABLE `advertisement` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `name` varchar(256) DEFAULT NULL,
                           `logo` varchar(256) DEFAULT NULL,
                           `description` varchar(512) DEFAULT NULL,
                           `address` varchar(128) DEFAULT NULL COMMENT '网站地址',
                           `status` char(1) DEFAULT '2' COMMENT '审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)',
                           `create_by` bigint(20) DEFAULT NULL,
                           `create_time` datetime DEFAULT NULL,
                           `update_by` bigint(20) DEFAULT NULL,
                           `update_time` datetime DEFAULT NULL,
                           `del_flag` int(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='友链';

/*Data for the table `sg_link` */

insert  into `advertisement`(`id`,`name`,avatar,`description`,`address`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`del_flag`) values (1,'sda','https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn1.itc.cn%2Fimg8%2Fwb%2Frecom%2F2016%2F05%2F10%2F146286696706220328.PNG&refer=http%3A%2F%2Fn1.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1646205529&t=f942665181eb9b0685db7a6f59d59975','sda','https://www.baidu.com','1',NULL,'2022-01-13 08:25:47',NULL,'2022-01-13 08:36:14',0),(2,'sda','https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn1.itc.cn%2Fimg8%2Fwb%2Frecom%2F2016%2F05%2F10%2F146286696706220328.PNG&refer=http%3A%2F%2Fn1.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1646205529&t=f942665181eb9b0685db7a6f59d59975','dada','https://www.qq.com','1',NULL,'2022-01-13 09:06:10',NULL,'2022-01-13 09:07:09',0),(3,'sa','https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn1.itc.cn%2Fimg8%2Fwb%2Frecom%2F2016%2F05%2F10%2F146286696706220328.PNG&refer=http%3A%2F%2Fn1.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1646205529&t=f942665181eb9b0685db7a6f59d59975','da','https://www.taobao.com','1',NULL,'2022-01-13 09:23:01',NULL,'2022-01-13 09:23:01',0);

DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `title` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
                        `description` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
                        `content` longtext CHARACTER SET utf8,
                        `uid` int(11) DEFAULT NULL,
                        `status` int(11) DEFAULT '1',
                        `type_id` int(11) DEFAULT NULL,
                        `author` int(11) DEFAULT '1',
                        `create_time` datetime DEFAULT NULL,
                        `update_time` datetime DEFAULT NULL,
                        `del_flag` int(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 comment '文章帖子表';

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type` (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `name` varchar(20) DEFAULT NULL,
                        `description` varchar(50) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 comment '文章的类型';


-- ----------------------------
-- Table structure for mention
-- ----------------------------
DROP TABLE IF EXISTS `mention`;
CREATE TABLE `mention` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `from_id` int(11) DEFAULT NULL,
                           `to_id` int(11) DEFAULT NULL,
                           `title` varchar(30) DEFAULT NULL,
                           `description` varchar(50) DEFAULT NULL,
                           `content` longtext,
                           `time` datetime DEFAULT NULL,
                           `status` int(255) DEFAULT '1',
                           `type_id` int(11) DEFAULT NULL,
                           `pid` int(11) DEFAULT NULL,
                           `create_time` datetime DEFAULT NULL,
                           `update_time` datetime DEFAULT NULL,
                           `del_flag` int(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '消息推送:例如，点赞、关注、评论、回复评论这些功能';

DROP TABLE IF EXISTS `msg_type`;
CREATE TABLE `msg_type` (
                            `id` int(11) NOT NULL,
                            `name` varchar(10) DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '推送消息的类型,点赞、关注、评论、回复';

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `to_id` int(11) DEFAULT NULL,
                           `message` varchar(100) DEFAULT NULL,
                           `time` datetime DEFAULT NULL,
                           `type_id` int(11) DEFAULT NULL,
                           `status` int(255) DEFAULT '1' comment '代表是否被读',
                           `create_time` datetime DEFAULT NULL,
                           `update_time` datetime DEFAULT NULL,
                           `del_flag` int(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 comment '用户之间的消息表';

DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `uid` int(11) DEFAULT NULL,
                          `uname` varchar(50) DEFAULT NULL,
                          `avatar` varchar(100) DEFAULT NULL,
                          `content` varchar(50) DEFAULT NULL,
                          `time` datetime DEFAULT NULL,
                          `comment_id` int(11) DEFAULT NULL comment '回复的评论id',
                          `status` int(11) DEFAULT '1',
                          `create_time` datetime DEFAULT NULL,
                          `update_time` datetime DEFAULT NULL,
                          `del_flag` int(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '评论的回复';



create table if not exists `stray`(
                                      `id` int(11) primary key  NOT NULL AUTO_INCREMENT,
                                      description varchar(128) default null,
                                      `uid` int(11) DEFAULT NULL comment '发布的用户',
                                      publish_time datetime DEFAULT NULL,
                                      image varchar(128) DEFAULT NULL comment '照片',
                                      address varchar(128) default null comment '发现地点',
                                      status int default 0 comment '0为未审核'
);

create table if not exists `foster`(
                                       `id` int(11) primary key NOT NULL AUTO_INCREMENT,
                                       description varchar(128) default null,
                                       `uid` int(11) DEFAULT NULL comment '发布的用户',
                                       publish_time datetime DEFAULT NULL,
                                       image varchar(128) DEFAULT NULL comment '照片',
                                       address varchar(128) default null comment '地点',
                                       contaction varchar(128) default null comment '联系方式',
                                       status int default 0 comment '0为未审核',
                                       solve int default 0 comment '0代表未给宠物找到新主人'
);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `uid` int(11) DEFAULT NULL,
                           `uname` varchar(30) DEFAULT NULL,
                           `avatar` varchar(100) DEFAULT NULL,
                           `time` datetime DEFAULT NULL,
                           `content` varchar(50) DEFAULT NULL,
                           `pid` int(11) DEFAULT NULL comment '回复的帖子id',
                           `status` int(11) DEFAULT '1',
                           `create_time` datetime DEFAULT NULL,
                           `update_time` datetime DEFAULT NULL,
                           `del_flag` int(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '文章的回答';

INSERT INTO `msg_type` VALUES ('1', '评论');
INSERT INTO `msg_type` VALUES ('2', '回复');
INSERT INTO `msg_type` VALUES ('3', '提到');
INSERT INTO `msg_type` VALUES ('4', '点赞');
INSERT INTO `msg_type` VALUES ('5', '关注');



