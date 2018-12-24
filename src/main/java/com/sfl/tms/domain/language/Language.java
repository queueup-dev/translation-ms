package com.sfl.tms.domain.language;

import com.sfl.tms.domain.AbstractEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 5:16 PM
 */
@Entity
@Table(name = "language",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_language_lang", columnNames = "lang")
        }
)
@SequenceGenerator(name = "sequence_generator", sequenceName = "language_seq", allocationSize = 1)
public class Language extends AbstractEntity {

    private static final long serialVersionUID = 741452657069094371L;

    //region Properties

    @Column(name = "lang", nullable = false)
    private String lang;

    //endregion

    //region Getters and setters

    public String getLang() {
        return lang;
    }

    public void setLang(final String name) {
        this.lang = name;
    }

    //endregion

    //region Equals, hashCode and toString

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
        Language rhs = (Language) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.lang, rhs.lang)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(lang)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("lang", lang)
                .toString();
    }

    //endregion
}
