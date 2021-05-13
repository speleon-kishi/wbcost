/*
 Navicat Premium Data Transfer

 Source Server         : wbcost
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : localhost:3306
 Source Schema         : wbcost

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 13/05/2021 10:10:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名',
  `item_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `price` decimal(10, 1) NULL DEFAULT NULL COMMENT '价格',
  `status` int(1) NULL DEFAULT 0 COMMENT '状态 0可用 1禁用',
  `pic_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片URL',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of goods
-- ----------------------------
BEGIN;
INSERT INTO `goods` VALUES (1, '芬达', '瓶装', 6.0, 0, 'https://i.loli.net/2020/03/19/YyqdhwRKoJ4Gcpr.jpg'), (6, '百事可乐', '听装', 3.5, 0, 'https://i.loli.net/2020/03/19/2SmpCHVYr4jownB.png');
COMMIT;

-- ----------------------------
-- Table structure for present
-- ----------------------------
DROP TABLE IF EXISTS `present`;
CREATE TABLE `present`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `present_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '礼品名称',
  `present_descrip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '礼品描述',
  `present_costs` int(255) NULL DEFAULT NULL COMMENT '所需积分',
  `status` int(1) NULL DEFAULT NULL COMMENT '状态 0可用 1禁用',
  `pic_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of present
-- ----------------------------
BEGIN;
INSERT INTO `present` VALUES (5, '可口可乐', '瓶装', 1200, 0, 'https://z3.ax1x.com/2021/05/09/gJD3es.png');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` int(1) NOT NULL DEFAULT 0 COMMENT '状态 0可用 1禁用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `permission` int(1) NOT NULL DEFAULT 2 COMMENT '权限 0管理员 1企业 2求职者',
  `lastlogin` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录令牌',
  `balance` double(255, 1) NULL DEFAULT 0.0 COMMENT '账户余额',
  `online` int(1) NULL DEFAULT 0 COMMENT '是否在线 0否 1是',
  `user_point` int(255) NULL DEFAULT NULL COMMENT '账户积分',
  `buy_goods_cost` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '购物花费',
  `true_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户姓名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (2, 'admin', '4c984344d7da6ed8de08461fc4b1d3f4', NULL, 0, '2020-03-16 20:23:07', 0, '2021-05-12 20:52:19', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjA4Mjc1MzksImlhdCI6MTYyMDgyMzkzOSwidXNlcm5hbWUiOiJhZG1pbiJ9.ffe0taNX-ZsKHez2wXzsHH5OIIAXO-ZtzBkmzKsYzLw', 0.0, 0, 0, 0.00, '管理员'), (16, '36073119961111031X', '4c984344d7da6ed8de08461fc4b1d3f4', NULL, 0, '2021-05-06 17:01:08', 2, '2021-05-08 17:58:46', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjA4Mjc0NjksImlhdCI6MTYyMDgyMzg2OSwidXNlcm5hbWUiOiIzNjA3MzExOTk2MTExMTAzMVgifQ.krgeF-1SyYABUlEhNjYO4uQ_XAVSUrCH6OCM6c3G4t4', 17.5, 0, 9, 3.50, '李四'), (17, '362132197311110330', '15890bb443cb7500dd7d5b7789adc231', NULL, 0, '2021-05-06 17:11:33', 2, '2021-05-06 17:11:33', NULL, 17.0, 0, 3, 0.00, '王五'), (18, '110101199201016737', '190fbdf53c30f797d87c3e7d03740b49', NULL, 0, '2021-05-06 17:19:10', 2, '2021-05-07 22:09:10', NULL, 26.0, 0, 6, 0.00, '赵六'), (21, '632323190605265643', '8f4712f0147e06f35b2e444c2e8063c2', NULL, 0, '2021-05-09 04:14:33', 2, '2021-05-09 08:00:06', NULL, 0.0, 0, 6, 0.00, '吴起'), (23, '360731199811110314', '4c984344d7da6ed8de08461fc4b1d3f4', NULL, 0, '2021-05-09 06:26:02', 2, '2021-05-09 09:44:11', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjA1MjgyNjUsImlhdCI6MTYyMDUyNDY2NSwidXNlcm5hbWUiOiIzNjA3MzExOTk4MTExMTAzMTQifQ.ZaBF1K3QvSvyIz5in5_GLIfRqzqOP8aOnwjLwgl7aeg', 14.0, 0, 24, 6.00, '彭某某');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
