package io.tackle.controls.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.tackle.commons.annotations.Filterable;
import io.tackle.commons.entities.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JsonBackReference
    @JoinTable(
            name = "stakeholdergroup_stakeholders",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "stakeholder_id")}
    )
    public List<Stakeholder> stakeholders = new ArrayList<>();

    @Filterable
    public Integer members;

    @PreRemove
    private void preRemove() {
        stakeholders.forEach(stakeholder -> stakeholder.stakeholderGroups.remove(this));
    }
}
