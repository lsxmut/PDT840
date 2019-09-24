
-- Table: sys_variable
CREATE TABLE sys_variable ( 
    id           BIGINT( 20 )    NOT NULL,
    code         VARCHAR( 32 )   NOT NULL,
    name         VARCHAR( 100 )  NOT NULL,
    value         VARCHAR( 255 ),
    parent_id    BIGINT( 20 ),
    memo         VARCHAR( 255 ),
    version      INT             NOT NULL DEFAULT 0,
    order_no     INT( 10 )       DEFAULT '0',
    del_flag     TINYINT( 1 )    NOT NULL DEFAULT '0',
    create_id    BIGINT( 20 ),
    date_created DATETIME       ,
    date_updated DATETIME       ,
    PRIMARY KEY ( id ),
    UNIQUE ( code ) 
);


-- INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727550, 'atlas_path_save', '数据分析-存入路径', "", '数据分析文件路径',null,0,strftime('%s000','now'),strftime('%s000','now'));
-- INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727551, 'atlas_path_import', '数据分析-导入路径', "", '数据分析文件路径',null,0,strftime('%s000','now'),strftime('%s000','now'));


INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727650, 'threshold-type', '阈值生效类型0客户端设备1服务端软件', 1, '阈值生效类型',null,0,strftime('%s000','now'),strftime('%s000','now'));
-- 预设-阈值
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727750, 'd-threshold', '预设-阈值', null, '预设-阈值',null,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727751, 'd-tevNormal', '地电波正常', '20', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727752, 'd-tevEarlyWarning', '地电波关注', '25', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727753, 'd-tevAbnormal', '地电波异常', '30', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727754, 'd-aaNormal', '空气式超声波正常', '6', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727755, 'd-aaEarlyWarning', '空气式超声波关注', '15', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727756, 'd-aaAbnormal', '空气式超声波异常', '20', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727757, 'd-hfctNormal', '高频电流正常', '600', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727758, 'd-hfctEarlyWarning', '高频电流关注', '3000', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727759, 'd-hfctAbnormal', '高频电流异常', '20000', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727760, 'd-aeNormal', '接触式超声波正常', '5', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727761, 'd-aeEarlyWarning', '接触式超声波关注', '15', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727762, 'd-aeAbnormal', '接触式超声波异常', '20', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727763, 'd-uhfNormal', '特高频正常', '-60', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727764, 'd-uhfEarlyWarning', '特高频关注', '-40', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727765, 'd-uhfAbnormal', '特高频异常', '-10', '预设-阈值',513542709101727750,0,strftime('%s000','now'),strftime('%s000','now'));
-- 当前-阈值
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727850, 'i-threshold', '当前-阈值', null, '当前-阈值',null,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727851, 'i-tevNormal', '地电波正常', '20', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727852, 'i-tevEarlyWarning', '地电波关注', '25', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727853, 'i-tevAbnormal', '地电波异常', '30', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727854, 'i-aaNormal', '空气式超声波正常', '6', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727855, 'i-aaEarlyWarning', '空气式超声波关注', '15', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727856, 'i-aaAbnormal', '空气式超声波异常', '20', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727857, 'i-hfctNormal', '高频电流正常', '600', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727858, 'i-hfctEarlyWarning', '高频电流关注', '3000', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727859, 'i-hfctAbnormal', '高频电流异常', '20000', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727860, 'i-aeNormal', '接触式超声波正常', '5', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727861, 'i-aeEarlyWarning', '接触式超声波关注', '15', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727862, 'i-aeAbnormal', '接触式超声波异常', '20', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727863, 'i-uhfNormal', '特高频正常', '-60', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727864, 'i-uhfEarlyWarning', '特高频关注', '-40', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727865, 'i-uhfAbnormal', '特高频异常', '-10', '当前-阈值',513542709101727850,0,strftime('%s000','now'),strftime('%s000','now'));
-- 路径设置
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727950, 'i-path', '路径设置', null, '路径设置',null,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727951, 'i-path-data', '数据文件存储路径', 'd:/pd400/data', '路径设置',513542709101727950,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727952, 'i-path-images', '图形文件路径', 'd:/pd400/images', '路径设置',513542709101727950,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727953, 'i-path-pdf', 'PDF文件路径', 'd:/pd400/pdf', '路径设置',513542709101727950,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727954, 'i-path-reports', '报表导出路径', 'd:/pd400/reports', '路径设置',513542709101727950,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727955, 'i-path-tests', '测试表路径', 'd:/pd400/tests', '路径设置',513542709101727950,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101727956, 'i-path-stations', '站点信息路径', 'd:/pd400/stations', '路径设置',513542709101727950,0,strftime('%s000','now'),strftime('%s000','now'));
-- 通信设置
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728050, 'i-socket', '通信设置', null, '通信设置',null,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728051, 'i-socket-wifi-ip', 'WIFI通信参数-本机ip', '127.0.0.1', '路径设置',513542709101728050,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728052, 'i-socket-wifi-port', 'WIFI通信参数-端口号', '12306', '路径设置',513542709101728050,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728053, 'i-socket-bluetooth', '蓝牙通信参数', '0000', '路径设置',513542709101728050,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728054, 'i-socket-password', '通信密码验收', '0000', '路径设置',513542709101728050,0,strftime('%s000','now'),strftime('%s000','now'));
-- -- 数据库设置
-- INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728150, 'i-db-back', '数据库备份设置', null, '数据库备份设置',null,0,strftime('%s000','now'),strftime('%s000','now'));
-- INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728151, 'i-db-back-name', '库备份-文件名称', 'pd400', '数据库备份设置',513542709101728150,0,strftime('%s000','now'),strftime('%s000','now'));
-- INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728152, 'i-db-back-dir', '库备份-文件夹路径', 'd:/pd400/db/back/', '数据库备份设置',513542709101728150,0,strftime('%s000','now'),strftime('%s000','now'));
-- INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728153, 'i-db-back-url', '库备份-文件具体地址', 'd:/pd400/db/back/pd400.db', '数据库备份设置',513542709101728150,0,strftime('%s000','now'),strftime('%s000','now'));
-- 软件锁屏时间
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728250, 'i-sys-timeout', '系统锁屏时间', '30', '系统锁屏时间',null,0,strftime('%s000','now'),strftime('%s000','now'));
-- 软件版本设置
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728350, 'i-version', '软件版本设置', null, '软件版本设置',null,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728351, 'i-version-name', '软件名称', '红相电力1', '软件版本设置',513542709101728350,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728352, 'i-version-code', '版本号', '0.0.0.1', '软件版本设置',513542709101728350,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728353, 'i-version-copyright', '版权归属', '©2018 hongxiang', '软件版本设置',513542709101728350,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728354, 'i-version-company-address', '公司地址', '深圳厦门', '软件版本设置',513542709101728350,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728355, 'i-version-company-website', '公司网址', 'http://hx.com', '软件版本设置',513542709101728350,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728356, 'i-version-company-icon', '软件图标', '/com/redphase/ui/icon.png', '软件版本设置',513542709101728350,0,strftime('%s000','now'),strftime('%s000','now'));
INSERT INTO [sys_variable] ([id], [code], [name], [value],[memo], [parent_id], [del_flag],[date_created],[date_updated]) VALUES (513542709101728357, 'i-version-company-logo', '公司LOGO', '/com/redphase/ui/logo.jpeg', '软件版本设置',513542709101728350,0,strftime('%s000','now'),strftime('%s000','now'));
