--
-- Copyright © 2021 the Konveyor Contributors (https://konveyor.io/)
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

INSERT INTO tag_type (id, name, rank, colour, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Application Type', 6, '#ec7a08', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag_type (id, name, rank, colour, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Data Center', 5, '#2b9af3', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag_type (id, name, rank, colour, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Database', 4, '#6ec664', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag_type (id, name, rank, colour, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Language', 1, '#009596', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag_type (id, name, rank, colour, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Operating System', 2, '#a18fff', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
INSERT INTO tag_type (id, name, rank, colour, createUser, createTime, updateUser, updateTime, deleted) VALUES (nextval('hibernate_sequence'), 'Runtime', 3, '#7d1007', '<shipped-data>', CURRENT_TIMESTAMP, '<shipped-data>', CURRENT_TIMESTAMP, false);
