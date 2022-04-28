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

UPDATE business_service SET createUser = 'mrizzi', createTime = '2019-01-01 00:00:00.407', updateUser = 'mrizzi', updateTime = CURRENT_TIMESTAMP, deleted = false WHERE id = 1;
UPDATE business_service SET createUser = 'foo', createTime = '2020-01-01 00:00:00', updateUser = 'mrizzi', updateTime = CURRENT_TIMESTAMP, deleted = false WHERE id = 2;
UPDATE business_service SET createUser = 'foo', createTime = '2021-01-01 00:00:00', deleted = false WHERE id = 3;

