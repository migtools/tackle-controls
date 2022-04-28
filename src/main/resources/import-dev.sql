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

INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Business Analyst', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Business Service Owner / Manager', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO stakeholder (id, displayName, email, jobFunction_id, createUser, createTime, deleted) VALUES (nextval('hibernate_sequence'), 'Jessica Fletcher', 'jbfletcher@murdershewrote.com', 1, 'mrizzi', CURRENT_TIMESTAMP, false);
INSERT INTO stakeholder (id, displayName, email, jobFunction_id, createUser, createTime, deleted) VALUES (nextval('hibernate_sequence'), 'Emmett Brown', 'doc@greatscott.movie', 2, 'mrizzi', CURRENT_TIMESTAMP, false);
INSERT INTO business_service (id, name, description, owner_id, createUser, createTime, deleted) VALUES (nextval('hibernate_sequence'), 'Home Banking BU', 'Important service to let private customer use their home banking accounts', 3, 'mrizzi', CURRENT_TIMESTAMP, false);
INSERT INTO business_service (id, name, description, owner_id, createUser, createTime, deleted) VALUES (nextval('hibernate_sequence'), 'Online Investments service', 'Corporate customers investments management', 4, 'foo', CURRENT_TIMESTAMP, false);
INSERT INTO business_service (id, name, description, deleted) VALUES (nextval('hibernate_sequence'), 'Credit Cards BS', 'Internal credit card creation and management service', false);
INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Consultant', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'DBA', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Developer / Software Engineer', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'IT Operations', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Program Manager', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Project Manager', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Service Owner', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Solution Architect', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'System Administrator', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO job_function (id, role, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Test Analyst / Manager', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag_type (id, name, rank, colour, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Runtime', 1, '#123456', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag_type (id, name, rank, colour, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Operating System', 3, '#111111', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag_type (id, name, rank, colour, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Data Center', 101, '#999999', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'EAP', 18, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'JWS', 18, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Quarkus', 18, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Spring Boot', 18, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'RHEL 8', 19, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag (id, name, tagType_id, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Windows Server 2016', 19, '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO stakeholder_group (id, name, description, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Managers', 'Managers Group', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO stakeholder_group (id, name, description, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Engineers', 'Engineers Group', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO stakeholdergroup_stakeholders (group_id, stakeholder_id) VALUES (27, 4);
INSERT INTO stakeholdergroup_stakeholders (group_id, stakeholder_id) VALUES (28, 3);