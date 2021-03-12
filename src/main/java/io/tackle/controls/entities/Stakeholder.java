package io.tackle.controls.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.tackle.commons.entities.AbstractEntity;
import io.tackle.commons.annotations.Filterable;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stakeholder")
@SQLDelete(sql = "UPDATE stakeholder SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Stakeholder extends AbstractEntity {
    @Filterable
    public String displayName;
    @ManyToOne
    @Filterable
    public JobFunction jobFunction;
    @Filterable
    public String email;
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @JsonBackReference
    public List<BusinessService> businessServices = new ArrayList<>();

    @ManyToMany(mappedBy="stakeholders", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnore
    public List<StakeholderGroup> stakeholderGroups = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        businessServices.forEach(businessService -> businessService.owner = null);
        stakeholderGroups.forEach(stakeholderGroup -> stakeholderGroup.stakeholders.remove(this));
    }
}
