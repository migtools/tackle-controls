create table tag (
     id int8 not null,
     createTime timestamp,
     createUser varchar(255),
     deleted boolean,
     updateTime timestamp,
     updateUser varchar(255),
     name varchar(255),
     tagType_id int8 not null,
     primary key (id)
);
alter table if exists tag
    add constraint FKq664i9aw1wjdlptk0q5dapim7
    foreign key (tagType_id)
    references tag_type;