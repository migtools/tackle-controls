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

INSERT INTO stakeholder (id, name, surname, email, createUser, createTime, deleted) VALUES (nextval('hibernate_sequence'), 'Jessica', 'Fletcher', 'jbfletcher@murdershewrote.series', 'mrizzi', CURRENT_TIMESTAMP, false);
INSERT INTO stakeholder (id, name, surname, email, createUser, createTime, deleted) VALUES (nextval('hibernate_sequence'), 'Emmett', 'Brown', 'doc@greatscott.movie', 'mrizzi', CURRENT_TIMESTAMP, false);
UPDATE business_service SET owner_id = 4, updateTime = CURRENT_TIMESTAMP WHERE id = 1;
UPDATE business_service SET owner_id = 5, updateTime = CURRENT_TIMESTAMP WHERE id = 2;
