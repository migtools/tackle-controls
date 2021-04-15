create table tag_type (
      id int8 not null,
      createTime timestamp,
      createUser varchar(255),
      deleted boolean,
      updateTime timestamp,
      updateUser varchar(255),
      colour varchar(255),
      name varchar(255),
      rank int4,
      primary key (id)
);

