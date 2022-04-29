/*
 * Copyright © 2021 the Konveyor Contributors (https://konveyor.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.tackle.controls.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.tackle.commons.annotations.Filterable;
import io.tackle.commons.entities.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "stakeholder")
@SQLDelete(sql = "UPDATE stakeholder SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Stakeholder extends AbstractEntity {
    @Filterable
    public String displayName;
    @ManyToOne
    @Filterable(filterName = "jobFunction.role")
    public JobFunction jobFunction;
    @Filterable
    public String email;
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @JsonBackReference("businessServicesReference")
    public List<BusinessService> businessServices = new ArrayList<>();

    @ManyToMany(mappedBy="stakeholders", fetch = FetchType.LAZY)
    @JsonBackReference("stakeholderGroupsReference")
    @Filterable(filterName = "stakeholderGroups.name")
    public Set<StakeholderGroup> stakeholderGroups = new HashSet<>();

    @PreRemove
    private void preRemove() {
        businessServices.forEach(businessService -> businessService.owner = null);
        stakeholderGroups.forEach(stakeholderGroup -> stakeholderGroup.stakeholders.remove(this));
    }

    @PreUpdate
    private void preUpdate() {
        stakeholderGroups.forEach(stakeholderGroup -> stakeholderGroup.stakeholders.add(this));
    }

    @PostPersist
    private void postPersist() {
        // using the iterator lets us remove the not-valid referenced StakeholderGroup IDs
        Iterator<StakeholderGroup> iter = stakeholderGroups.iterator();
        while (iter.hasNext()) {
            StakeholderGroup stakeholderGroupFromDb = StakeholderGroup.findById(iter.next().id);
            if (stakeholderGroupFromDb != null) stakeholderGroupFromDb.stakeholders.add(this);
            else iter.remove();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stakeholder)) return false;
        Stakeholder stakeholder = (Stakeholder) o;
        return Objects.equals(id, stakeholder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
