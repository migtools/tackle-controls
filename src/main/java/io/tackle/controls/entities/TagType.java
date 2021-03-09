package io.tackle.controls.entities;

import io.tackle.commons.annotations.Filterable;
import io.tackle.commons.entities.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tag_type")
@SQLDelete(sql = "UPDATE tag_type SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class TagType extends AbstractEntity {
    @Filterable
    public String name;
    @Filterable
    public Integer rank;
    @Filterable
    public String colour;
}
