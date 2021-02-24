create sequence hibernate_sequence start 1 increment 1;
create table business_service (
    id int8 not null,
    description varchar(255),
    name varchar(255),
    primary key (id)
);
