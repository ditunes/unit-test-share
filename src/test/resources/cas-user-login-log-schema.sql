CREATE TABLE `cas_user_login_log` (
`id`  int(11) NOT NULL  AUTO_INCREMENT,
`userId`  int(11) NOT NULL ,
`ip`  varchar(30) NULL DEFAULT NULL ,
`loginTime`  timestamp NULL DEFAULT CURRENT_TIMESTAMP ,
`location`  varchar(30)  NOT NULL ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

