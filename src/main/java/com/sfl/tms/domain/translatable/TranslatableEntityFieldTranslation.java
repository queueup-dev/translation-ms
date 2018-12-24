package com.sfl.tms.domain.translatable;

import com.sfl.tms.domain.AbstractEntity;
import com.sfl.tms.domain.language.Language;
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
        name = "translatable_entity_field_translation",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tef_field_language", columnNames = {"field_id", "language_id"})
        }
)
@SequenceGenerator(name = "sequence_generator", sequenceName = "translatable_entity_translation_seq", allocationSize = 1)
public class TranslatableEntityFieldTranslation extends AbstractEntity {

    private static final long serialVersionUID = 1588050404336971076L;

    //region Properties

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tef_field_id"))
    private TranslatableEntityField field;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tef_language_id"))
    private Language language;

    //endregion

    //region Getters and setters

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public TranslatableEntityField getField() {
        return field;
    }

    public void setField(final TranslatableEntityField field) {
        this.field = field;
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
        TranslatableEntityFieldTranslation rhs = (TranslatableEntityFieldTranslation) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.text, rhs.text)
                .append(this.field, rhs.field)
                .append(this.language, rhs.language)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(text)
                .append(field)
                .append(language)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("text", text)
                .append("field", field)
                .append("language", language)
                .toString();
    }

    //endregion
}
