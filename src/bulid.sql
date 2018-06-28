CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `phone` int(11) NOT NULL,
  `statu` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `password` int(11) NOT NULL,
  `group_no` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `athlete` (
  `id` int(11) NOT NULL COMMENT '		',
  `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `sex` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `group_no` int(11) DEFAULT NULL,
  `item` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `arrange` (
  `ath_no` int(11) NOT NULL,
  `contest` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ath_no`),
  UNIQUE KEY `ath_no_UNIQUE` (`ath_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `score` (
  `ath_no` int(11) NOT NULL,
  `n_score` int(11) DEFAULT NULL,
  `p_score` int(11) DEFAULT NULL,
  `d_score` int(11) DEFAULT NULL,
  `final_score` int(11) DEFAULT NULL,
  PRIMARY KEY (`ath_no`),
  UNIQUE KEY `ath_no_UNIQUE` (`ath_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
