create table job_function (
      id int8 not null,
      createTime timestamp,
      createUser varchar(255),
      deleted boolean,
      updateTime timestamp,
      updateUser varchar(255),
      role varchar(255),
      primary key (id)
);
