package com.sfl.qup.tms.domain.translatablestastic;

import com.sfl.qup.tms.domain.AbstractEntity;
import com.sfl.qup.tms.domain.language.Language;
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
                @Index(name = "idx_translatable_static_key", columnList = "key"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_translatable_static_uuid", columnNames = {"key", "language_id"})
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
