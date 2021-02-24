create table stakeholder (
     id int8 not null,
     createTime timestamp,
     createUser varchar(255),
     deleted boolean,
     updateTime timestamp,
     updateUser varchar(255),
     email varchar(255),
     name varchar(255),
     surname varchar(255),
     primary key (id)
);
