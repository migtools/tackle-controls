-- add new columns
alter table if exists stakeholder
    add column displayName varchar(255),
    add column jobFunction varchar(255);
-- insert data new columns, eventually with values from columns to be removed
UPDATE stakeholder
SET displayName = CONCAT(name, ' ', surname);
-- remove useless columns
alter table if exists stakeholder
    DROP column name,
    DROP column surname;