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

INSERT INTO business_service (id, name, description) VALUES (nextval('hibernate_sequence'), 'Home Banking BU', 'Important service to let private customer use their home banking accounts');
INSERT INTO business_service (id, name, description) VALUES (nextval('hibernate_sequence'), 'Online Investments service', 'Corporate customers investments management');
INSERT INTO business_service (id, name, description) VALUES (nextval('hibernate_sequence'), 'Credit Cards BS', 'Internal credit card creation and management service');
