package com.sfl.qup.tms.domain.translatable;

import com.sfl.qup.tms.domain.AbstractEntity;
import com.sfl.qup.tms.domain.language.Language;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * User: Vazgen Danielyan
 * Date: 12/4/18
 * Time: 5:24 PM
 */
@Entity
@Table(
        name = "translatable_entity_translation",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_translatable_entity_translation_entity_language", columnNames = {"entity_id", "language_id"})
        }
)
@SequenceGenerator(name = "sequence_generator", sequenceName = "translatable_entity_translation_seq", allocationSize = 1)
public class TranslatableEntityTranslation extends AbstractEntity {

    private static final long serialVersionUID = 3735056767504297785L;

    //region Properties

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id", nullable = false, foreignKey = @ForeignKey(name = "fk_translatable_entity_translation_entity_id"))
    private TranslatableEntity entity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language_id", nullable = false, foreignKey = @ForeignKey(name = "fk_translatable_entity_translation_language_id"))
    private Language language;

    //endregion

    //region Getters and setters

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public TranslatableEntity getEntity() {
        return entity;
    }

    public void setEntity(final TranslatableEntity entity) {
        this.entity = entity;
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
        TranslatableEntityTranslation rhs = (TranslatableEntityTranslation) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.text, rhs.text)
                .append(this.entity, rhs.entity)
                .append(this.language, rhs.language)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(text)
                .append(entity)
                .append(language)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("text", text)
                .append("entity", entity)
                .append("language", language)
                .toString();
    }

    //endregion
}
