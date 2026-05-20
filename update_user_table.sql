-- 修改 user 表结构以支持加盐 MD5 密码存储
-- 1. 首先添加 salt 字段用于存储盐值
ALTER TABLE `user` ADD COLUMN `salt` varchar(16) DEFAULT NULL AFTER `password`;

-- 2. 修改 password 字段长度为 32 位以存储 MD5 密文
ALTER TABLE `user` MODIFY COLUMN `password` varchar(32) DEFAULT NULL;
