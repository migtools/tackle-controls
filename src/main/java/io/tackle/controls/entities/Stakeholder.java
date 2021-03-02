package io.tackle.controls.entities;

import io.tackle.commons.entities.AbstractEntity;
import io.tackle.commons.annotations.Filterable;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stakeholder")
@SQLDelete(sql = "UPDATE stakeholder SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Stakeholder extends AbstractEntity {
    @Filterable
    public String displayName;
    @Filterable
    public String jobFunction;
    @Filterable
    public String email;
}
