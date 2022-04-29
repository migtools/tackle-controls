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