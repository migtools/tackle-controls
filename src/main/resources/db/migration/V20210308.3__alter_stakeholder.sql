-- alter existing 'jobFunction' column
-- allowed because not in production yet: remove the 'jobFunction' column
alter table if exists stakeholder
    add column jobFunction_id int8,
    drop column jobFunction,
    add constraint FK298p7tiy7e6pljwoxiwn9tjx2
    foreign key (jobFunction_id)
    references job_function;
