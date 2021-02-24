UPDATE business_service SET createUser = 'mrizzi', createTime = '2019-01-01 00:00:00.407', updateUser = 'mrizzi', updateTime = CURRENT_TIMESTAMP, deleted = false WHERE id = 1;
UPDATE business_service SET createUser = 'foo', createTime = '2020-01-01 00:00:00', updateUser = 'mrizzi', updateTime = CURRENT_TIMESTAMP, deleted = false WHERE id = 2;
UPDATE business_service SET createUser = 'foo', createTime = '2021-01-01 00:00:00', deleted = false WHERE id = 3;

