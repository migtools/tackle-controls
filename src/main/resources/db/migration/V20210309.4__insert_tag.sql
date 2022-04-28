--
-- Copyright © 2021 Konveyor (https://konveyor.io/)
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'COTS', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Application Type';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'In house', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Application Type';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'SaaS', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Application Type';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Boston (USA)', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Data Center';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'London (UK)', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Data Center';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Paris (FR)', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Data Center';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Sydney (AU)', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Data Center';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'DB2', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Database';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'MongoDB', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Database';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Oracle', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Database';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Postgresql', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Database';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'SQL Server', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Database';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'C# ASP .Net', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Language';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'C++', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Language';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'COBOL', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Language';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Java', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Language';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Javascript', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Language';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Python', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Language';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'RHEL 8', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Operating System';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Windows Server 2016', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Operating System';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Z/OS', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Operating System';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'EAP', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Runtime';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'JWS', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Runtime';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Quarkus', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Runtime';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Spring Boot', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Runtime';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'Tomcat', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Runtime';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'WebLogic', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Runtime';
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted)
SELECT nextval('hibernate_sequence'), 'WebSphere', id, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false
FROM tag_type
WHERE name = 'Runtime';
