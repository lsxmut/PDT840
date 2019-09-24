-- Table: auth_perm
CREATE TABLE auth_perm (
    id           VARCHAR( 32 )   NOT NULL,
    name         VARCHAR( 50 )   NOT NULL,
    match_str    VARCHAR( 50 )   NOT NULL,
    parent_id    VARCHAR( 32 ),
    memo         VARCHAR( 255 ),
    order_no     INT( 10 )       DEFAULT '0',
    version      INT             DEFAULT '0',
    keyword      VARCHAR( 255 ),
    del_flag     TINYINT( 1 )    NOT NULL DEFAULT '0',
    create_id    BIGINT( 20 ),
    date_created DATETIME        DEFAULT ( ( strftime( '%s000', 'now' )  )  ),
    date_updated DATETIME        DEFAULT ( ( strftime( '%s000', 'now' )  )  ),
    nodel_flag   TINYINT( 1 )    DEFAULT '0',
    PRIMARY KEY ( id ),
    UNIQUE ( match_str )
);

   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_root','root','root',null,'系统生成');

   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_account_menu','台账管理','account:menu','id_root','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_account_info','台账-详情','account:info','id_account_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_account_add','台账-新增','account:add','id_account_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_account_edit','台账-编辑','account:edit','id_account_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_account_findDataById','台账-详情2','account:findDataById','id_account_menu','系统生成');

--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_accountSiteInfo_menu','台账位置管理','accountSiteInfo:menu','id_root','系统生成');
--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_accountSiteInfo_info','台账位置-详情','accountSiteInfo:info','id_accountSiteInfo_menu','系统生成');
--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_accountSiteInfo_phydel','台账位置-删除','accountSiteInfo:phydel','id_accountSiteInfo_menu','系统生成');
--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_accountSiteInfo_add','台账位置-新增','accountSiteInfo:add','id_accountSiteInfo_menu','系统生成');
--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_accountSiteInfo_edit','台账位置-编辑','accountSiteInfo:edit','id_accountSiteInfo_menu','系统生成');

   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_task_menu','任务单管理','task:menu','id_root','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_task_info','任务单-详情','task:info','id_task_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_task_phydel','任务单-删除','task:phydel','id_task_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_task_add','任务单-新增','task:add','id_task_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_task_edit','任务单-编辑','task:edit','id_task_menu','系统生成');

--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_taskDetail_menu','任务单详情管理','taskDetail:menu','id_root','系统生成');
--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_taskDetail_info','任务单详情-查看','taskDetail:info','id_taskDetail_menu','系统生成');
--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_taskDetail_phydel','任务单详情-删除','taskDetail:phydel','id_taskDetail_menu','系统生成');
--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_taskDetail_add','任务单详情-新增','taskDetail:add','id_taskDetail_menu','系统生成');
--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_taskDetail_edit','任务单详情-编辑','taskDetail:edit','id_taskDetail_menu','系统生成');

   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_analyze_menu','数据分析','analyze:menu','id_root','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_analyze_del','数据分析-删除','analyze:del','id_root','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_trend_menu','历史趋势','trend:menu','id_root','系统生成');

   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_user_menu','用户管理','user:menu','id_root','系统生成');
--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_user_recycle','用户-回收站','user:recycle','id_user_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_user_info','用户-详情','user:info','id_user_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_user_del','用户-删除','user:del','id_user_menu','系统生成');
--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_user_recovery','用户-恢复','user:recovery','id_user_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_user_add','用户-新增','user:add','id_user_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_user_edit','用户-编辑','user:edit','id_user_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_user_pwd','用户-密码修改','user:edit:pwd','id_user_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_user_resetpwd','用户-重置密码','user:edit:resetpwd','id_user_menu','系统生成');
--    INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_user_role','用户-角色管理','user:edit:role','id_user_menu','系统生成');

   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_alog_menu','操作日志','alog:menu','id_root','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_alog_info','操作日志-详情','alog:info','id_alog_menu','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_alog_del','操作日志-删除','alog:del','id_alog_menu','系统生成');

   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_sys','系统设置','sys:menu','id_root','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_sys_threshold','系统设置-阈值设置','sys:threshold','id_sys','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_sys_path','系统设置-路径设置','sys:path','id_sys','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_sys_socket','系统设置-通信设置','sys:socket','id_sys','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_sys_timeout','系统设置-系统保护','sys:timeout','id_sys','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_sys_db','系统设置-数据库备份还原','sys:db','id_sys','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_sys_version','系统设置-版本信息','sys:version','id_sys','系统生成');
   INSERT INTO [auth_perm] ([id], [name], [match_str], [parent_id], [memo]) VALUES ('id_sys_super','系统设置-超级用户设置','sys:super','id_sys','系统生成');
