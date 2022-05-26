DROP TABLE IF EXISTS `execution`;
CREATE TABLE `execution`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `component` varchar(100) NOT NULL,
    `version` varchar(100) NOT NULL,
    `pr_id` varchar(45) NOT NULL DEFAULT '0',
    `suite` varchar(100) NOT NULL,
    `actor` varchar(45) NOT NULL,
    `thread_ts` varchar(45) NOT NULL,
    `repo` varchar(45) NOT NULL,
    `branch` varchar(45) NOT NULL,
    `service_mocking` varchar(200) NOT NULL,
    `dependency` varchar(200) NOT NULL,
    `build_number` varchar(45) NOT NULL,
    `build_url` varchar(100) NOT NULL,
    `status` varchar(45) NOT NULL DEFAULT 'SUBMITTED',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `execution_task`;
CREATE TABLE `execution_task`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `execution_id` int(11) NOT NULL,
    `environment_id` int(11) NOT NULL,
    `status` varchar(45) NOT NULL DEFAULT 'IN_PROGRESS',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `environment`;
CREATE TABLE `environment`(
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    `url` varchar(100) NOT NULL,
    `status` varchar(45) NOT NULL DEFAULT 'IDLE',
    `version` varchar(100) NOT NULL,
    `namespace` varchar(45) NOT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;