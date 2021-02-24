alter table if exists business_service
    add column createUser varchar (255),
    add column createTime timestamp,
    add column owner_id int8,
    add column deleted boolean,
    add column updateTime timestamp,
    add column updateUser varchar(255);
