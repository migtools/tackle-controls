create table stakeholder_group (
       id int8 not null,
        createTime timestamp,
        createUser varchar(255),
        deleted boolean,
        updateTime timestamp,
        updateUser varchar(255),
        description varchar(255),
        members int4,
        name varchar(255),
        primary key (id)
);
