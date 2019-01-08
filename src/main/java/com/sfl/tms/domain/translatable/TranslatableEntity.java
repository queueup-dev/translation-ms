package com.sfl.tms.domain.translatable;

import com.sfl.tms.domain.AbstractEntity;
import com.sfl.tms.domain.translatablestastic.TranslatableStatic;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
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
                @Index(name = "idx_translatable_entity_uuid", columnList = "uuid")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_translatable_entity_uuid", columnNames = "uuid")
        }
)
@SequenceGenerator(name = "sequence_generator", sequenceName = "translatable_entity_seq", allocationSize = 1)
public class TranslatableEntity extends AbstractEntity {

    private static final long serialVersionUID = -2779008497944621143L;

    //region Properties

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entity")
    private Set<TranslatableEntityField> fields;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "entity")
    private Set<TranslatableStatic> statics;

    //endregion

    //region Getters and setters

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
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

    public Set<TranslatableStatic> getStatics() {
        return statics;
    }

    public void setStatics(final Set<TranslatableStatic> statics) {
        this.statics = statics;
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
                .append(this.name, rhs.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(uuid)
                .append(name)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("uuid", uuid)
                .append("name", name)
                .toString();
    }

    //endregion
}
