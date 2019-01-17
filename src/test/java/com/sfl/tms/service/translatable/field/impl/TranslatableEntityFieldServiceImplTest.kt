package com.sfl.tms.service.translatable.field.impl

import com.sfl.tms.domain.translatable.TranslatableEntity
import com.sfl.tms.domain.translatable.TranslatableEntityField
import com.sfl.tms.persistence.translatable.TranslatableEntityFieldRepository
import com.sfl.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.service.translatable.entity.exception.TranslatableEntityNotFoundByUuidAndLabelException
import com.sfl.tms.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.service.translatable.field.dto.TranslatableEntityFieldDto
import com.sfl.tms.service.translatable.field.exception.TranslatableEntityFieldExistsForTranslatableEntityException
import com.sfl.tms.service.translatable.field.exception.TranslatableEntityFieldNotFoundException
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

    //region findByKeyAndEntityUuidAndEntityLabelTest

    @Test
    fun findByKeyAndEntityTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val entity = TranslatableEntity()
        val field = TranslatableEntityField().apply { this.key = key }.apply { this.entity = entity }
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByKeyAndEntity(key, entity)).thenReturn(field)
        // sut
        val result = translatableEntityFieldService.findByKeyAndEntity(key, uuid, label)
        // verify
        verify(translatableEntityService, times(1)).getByUuidAndLabel(uuid, label)
        verify(translatableEntityFieldRepository, times(1)).findByKeyAndEntity(key, entity)

        Assert.assertNotNull(result)
        Assert.assertEquals(key, result!!.key)
    }

    //endregion

    //region getByKeyAndEntity

    @Test(expected = TranslatableEntityFieldNotFoundException::class)
    fun getByKeyAndEntityWhenTranslatableEntityFieldNotFoundTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val entity = TranslatableEntity()
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByKeyAndEntity(key, entity)).thenReturn(null)
        // sut
        translatableEntityFieldService.getByKeyAndEntity(key, uuid, label)
        // verify
        verify(translatableEntityService, times(1)).getByUuidAndLabel(uuid, label)
        verify(translatableEntityFieldRepository, times(1)).findByKeyAndEntity(key, entity)
    }

    @Test
    fun getByKeyAndEntityTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val entity = TranslatableEntity()
        val field = TranslatableEntityField().apply { this.key = key }
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByKeyAndEntity(key, entity)).thenReturn(field)
        // sut
        val result = translatableEntityFieldService.getByKeyAndEntity(key, uuid, label)
        // verify
        verify(translatableEntityService, times(1)).getByUuidAndLabel(uuid, label)
        verify(translatableEntityFieldRepository, times(1)).findByKeyAndEntity(key, entity)

        Assert.assertEquals(key, result.key)
    }

    //endregion

    //region create

    @Test(expected = TranslatableEntityNotFoundByUuidAndLabelException::class)
    fun createTranslatableEntityFieldWhenTranslatableEntityNotFoundTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val dto = TranslatableEntityFieldDto(key, uuid, label)
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenThrow(TranslatableEntityNotFoundByUuidAndLabelException::class.java)
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
        val dto = TranslatableEntityFieldDto(key, uuid, label)
        val entity = TranslatableEntity()
        val field = TranslatableEntityField().apply { this.entity = entity }
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByKeyAndEntity(key, entity)).thenReturn(field)
        // sut
        translatableEntityFieldService.create(dto)
        // verify
        verify(translatableEntityService, times(1)).getByUuidAndLabel(uuid, label)
        verify(translatableEntityFieldRepository, times(1)).findByKeyAndEntity(key, entity)
    }

    @Test
    fun createTranslatableEntityFieldTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val dto = TranslatableEntityFieldDto(key, uuid, label)
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.key = key }.apply { this.entity = entity }
        // mock
        `when`(translatableEntityService.getByUuidAndLabel(uuid, label)).thenReturn(entity)
        `when`(translatableEntityFieldRepository.findByKeyAndEntity(key, entity)).thenReturn(null)
        `when`(translatableEntityFieldRepository.save(ArgumentMatchers.any(TranslatableEntityField::class.java))).thenReturn(field)
        // sut
        val result = translatableEntityFieldService.create(dto)
        // verify
        verify(translatableEntityService, times(2)).getByUuidAndLabel(uuid, label)
        verify(translatableEntityFieldRepository, times(1)).findByKeyAndEntity(key, entity)
        verify(translatableEntityFieldRepository, times(1)).save(ArgumentMatchers.any(TranslatableEntityField::class.java))

        Assert.assertEquals(key, result.key)
        Assert.assertEquals(uuid, result.entity.uuid)
        Assert.assertEquals(label, result.entity.label)
    }

    //endregion
}