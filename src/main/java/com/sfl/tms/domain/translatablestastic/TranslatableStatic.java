package com.sfl.tms.domain.translatablestastic;

import com.sfl.tms.domain.AbstractEntity;
import com.sfl.tms.domain.language.Language;
import com.sfl.tms.domain.translatable.TranslatableEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Vazgen Danielyan
 * Date: 12/11/18
 * Time: 1:25 AM
 */
@Entity
@Table(
        name = "translatable_static",
        indexes = {
                @Index(name = "idx_ts_key", columnList = "key"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_ts_key_entity_id_language_id", columnNames = {"key", "entity_id", "language_id"})
        }
)
@SequenceGenerator(name = "sequence_generator", sequenceName = "translatable_static_seq", allocationSize = 1)
public class TranslatableStatic extends AbstractEntity {

    private static final long serialVersionUID = -5107763511936515375L;

    //region Properties

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "value", nullable = false, length = 5000)
    private String value;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language_id", nullable = false, foreignKey = @ForeignKey(name = "fk_te_language_id"))
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false, foreignKey = @ForeignKey(name = "fk_te_entity_id"))
    private TranslatableEntity entity;

    //endregion

    //region Getters and setters

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(final Language language) {
        this.language = language;
    }

    public TranslatableEntity getEntity() {
        return entity;
    }

    public void setEntity(final TranslatableEntity entity) {
        this.entity = entity;
    }

    //endregion

    public TranslatableStatic copy() {
        final TranslatableStatic translatableStatic = new TranslatableStatic();
        translatableStatic.key = key;
        translatableStatic.value = value;
        translatableStatic.language = language;
        return translatableStatic;
    }

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
        TranslatableStatic rhs = (TranslatableStatic) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.key, rhs.key)
                .append(this.value, rhs.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(key)
                .append(value)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("key", key)
                .append("value", value)
                .append("language", language)
                .toString();
    }

    //endregion
}
