package com.sfl.qup.tms.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User: Vazgen Danielyan
 * Date: 2/5/17
 * Time: 4:34 PM
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = -7789823601518251030L;

    //region Properties

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "removed")
    private LocalDateTime removed;

    @Column(name = "updated", nullable = false)
    private LocalDateTime updated;

    //endregion

    //region Constructor

    public AbstractEntity() {
        setCreated(LocalDateTime.now());
        setUpdated(getCreated());
    }

    //endregion

    //region Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(final LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getRemoved() {
        return removed;
    }

    public void setRemoved(final LocalDateTime removed) {
        this.removed = removed;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(final LocalDateTime updated) {
        this.updated = updated;
    }

    //endregion

    //region Equals hashcode and toString

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
        AbstractEntity rhs = (AbstractEntity) obj;
        return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.created, rhs.created)
                .append(this.removed, rhs.removed)
                .append(this.updated, rhs.updated)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(created)
                .append(removed)
                .append(updated)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("created", created)
                .append("removed", removed)
                .append("updated", updated)
                .toString();
    }

    //endregion

    //region Utility methods

    protected static Long getIdOrNull(final AbstractEntity entity) {
        return entity == null ? null : entity.getId();
    }

    //endregion
}