/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50521
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50521
File Encoding         : 65001

Date: 2014-07-18 03:28:46
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `hf_catalogs`
-- ----------------------------
DROP TABLE IF EXISTS `hf_catalogs`;
CREATE TABLE `hf_catalogs` (
  `cat_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '分类名',
  `slug` varchar(255) DEFAULT NULL COMMENT 'URL表示',
  `parent` int(11) unsigned DEFAULT NULL COMMENT '父分类',
  `sort_order` smallint(5) unsigned DEFAULT NULL COMMENT '显示顺序',
  PRIMARY KEY (`cat_id`),
  UNIQUE KEY `cat_slug` (`cat_id`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='分类表';

-- ----------------------------
-- Records of hf_catalogs
-- ----------------------------
INSERT INTO hf_catalogs VALUES ('1', 'java', 'java', '0', '1');
INSERT INTO hf_catalogs VALUES ('2', 'linux', 'linux', '0', '2');
INSERT INTO hf_catalogs VALUES ('3', 'db', 'db', '0', '3');
INSERT INTO hf_catalogs VALUES ('4', 'python', 'python', '0', '4');
INSERT INTO hf_catalogs VALUES ('5', 'hadoop', 'hadoop', '0', '5');
INSERT INTO hf_catalogs VALUES ('6', 'git', 'git', '0', '6');
INSERT INTO hf_catalogs VALUES ('7', 'spring', 'spring', '0', '7');
INSERT INTO hf_catalogs VALUES ('10', '其它', 'other', '0', '8');

-- ----------------------------
-- Table structure for `hf_comments`
-- ----------------------------
DROP TABLE IF EXISTS `hf_comments`;
CREATE TABLE `hf_comments` (
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `post_id` int(20) unsigned DEFAULT NULL COMMENT '对应文章ID',
  `name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '评论者',
  `email` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '评论者邮箱',
  `url` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '评论者网址',
  `content` mediumtext COLLATE utf8_bin COMMENT '评论正文',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '评论时间',
  `client_ip` char(100) COLLATE utf8_bin DEFAULT NULL COMMENT '评论者IP',
  `parent` int(20) unsigned DEFAULT NULL COMMENT '父评论ID',
  PRIMARY KEY (`id`),
  KEY `fk_post_id` (`post_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='评论，评论的游客的userid为0';

-- ----------------------------
-- Records of hf_comments
-- ----------------------------

-- ----------------------------
-- Table structure for `hf_links`
-- ----------------------------
DROP TABLE IF EXISTS `hf_links`;
CREATE TABLE `hf_links` (
  `link_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增唯一ID',
  `link_url` varchar(255) DEFAULT NULL COMMENT '链接URL',
  `link_name` varchar(255) DEFAULT NULL COMMENT '链接标题',
  `link_image` varchar(255) DEFAULT NULL COMMENT 'link_image',
  `link_target` varchar(25) DEFAULT NULL COMMENT '链接打开方式',
  `link_description` varchar(255) DEFAULT NULL COMMENT '链接描述',
  `link_visible` varchar(20) DEFAULT NULL COMMENT '是否可见（Y/N）',
  `link_owner` bigint(10) unsigned DEFAULT NULL COMMENT '添加者用户ID',
  `link_rating` int(11) DEFAULT NULL COMMENT '评分等级',
  `link_updated` datetime DEFAULT NULL COMMENT '未知',
  `link_rel` varchar(255) DEFAULT NULL COMMENT 'XFN关系',
  `link_notes` mediumtext COMMENT 'XFN注释',
  `link_rss` varchar(255) DEFAULT NULL COMMENT '链接RSS地址',
  PRIMARY KEY (`link_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='存储友情链接（Blogroll）';

-- ----------------------------
-- Records of hf_links
-- ----------------------------
INSERT INTO hf_links VALUES ('1', 'http://www.baidu.com', '链接名', null, null, null, null, '1', null, null, null, null, null);

-- ----------------------------
-- Table structure for `hf_options`
-- ----------------------------
DROP TABLE IF EXISTS `hf_options`;
CREATE TABLE `hf_options` (
  `option_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `blog_id` bigint(20) unsigned zerofill DEFAULT NULL COMMENT '博客ID，用于多用户博客，默认0',
  `option_name` varchar(255) DEFAULT NULL COMMENT '键名',
  `option_value` longtext COMMENT '键值',
  `autoload` varchar(20) DEFAULT NULL COMMENT '在系统载入时自动载入（yes/no）',
  PRIMARY KEY (`option_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='存储WordPress系统选项和插件、主题配置';

-- ----------------------------
-- Records of hf_options
-- ----------------------------

-- ----------------------------
-- Table structure for `hf_posts`
-- ----------------------------
DROP TABLE IF EXISTS `hf_posts`;
CREATE TABLE `hf_posts` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cat_id` int(10) unsigned DEFAULT NULL COMMENT '分类编号',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `content` text COMMENT '正文',
  `excerpt` text COMMENT '摘录',
  `tags` varchar(100) DEFAULT NULL COMMENT '关键字',
  `type` tinyint(3) DEFAULT NULL COMMENT '博文类型 0原创,1转载,2草稿',
  `origin_url` varchar(100) DEFAULT NULL COMMENT '引用地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modified_time` datetime DEFAULT NULL COMMENT '修改时间',
  `comment_count` int(10) unsigned DEFAULT '0' COMMENT '评论数',
  `view_count` int(10) unsigned DEFAULT '0' COMMENT '访问数',
  `as_top` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cat_id` (`cat_id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hf_posts
-- ----------------------------
INSERT INTO hf_posts VALUES ('1', '1', 'webservice+cxf+spring', '<p>sdfsdf</p>\n<p>&nbsp;</p>\n<p>你或许也知道，正是JVM( Java Virtusal Machine，Java虚拟机)使得Java成为遵循&ldquo;一次编写，处处运行&rdquo;的范例。JVM包括如下核心组件： 堆 栈 持久代及方法区</p>', '你或许也知道，正是JVM( Java Virtusal Machine，Java虚拟机)使得Java成为遵循“一次编写，处处运行”的范例。JVM包括如下核心组件： 堆 栈 持久代及方法区', 'java,jdbc', '0', null, '2014-06-28 18:11:17', '2014-07-18 02:43:20', '4', '22', '0');
INSERT INTO hf_posts VALUES ('2', '1', 'byte，short，char的一些细节', '<p>ffff</p>\n<p>&nbsp;</p>\n<p>例如有表A(字段为ID,NAME)，有两条记录 表B（字段为ID,NAME)，有三条记录 当表A连接表B时， select * from A a left join B b on a.name=b.name; &nbsp;结果产生3条记录</p>', '例如有表A(字段为ID,NAME)，有两条记录 表B（字段为ID,NAME)，有三条记录 当表A连接表B时， select * from A a left join B b on a.name=b.name;  结果产生3条记录', 'lucene,hadoop,hbase', '0', null, '2014-07-12 18:11:23', '2014-07-18 02:43:29', '1', '26', '0');
INSERT INTO hf_posts VALUES ('3', '1', 'SSH应用2', '<p>允许你去验证一个模型的状态，在它被写入到数据库之前。这有一些方法你可以用来校验你的模型和验证属性值是否为空，是否唯一性和是否已存在于数据库中，以及遵从与一个特定的详细格式之类的。 &nbsp; 当数据持久型到数据库的时候，验证是一个非常重要的问题需要考虑的，特别是这些方法 create, save 和 update 需&nbsp;</p>', '允许你去验证一个模型的状态，在它被写入到数据库之前。这有一些方法你可以用来校验你的模型和验证属性值是否为空，是否唯一性和是否已存在于数据库中，以及遵从与一个特定的详细格式之类的。   当数据持久型到数据库的时候，验证是一个非常重要的问题需要考虑的，特别是这些方法 create, save 和 update 需 ', 'mysql,db', '0', null, '2014-06-17 18:11:27', '2014-07-18 02:43:05', '0', '13', '0');
INSERT INTO hf_posts VALUES ('4', '2', 'Windows Server下Weblogic 11g控制台输出日志文件体积过大，占满磁盘空间的解决办法', '<p>ewwwe</p>\n<p>&nbsp;</p>\n<p>&nbsp; &nbsp; 代码编写完毕，就可以进行编译了，这里使用ant工具，起build.xml文件在我的其他博客中有下载，只要电脑配置了ant环境，进入命令行运行ant compile就可以编译代码了，同时由于不适用IDE工具，代码错误定位会身份准确，调试起来得心应手。 希望SSH系列博客有帮助到各位小白。</p>\n<p>&nbsp;</p>\n<p>&nbsp; &nbsp; 代码编写完毕，就可以进行编译了，这里使用ant工具，起build.xml文件在我的其他博客中有下载，只要电脑配置了ant环境，进入命令行运行ant compile就可以编译代码了，同时由于不适用IDE工具，代码错误定位会身份准确，调试起来得心应手。 希望SSH系列博客有帮助到各位小白。</p>', '代码编写完毕，就可以进行编译了，这里使用ant工具，起build.xml文件在我的其他博客中有下载，只要电脑配置了ant环境，进入命令行运行ant compile就可以编译代码了，同时由于不适用IDE工具，代码错误定位会身份准确，调试起来得心应手。 希望SSH系列博客有帮助到各位小白。', 'js,web前段,html5', '0', null, '2014-06-26 18:11:30', '2014-07-18 02:43:13', '1', '21', '0');
INSERT INTO hf_posts VALUES ('5', '2', 'ZooKeeper学习之本地存储(事务日志与快照)', '<p>今天主要讲3个方面的内容： &nbsp; &nbsp; &nbsp; 目录： &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;1：通过spring_cxf创建webservice。 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 2：通过CXF的wsdl2java创建java类。 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 3：客户端调用。 1：创建webservice &nbsp; &nbsp; &nbsp;首先创建个maven-archetype-webapp项目，不会创建参考我博客spring</p>', '今天主要讲3个方面的内容：       目录：              1：通过spring_cxf创建webservice。               2：通过CXF的wsdl2java创建java类。               3：客户端调用。 1：创建webservice      首先创建个maven-archetype-webapp项目，不会创建参考我博客spring', 'html5', '0', null, '2014-06-06 18:11:34', '2014-07-18 02:42:54', '0', '15', '0');
INSERT INTO hf_posts VALUES ('10', '7', '测试sdfsdfsdfdsfsdfsdf', '<h2>介绍：</h2>\n<p>IntelliJ IDEA支持开发运行在移动手机上的应用程序，所以也能在Android操作系统下运行。出来一般的代码提示助手功能之外，这个IDE也是你能够通过配置模拟器设备测试Android程序。</p>\n<p>IntelliJ IDEA的帮助提示：</p>\n<p>&middot;通过New&nbsp;Project&nbsp;Wizard创建一个Android程序</p>\n<p>&middot;在文件和文件夹的tree-view视图下查看Android程序</p>\n<p>&middot;创建Android程序的元素，管理文件资源，例如strings，colors等等，通过R.java能够看到与之紧密相联的资源</p>\n<p>&middot;运行程序（<a href=\"http://my.oschina.net/obj\" target=\"_blank\">txlong_onz</a>）</p>\n<p>&middot;配置模拟器并且在模拟器上运行程序</p>\n<p>&nbsp;</p>\n<p>下边就一步一步的教你怎么通过IntelliJ IDEA来开发并且运行一个简单的Android应用</p>\n<h2>先决条件：</h2>\n<p>&middot;你要在IntelliJ IDEA Ultimate 9版本或者以上</p>\n<p>&middot;还有就是要安装JDK哦</p>\n<p>&middot;然后还要有Android SDK了，这是废话，嘻嘻，我就用SDK2.2 FroYo好了</p>\n<p><br /><img src=\"http://www.loveff.cn/tinymce_4.1.0/plugins/emoticons/img/smiley-laughing.gif\" alt=\"laughing\" /></p>', '介绍： IntelliJ IDEA支持开发运行在移动手机上的应用程序，所以也能在Android操作系统下运行。出来一般的代码提示助手功能之外，这个IDE也是你能够通过配置模拟器设备测试Android程序。', 'IntelliJ Idea, tree, Android SDK', '0', null, '2014-07-14 04:16:48', '2014-07-18 02:43:37', '0', '52', '0');
INSERT INTO hf_posts VALUES ('20', '10', 'asdfsadfsafd', '<p><img src=\"http://static.loveff.cn/uploads/201407151524009290.png\" alt=\"\" width=\"156\" height=\"156\" /></p>', 'asdfsdf', 'asdfsadfsadf', '0', null, '2014-07-15 15:25:39', '2014-07-18 02:43:47', '0', '36', '0');
INSERT INTO hf_posts VALUES ('21', '5', '心烦！', '<p><img src=\"http://static.loveff.cn/uploads/201407161830375095.gif\" alt=\"\" width=\"50\" height=\"50\" />好心烦！！！3</p>', 'afsdfasdfsafd', 'fds', '0', null, '2014-07-16 18:31:45', '2014-07-18 02:43:52', '0', '129', '0');
INSERT INTO hf_posts VALUES ('40', '3', '6年年', '<p>6年年</p>', '6年年', '', '2', null, '2014-07-18 03:19:04', '2014-07-18 03:19:04', '0', '3', '0');
INSERT INTO hf_posts VALUES ('46', '0', 'fff', '<p>fff</p>', 'ff', '', '2', null, '2014-07-18 03:19:33', '2014-07-18 03:19:33', '0', '0', '0');

-- ----------------------------
-- Table structure for `hf_users`
-- ----------------------------
DROP TABLE IF EXISTS `hf_users`;
CREATE TABLE `hf_users` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `email` varchar(100) DEFAULT NULL COMMENT 'Email',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `nicename` varchar(50) DEFAULT NULL COMMENT '显示昵称',
  `url` varchar(100) DEFAULT NULL COMMENT '网址',
  `reg_time` datetime DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='注册用户基本信息';

-- ----------------------------
-- Records of hf_users
-- ----------------------------
INSERT INTO hf_users VALUES ('1', 'xmasmail@126.com', '9710630fc22873b7c9c5396fe7396c71', '小张', null, '2014-06-22 18:22:10');
