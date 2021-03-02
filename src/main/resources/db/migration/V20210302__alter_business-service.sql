alter table if exists business_service
    add constraint UK_btcp3rc4skxn0oyxbbnok68do unique (name),
    add constraint FK64f0d7vxcp60lyjlttb7dqk3t
    foreign key (owner_id)
    references stakeholder;
