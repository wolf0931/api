

DROP TABLE IF EXISTS `api_document`;
CREATE TABLE `api_document` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL COMMENT '项目ID',
  `group_id` int(11) NOT NULL COMMENT '分组ID',
  `name` varchar(50) NOT NULL COMMENT '接口名称',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0-启用、1-维护、2-废弃',
  `star_level` tinyint(4) NOT NULL COMMENT '星标等级：0-普通接口、1-一星接口',
  `request_url` varchar(100) NOT NULL COMMENT 'Request URL：相对地址',
  `request_method` varchar(20) NOT NULL COMMENT 'Request Method：如POST、GET',
  `request_headers` text COMMENT 'Request Headers：Map-JSON格式字符串',
  `query_params` text COMMENT 'Query String Parameters：VO-JSON格式字符串',
  `response_params` text COMMENT 'Response Parameters：VO-JSON格式字符串',
  `success_resp_type` varchar(50) NOT NULL COMMENT 'Response Content-type：成功接口，如JSON、XML、HTML、TEXT、JSONP',
  `success_resp_example` text COMMENT 'Response Content：成功接口',
  `fail_resp_type` varchar(255) NOT NULL COMMENT 'Response Content-type：失败接口',
  `fail_resp_example` text COMMENT 'Response Content：失败接口',
  `remark` text COMMENT '备注',
  `add_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `api_group`;

CREATE TABLE `api_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL COMMENT '项目ID',
  `name` varchar(255) NOT NULL COMMENT '分组名称',
  `order` int(11) NOT NULL COMMENT '分组排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `api_mock`;
CREATE TABLE `api_mock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `document_id` int(11) NOT NULL COMMENT '接口ID',
  `uuid` varchar(50) NOT NULL COMMENT 'UUID',
  `resp_type` varchar(50) NOT NULL COMMENT 'Response Content-type：如JSON、XML、HTML、TEXT、JSONP',
  `resp_example` text COMMENT 'Response Content',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `api_project`;
CREATE TABLE `api_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '项目名称',
  `desc` varchar(200) DEFAULT NULL COMMENT '项目描述',
  `permission` tinyint(4) NOT NULL DEFAULT '0' COMMENT '访问权限：0-公开、1-私有',
  `base_url_product` varchar(200) NOT NULL COMMENT '跟地址：线上环境',
  `base_url_ppe` varchar(200) DEFAULT NULL COMMENT '跟地址：预发布环境',
  `base_url_qa` varchar(200) DEFAULT NULL COMMENT '跟地址：测试环境',
  `version` varchar(50) NOT NULL COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `api_test_history`;
CREATE TABLE `api_test_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `document_id` int(11) NOT NULL COMMENT '接口ID',
  `add_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `request_url` varchar(200) NOT NULL COMMENT 'Request URL：绝对地址',
  `request_method` varchar(20) NOT NULL COMMENT 'Request Method：如POST、GET',
  `request_headers` text COMMENT 'Request Headers：Map-JSON格式字符串',
  `query_params` text COMMENT 'Query String Parameters：VO-JSON格式字符串',
  `resp_type` varchar(50) DEFAULT NULL COMMENT 'Response Content-type：如JSON',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `api_user`;
CREATE TABLE `api_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户类型：0-普通用户、1-超级管理员',
  `realname` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into `api_user` ( `username`, `password`, `type`, `realname`) values ( 'admin', '123456', '1', '管理员');

DROP TABLE IF EXISTS `api_user_permission`;
CREATE TABLE `api_user_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `product_id` int(11) NOT NULL COMMENT '项目ID',
  `add_time` datetime NOT NULL COMMENT '新增时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
