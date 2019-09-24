
-- Table: auth_role
CREATE TABLE auth_role ( 
    id           BIGINT( 20 )    NOT NULL,
    name         VARCHAR( 50 )   NOT NULL,
    is_super     TINYINT( 1 )    DEFAULT '0',
    memo         VARCHAR( 255 ),
    order_no     INT( 10 )       DEFAULT '0',
    version      INT             NOT NULL
                                 DEFAULT '0',
    keyword      VARCHAR( 255 ),
    del_flag     TINYINT( 1 )    NOT NULL
                                 DEFAULT '0',
    date_created DATETIME        DEFAULT ( ( strftime( '%s000', 'now' )  )  ),
    date_updated DATETIME        DEFAULT ( ( strftime( '%s000', 'now' )  )  ),
    PRIMARY KEY ( id ) 
);

INSERT INTO [auth_role] ([id], [name], [is_super], [memo], [order_no], [version], [keyword], [del_flag], [date_created], [date_updated]) VALUES (1, '超级管理员', 1, null, 0, 0, null, 0, 1526883520000, 1526888611000);
INSERT INTO [auth_role] ([id], [name], [is_super], [memo], [order_no], [version], [keyword], [del_flag], [date_created], [date_updated]) VALUES (2, '管理员', 0, null, 0, 0, null, 0, 1526888611000, 1526888611000);
INSERT INTO [auth_role] ([id], [name], [is_super], [memo], [order_no], [version], [keyword], [del_flag], [date_created], [date_updated]) VALUES (3, '中级用户', 0, null, 0, 0, null, 0, 1526896632000, 1526896632000);
INSERT INTO [auth_role] ([id], [name], [is_super], [memo], [order_no], [version], [keyword], [del_flag], [date_created], [date_updated]) VALUES (4, '初级用户', 0, null, 0, 0, null, 0, 1526896632000, 1526896632000);

INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (4,'id_analyze_menu', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (4,'id_trend_menu', 2);

INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_trend_menu', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_account_edit', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_account_findDataById', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_account_info', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_account_menu', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_analyze_menu', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_root', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_task_add', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_task_edit', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_task_info', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_task_menu', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (3,'id_task_phydel', 2);

INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_account_edit', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_trend_menu', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_account_findDataById', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_account_info', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_account_menu', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_alog_info', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_alog_del', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_alog_menu', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_analyze_menu', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_analyze_del', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_root', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_sys', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_sys_db', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_sys_path', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_sys_socket', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_sys_threshold', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_sys_timeout', 2);
-- INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_sys_version', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_task_add', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_task_edit', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_task_info', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_task_menu', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_task_phydel', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_user_add', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_user_del', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_user_edit', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_user_info', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_user_menu', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_user_pwd', 2);
INSERT INTO [auth_role_vs_perm] ([role_id], [perm_id], [create_id]) VALUES (2,'id_user_resetpwd', 2);
