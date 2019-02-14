package com.sfl.tms.service.translatable.field.impl

import com.sfl.tms.core.domain.language.Language
import com.sfl.tms.core.domain.translatable.TranslatableEntity
import com.sfl.tms.core.domain.translatable.TranslatableEntityField
import com.sfl.tms.core.domain.translatable.TranslatableEntityFieldTranslation
import com.sfl.tms.core.domain.translatable.TranslatableEntityFieldType
import com.sfl.tms.core.persistence.translatable.TranslatableEntityFieldRepository
import com.sfl.tms.core.service.config.ServicesProperties
import com.sfl.tms.core.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.core.service.translatable.entity.exception.TranslatableEntityNotFoundException
import com.sfl.tms.core.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.core.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.tms.core.service.translatable.field.exception.TranslatableEntityFieldExistsForTranslatableEntityException
import com.sfl.tms.core.service.translatable.field.exception.TranslatableEntityFieldNotFoundException
import com.sfl.tms.core.service.translatable.field.impl.TranslatableEntityFieldServiceImpl
import com.sfl.tms.core.service.translatable.translation.TranslatableEntityFieldTranslationService
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * User: Vazgen Danielyan
 * Date: 12/5/18
 * Time: 6:21 PM
 */
@RunWith(MockitoJUnitRunner::class)
class TranslatableEntityFieldServiceImplTest {

    //region Injection

    @Mock
    private lateinit var translatableEntityService: TranslatableEntityService

    @Mock
    private lateinit var translatableEntityFieldRepository: TranslatableEntityFieldRepository

    @Mock
    private lateinit var translatableEntityFieldTranslationService: TranslatableEntityFieldTranslationService

    @InjectMocks
    private var translatableEntityFieldService: TranslatableEntityFieldService = TranslatableEntityFieldServiceImpl()

    //endregion

    //region findByKeyAndEntityUuidAndEntityLabelTest

    @Test
    fun findByKeyAndEntityTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val type = TranslatableEntityFieldType.STATIC
        val entity = TranslatableEntity()
        val field = TranslatableEntityField().apply { this.key = key }.apply { this.entity = entity }
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByKeyAndTypeAndEntity(key, type, entity)).thenReturn(field)
        // sut
        val result = translatableEntityFieldService.findByKeyAndTypeAndEntity(key, type, uuid, label)
        // verify
        verify(translatableEntityService, times(1)).getByUuidAndLabel(uuid, label)
        verify(translatableEntityFieldRepository, times(1)).findByKeyAndTypeAndEntity(key, type, entity)

        Assert.assertNotNull(result)
        Assert.assertEquals(key, result!!.key)
    }

    //endregion

    //region getByKeyAndTypeAndEntity

    @Test(expected = TranslatableEntityFieldNotFoundException::class)
    fun getByKeyAndEntityWhenTranslatableEntityFieldNotFoundTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val type = TranslatableEntityFieldType.STATIC
        val entity = TranslatableEntity()
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByKeyAndTypeAndEntity(key, type, entity)).thenReturn(null)
        // sut
        translatableEntityFieldService.getByKeyAndTypeAndEntity(key, type, uuid, label)
        // verify
        verify(translatableEntityService, times(1)).getByUuidAndLabel(uuid, label)
        verify(translatableEntityFieldRepository, times(1)).findByKeyAndTypeAndEntity(key, type, entity)
    }

    @Test
    fun getByKeyAndEntityTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val type = TranslatableEntityFieldType.STATIC
        val entity = TranslatableEntity()
        val field = TranslatableEntityField().apply { this.key = key }
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByKeyAndTypeAndEntity(key, type, entity)).thenReturn(field)
        // sut
        val result = translatableEntityFieldService.getByKeyAndTypeAndEntity(key, type, uuid, label)
        // verify
        verify(translatableEntityService, times(1)).getByUuidAndLabel(uuid, label)
        verify(translatableEntityFieldRepository, times(1)).findByKeyAndTypeAndEntity(key, type, entity)

        Assert.assertEquals(key, result.key)
    }

    //endregion

    //region create

    @Test(expected = TranslatableEntityNotFoundException::class)
    fun createTranslatableEntityFieldWhenTranslatableEntityNotFoundTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val type = TranslatableEntityFieldType.STATIC
        val dto = TranslatableEntityFieldDto(key, type, uuid, label)
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenThrow(TranslatableEntityNotFoundException::class.java)
        // sut
        translatableEntityFieldService.create(dto)
        // verify
        verify(translatableEntityService, times(1)).getByUuidAndLabel(uuid, label)
    }

    @Test(expected = TranslatableEntityFieldExistsForTranslatableEntityException::class)
    fun createTranslatableEntityFieldWhenTranslatableEntityFieldExistTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val type = TranslatableEntityFieldType.STATIC
        val dto = TranslatableEntityFieldDto(key, type, uuid, label)
        val entity = TranslatableEntity()
        val field = TranslatableEntityField().apply { this.entity = entity }
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByKeyAndTypeAndEntity(key, type, entity)).thenReturn(field)
        // sut
        translatableEntityFieldService.create(dto)
        // verify
        verify(translatableEntityService, times(1)).getByUuidAndLabel(uuid, label)
        verify(translatableEntityFieldRepository, times(1)).findByKeyAndTypeAndEntity(key, type, entity)
    }

    @Test
    fun createTranslatableEntityFieldTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val type = TranslatableEntityFieldType.STATIC
        val dto = TranslatableEntityFieldDto(key, type, uuid, label)
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.key = key }.apply { this.entity = entity }
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByKeyAndTypeAndEntity(key, type, entity)).thenReturn(null)
        `when`(translatableEntityFieldRepository.save(ArgumentMatchers.any(TranslatableEntityField::class.java))).thenReturn(field)
        // sut
        val result = translatableEntityFieldService.create(dto)
        // verify
        verify(translatableEntityService, times(2)).getByUuidAndLabel(uuid, label)
        verify(translatableEntityFieldRepository, times(1)).findByKeyAndTypeAndEntity(key, type, entity)
        verify(translatableEntityFieldRepository, times(1)).save(ArgumentMatchers.any(TranslatableEntityField::class.java))

        Assert.assertEquals(key, result.key)
        Assert.assertEquals(uuid, result.entity.uuid)
        Assert.assertEquals(label, result.entity.label)
    }

    //endregion

    //region copyStatic

    @Test
    fun copyStaticTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val value = "value"
        val language = Language().apply { this.lang = "en" }
        val templateEntity = TranslatableEntity()
            .apply { this.uuid = ServicesProperties.templateUuid }
            .apply { this.label = ServicesProperties.templateLabel }
            .apply {
                val templateEntity = this
                templateEntity.fields = setOf(
                    TranslatableEntityField()
                        .apply { this.key = key }
                        .apply { this.entity = templateEntity }
                        .apply { this.type = TranslatableEntityFieldType.STATIC }
                        .apply {
                            val translatableEntityField = this
                            translatableEntityField.translations = setOf(
                                TranslatableEntityFieldTranslation()
                                    .apply { this.value = value }
                                    .apply { this.field = translatableEntityField }
                                    .apply { this.language = language }
                            )
                        }
                )
            }
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.key = key }.apply { this.entity = entity }.apply { this.type = TranslatableEntityFieldType.STATIC }
        val translation = TranslatableEntityFieldTranslation().apply { this.value = value }.apply { this.language = language }.apply { this.field = field }

        // mock
        `when`(translatableEntityService.getByUuidAndLabel(com.nhaarman.mockito_kotlin.eq(ServicesProperties.templateUuid), com.nhaarman.mockito_kotlin.eq(ServicesProperties.templateLabel))).thenReturn(templateEntity)
        `when`(translatableEntityService.getByUuidAndLabel(com.nhaarman.mockito_kotlin.eq(uuid), com.nhaarman.mockito_kotlin.eq(label))).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByKeyAndTypeAndEntity(key, TranslatableEntityFieldType.STATIC, entity)).thenReturn(null)
        `when`(translatableEntityFieldRepository.save(ArgumentMatchers.any(TranslatableEntityField::class.java))).thenReturn(field)
        `when`(translatableEntityFieldTranslationService.create(com.nhaarman.mockito_kotlin.any())).thenReturn(translation)
        // sut
        translatableEntityFieldService.copyStatics(uuid, label)
        // verify
        verify(translatableEntityService, times(1)).getByUuidAndLabel(com.nhaarman.mockito_kotlin.eq(ServicesProperties.templateUuid), com.nhaarman.mockito_kotlin.eq(ServicesProperties.templateLabel))
        verify(translatableEntityService, times(2)).getByUuidAndLabel(com.nhaarman.mockito_kotlin.eq(uuid), com.nhaarman.mockito_kotlin.eq(label))
        verify(translatableEntityFieldRepository, times(1)).findByKeyAndTypeAndEntity(key, TranslatableEntityFieldType.STATIC, entity)
        verify(translatableEntityFieldRepository, times(1)).save(ArgumentMatchers.any(TranslatableEntityField::class.java))
        verify(translatableEntityFieldTranslationService, times(1)).create(com.nhaarman.mockito_kotlin.any())
    }

    //endregion
}