INSERT INTO stakeholder (id, name, surname, email, createUser, createTime, deleted) VALUES (nextval('hibernate_sequence'), 'Jessica', 'Fletcher', 'jbfletcher@murdershewrote.series', 'mrizzi', CURRENT_TIMESTAMP, false);
INSERT INTO stakeholder (id, name, surname, email, createUser, createTime, deleted) VALUES (nextval('hibernate_sequence'), 'Emmett', 'Brown', 'doc@greatscott.movie', 'mrizzi', CURRENT_TIMESTAMP, false);
UPDATE business_service SET owner_id = 4, updateTime = CURRENT_TIMESTAMP WHERE id = 1;
UPDATE business_service SET owner_id = 5, updateTime = CURRENT_TIMESTAMP WHERE id = 2;
