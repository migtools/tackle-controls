alter table if exists stakeholdergroup_stakeholders
       add constraint FKrvendnekhlrq2hggkwdkxg4j0
       foreign key (stakeholder_id)
       references stakeholder;
alter table if exists stakeholdergroup_stakeholders
       add constraint FKmklbx86c9ehi6njo6d37nud50
       foreign key (group_id)
       references stakeholder_group;
