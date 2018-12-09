package com.sfl.qup.tms.service.translatable.impl

import com.sfl.qup.tms.domain.translatable.TranslatableEntity
import com.sfl.qup.tms.domain.translatable.TranslatableEntityField
import com.sfl.qup.tms.persistence.translatable.TranslatableEntityFieldRepository
import com.sfl.qup.tms.service.translatable.TranslatableEntityFieldService
import com.sfl.qup.tms.service.translatable.TranslatableEntityService
import com.sfl.qup.tms.service.translatable.dto.field.TranslatableEntityFieldDto
import com.sfl.qup.tms.service.translatable.exception.TranslatableEntityFieldExistsForTranslatableEntityException
import com.sfl.qup.tms.service.translatable.exception.TranslatableEntityNotFoundByUuidException
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

    @Test(expected = TranslatableEntityNotFoundByUuidException::class)
    fun createTranslatableEntityFieldWhenTranslatableEntityDoesNotExistTest() {
        // test data
        val uuid = "uuid"
        val name = "name"
        val dto = TranslatableEntityFieldDto(uuid, name)
        // mock
        `when`(translatableEntityService.findByUuid(uuid)).thenReturn(null)
        // sut
        translatableEntityFieldService.create(dto)
        // verify
        verify(translatableEntityService, times(1)).findByUuid(uuid)
    }

    @Test(expected = TranslatableEntityFieldExistsForTranslatableEntityException::class)
    fun createTranslatableEntityFieldWhenFieldAlreadyExistForEntityTest() {
        // test data
        val uuid = "uuid"
        val name = "name"
        val dto = TranslatableEntityFieldDto(uuid, name)
        val entity = TranslatableEntity()
        // mock
        `when`(translatableEntityService.findByUuid(uuid)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByNameAndEntity_Uuid(name, uuid)).thenReturn(TranslatableEntityField())
        // sut
        translatableEntityFieldService.create(dto)
        // verify
        verify(translatableEntityService, times(1)).findByUuid(uuid)
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
        `when`(translatableEntityService.findByUuid(entityUuid)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByNameAndEntity_Uuid(fieldName, entityUuid)).thenReturn(null)
        `when`(translatableEntityFieldRepository.save(ArgumentMatchers.any(TranslatableEntityField::class.java))).thenReturn(field)
        // sut
        val result = translatableEntityFieldService.create(dto)
        // verify
        verify(translatableEntityService, times(1)).findByUuid(entityUuid)
        verify(translatableEntityFieldRepository, times(1)).findByNameAndEntity_Uuid(fieldName, entityUuid)
        verify(translatableEntityFieldRepository, times(1)).save(ArgumentMatchers.any(TranslatableEntityField::class.java))

        Assert.assertEquals(fieldName, result.name)
        Assert.assertEquals(entityUuid, result.entity.uuid)
        Assert.assertEquals(entityName, result.entity.name)
    }

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
        val result = translatableEntityFieldService.find(fieldName, entityUuid);
        // verify
        verify(translatableEntityFieldRepository, times(1)).findByNameAndEntity_Uuid(fieldName, entityUuid)

        Assert.assertNotNull(result)
        Assert.assertEquals(fieldName, result!!.name)
        Assert.assertEquals(entityUuid, result.entity.uuid)
    }
}