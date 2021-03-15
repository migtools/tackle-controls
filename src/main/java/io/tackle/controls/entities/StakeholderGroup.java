package io.tackle.controls.entities;

import io.tackle.commons.annotations.Filterable;
import io.tackle.commons.entities.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stakeholder_group")
@SQLDelete(sql = "UPDATE stakeholder_group SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class StakeholderGroup extends AbstractEntity {
    @Filterable
    public String name;
    @Filterable
    public String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "stakeholdergroup_stakeholders",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "stakeholder_id")}
    )
    @Filterable(filterName = "stakeholders.displayName")
    public List<Stakeholder> stakeholders = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        stakeholders.forEach(stakeholder -> stakeholder.stakeholderGroups.remove(this));
    }
}
