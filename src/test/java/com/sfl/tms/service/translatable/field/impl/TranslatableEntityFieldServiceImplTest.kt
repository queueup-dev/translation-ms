package com.sfl.tms.service.translatable.field.impl

import com.sfl.tms.domain.translatable.TranslatableEntity
import com.sfl.tms.domain.translatable.TranslatableEntityField
import com.sfl.tms.persistence.translatable.TranslatableEntityFieldRepository
import com.sfl.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.service.translatable.entity.exception.TranslatableEntityNotFoundByUuidException
import com.sfl.tms.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.tms.service.translatable.field.exception.TranslatableEntityFieldExistsForTranslatableEntityException
import com.sfl.tms.service.translatable.field.exception.TranslatableEntityFieldNotFoundByNameAndEntityUuidException
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

    @InjectMocks
    private var translatableEntityFieldService: TranslatableEntityFieldService = TranslatableEntityFieldServiceImpl()

    //endregion

    @Test
    fun findTranslatableEntityFieldTest() {
        // test data
        val entityUuid = "entity_uuid"
        val fieldName = "field_name"
        val entity = TranslatableEntity().apply { this.uuid = entityUuid }
        val field = TranslatableEntityField().apply { this.name = fieldName }.apply { this.entity = entity }
        // mock
        `when`(translatableEntityFieldRepository.findByNameAndEntity_Uuid(fieldName, entityUuid)).thenReturn(field)
        // sut
        val result = translatableEntityFieldService.findByNameAndEntityUuid(fieldName, entityUuid);
        // verify
        verify(translatableEntityFieldRepository, times(1)).findByNameAndEntity_Uuid(fieldName, entityUuid)

        Assert.assertNotNull(result)
        Assert.assertEquals(fieldName, result!!.name)
        Assert.assertEquals(entityUuid, result.entity.uuid)
    }

    @Test(expected = TranslatableEntityFieldNotFoundByNameAndEntityUuidException::class)
    fun getByNameAndEntityUuidWhenTranslatableEntityFieldExistTest() {
        // test data
        val entityUuid = "entity_uuid"
        val fieldName = "field_name"
        // mock
        `when`(translatableEntityFieldRepository.findByNameAndEntity_Uuid(fieldName, entityUuid)).thenReturn(null)
        // sut
        translatableEntityFieldService.getByNameAndEntityUuid(fieldName, entityUuid);
        // verify
        verify(translatableEntityFieldRepository, times(1)).findByNameAndEntity_Uuid(fieldName, entityUuid)
    }

    @Test
    fun getByNameAndEntityUuidTest() {
        // test data
        val entityUuid = "entity_uuid"
        val fieldName = "field_name"
        val entity = TranslatableEntity().apply { this.uuid = entityUuid }
        val field = TranslatableEntityField().apply { this.name = fieldName }.apply { this.entity = entity }
        // mock
        `when`(translatableEntityFieldRepository.findByNameAndEntity_Uuid(fieldName, entityUuid)).thenReturn(field)
        // sut
        val result = translatableEntityFieldService.getByNameAndEntityUuid(fieldName, entityUuid);
        // verify
        verify(translatableEntityFieldRepository, times(1)).findByNameAndEntity_Uuid(fieldName, entityUuid)

        Assert.assertEquals(fieldName, result.name)
        Assert.assertEquals(entityUuid, result.entity.uuid)
    }

    @Test(expected = TranslatableEntityNotFoundByUuidException::class)
    fun createTranslatableEntityFieldWhenTranslatableEntityNotFoundTest() {
        // test data
        val uuid = "uuid"
        val name = "name"
        val dto = TranslatableEntityFieldDto(uuid, name)
        // mock
        `when`(translatableEntityService.getByUuid(uuid)).thenThrow(TranslatableEntityNotFoundByUuidException::class.java)
        // sut
        translatableEntityFieldService.create(dto)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(uuid)
    }

    @Test(expected = TranslatableEntityFieldExistsForTranslatableEntityException::class)
    fun createTranslatableEntityFieldWhenTranslatableEntityFieldExistTest() {
        // test data
        val uuid = "uuid"
        val name = "name"
        val dto = TranslatableEntityFieldDto(uuid, name)
        val entity = TranslatableEntity()
        val field = TranslatableEntityField()
        // mock
        `when`(translatableEntityService.getByUuid(uuid)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByNameAndEntity_Uuid(name, uuid)).thenReturn(field)
        // sut
        translatableEntityFieldService.create(dto)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(uuid)
        verify(translatableEntityFieldRepository, times(1)).findByNameAndEntity_Uuid(name, uuid)
    }

    @Test
    fun createTranslatableEntityFieldTest() {
        // test data
        val entityUuid = "entity_uuid"
        val entityName = "entity_name"
        val fieldName = "field_name"
        val dto = TranslatableEntityFieldDto(entityUuid, fieldName)
        val entity = TranslatableEntity().apply { this.uuid = entityUuid }.apply { this.name = entityName }
        val field = TranslatableEntityField().apply { this.entity = entity }.apply { this.name = fieldName }
        // mock
        `when`(translatableEntityService.getByUuid(entityUuid)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByNameAndEntity_Uuid(fieldName, entityUuid)).thenReturn(null)
        `when`(translatableEntityFieldRepository.save(ArgumentMatchers.any(TranslatableEntityField::class.java))).thenReturn(field)
        // sut
        val result = translatableEntityFieldService.create(dto)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(entityUuid)
        verify(translatableEntityFieldRepository, times(1)).findByNameAndEntity_Uuid(fieldName, entityUuid)
        verify(translatableEntityFieldRepository, times(1)).save(ArgumentMatchers.any(TranslatableEntityField::class.java))

        Assert.assertEquals(fieldName, result.name)
        Assert.assertEquals(entityUuid, result.entity.uuid)
        Assert.assertEquals(entityName, result.entity.name)
    }
}