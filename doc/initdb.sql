CREATE TABLE `customer` (
                            `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                            `customer_id` bigint(20) unsigned DEFAULT NULL,
                            `member_id` bigint(20) unsigned DEFAULT NULL,
                            `customer_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                            `customer_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                            `company_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                            `source` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                            `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户信息表';
