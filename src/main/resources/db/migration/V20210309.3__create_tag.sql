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

create table tag (
     id int8 not null,
     createTime timestamp,
     createUser varchar(255),
     deleted boolean,
     updateTime timestamp,
     updateUser varchar(255),
     name varchar(255),
     tagType_id int8 not null,
     primary key (id)
);
alter table if exists tag
    add constraint FKq664i9aw1wjdlptk0q5dapim7
    foreign key (tagType_id)
    references tag_type;