ALTER TABLE `app_config`   
  ADD COLUMN `sign1` TEXT NULL  COMMENT '签名档1' AFTER `commentable`,
  ADD COLUMN `sign2` TEXT NULL  COMMENT '签名档2' AFTER `sign1`;
