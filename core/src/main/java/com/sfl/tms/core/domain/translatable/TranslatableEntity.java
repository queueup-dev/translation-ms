package com.sfl.tms.core.domain.translatable;

import com.sfl.tms.core.domain.AbstractEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import jakarta.persistence.*;
import java.util.Set;

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 5:21 PM
 */
@Entity
@Table(
        name = "translatable_entity",
        indexes = {
                @Index(name = "idx_te_uuid", columnList = "uuid"),
                @Index(name = "idx_te_label", columnList = "label")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_te_uuid_label", columnNames = {"uuid", "label"})
        }
)
@SequenceGenerator(name = "sequence_generator", sequenceName = "translatable_entity_seq", allocationSize = 1)
public class TranslatableEntity extends AbstractEntity {

    private static final long serialVersionUID = -2779008497944621143L;

    //region Properties

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entity")
    private Set<TranslatableEntityField> fields;

    //endregion

    //region Getters and setters

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<TranslatableEntityField> getFields() {
        return fields;
    }

    public void setFields(final Set<TranslatableEntityField> fields) {
        this.fields = fields;
    }

    //endregion

    //region Equals, Hashcode and toString

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        TranslatableEntity rhs = (TranslatableEntity) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.uuid, rhs.uuid)
                .append(this.label, rhs.label)
                .append(this.name, rhs.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(uuid)
                .append(name)
                .append(label)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("uuid", uuid)
                .append("label", label)
                .append("name", name)
                .toString();
    }

    //endregion
}
