drop table  IF EXISTS base;
# 房间表
create table base
(
    id          bigint auto_increment primary key comment '程序查询，更新时使用id',
    code        varchar(20)                         not null comment '基本结构-$(表)不可读唯一标识编码，表名+随机code+原始id，取代id迁移困难',
    name        varchar(40)                         null comment '基本结构-$(表)可读姓名标识',
    creator     varchar(20)                         null comment '创建者code',
    mender      varchar(20)                         null comment '修改者code',
    create_time timestamp default CURRENT_TIMESTAMP null,
    modify_time timestamp default CURRENT_TIMESTAMP null,
    is_del      bit       default b'0'              null comment '是否被删除。1是0否',
    ext_1       int                                 null comment '扩展字段，存储状态或数字，最大2^32',
    ext_2       varchar(40)                         null comment '扩展字段，最多存储40字符长度',
    ext_3       json                                null comment '存储json字段',

)
    comment '基本表';

create index base_is_del_index
    on base (is_del);

create index base_creator_index
    on base (creator);

create index base_mender_index
    on base (mender);

