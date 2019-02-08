package com.sfl.tms.service.translatable.entity.impl

import com.sfl.tms.core.domain.translatable.TranslatableEntity
import com.sfl.tms.core.persistence.translatable.TranslatableEntityRepository
import com.sfl.tms.core.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.core.service.translatable.entity.dto.TranslatableEntityDto
import com.sfl.tms.core.service.translatable.entity.exception.TranslatableEntityExistsException
import com.sfl.tms.core.service.translatable.entity.exception.TranslatableEntityNotFoundException
import com.sfl.tms.core.service.translatable.entity.impl.TranslatableEntityServiceImpl
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
class TranslatableEntityServiceImplTest {

    //region Injection

    @Mock
    private lateinit var translatableEntityRepository: TranslatableEntityRepository

    @InjectMocks
    private var translatableEntityService: TranslatableEntityService = TranslatableEntityServiceImpl()

    //endregion

    //region finaAll

    @Test
    fun findAllTest() {
        // test data
        val uuid = "uuid"
        val label = "label"
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }
        val list = listOf(entity)
        // mock
        `when`(translatableEntityRepository.findAll()).thenReturn(list)
        // sut
        val result = translatableEntityService.findAll()
        // verify
        verify(translatableEntityRepository, times(1)).findAll()
        Assert.assertNotNull(result)
        Assert.assertTrue(result.isNotEmpty())
        result.forEach {
            Assert.assertEquals(uuid, it.uuid)
            Assert.assertEquals(label, it.label)
        }
    }

    //endregion

    //region findByUuid

    @Test
    fun findByUuidAndLabelTest() {
        // test data
        val uuid = "uuid"
        val label = "label"
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }
        // mock
        `when`(translatableEntityRepository.findByUuidAndLabel(uuid, label)).thenReturn(entity)
        // sut
        val result = translatableEntityService.findByUuidAndLabel(uuid, label)
        // verify
        verify(translatableEntityRepository, times(1)).findByUuidAndLabel(uuid, label)
        Assert.assertNotNull(result)
        Assert.assertEquals(uuid, result!!.uuid)
        Assert.assertEquals(label, result.label)
    }

    //endregion

    //region getByUuidAndLabel

    @Test(expected = TranslatableEntityNotFoundException::class)
    fun getByUuidWhenTranslatableEntityNotFoundTest() {
        // test data
        val uuid = "uuid"
        val label = "label"
        // mock
        `when`(translatableEntityRepository.findByUuidAndLabel(uuid, label)).thenReturn(null)
        // sut
        translatableEntityService.getByUuidAndLabel(uuid, label)
        // verify
        verify(translatableEntityRepository, times(1)).findByUuidAndLabel(uuid, label)
    }

    @Test
    fun getByUuidWhenTranslatableEntityFoundTest() {
        // test data
        val uuid = "uuid"
        val label = "label"
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }
        // mock
        `when`(translatableEntityRepository.findByUuidAndLabel(uuid, label)).thenReturn(entity)
        // sut
        val result = translatableEntityService.getByUuidAndLabel(uuid, label)
        // verify
        verify(translatableEntityRepository, times(1)).findByUuidAndLabel(uuid, label)

        Assert.assertEquals(uuid, result.uuid)
        Assert.assertEquals(label, result.label)
    }

    //endregion

    //region create

    @Test(expected = TranslatableEntityExistsException::class)
    fun createTranslatableEntityWhenEntityAlreadyExistsTest() {
        // test data
        val uuid = "uuid"
        val label = "label"
        val dto = TranslatableEntityDto(uuid, label, "name")
        val entity = TranslatableEntity()
        // mock
        `when`(translatableEntityRepository.findByUuidAndLabel(uuid, label)).thenReturn(entity)
        // sut
        translatableEntityService.create(dto)
        // verify
        verify(translatableEntityRepository, times(1)).findByUuidAndLabel(uuid, label)
    }

    @Test
    fun createTranslatableEntityWhenEntityNotFoundTest() {
        // test data
        val uuid = "uuid"
        val label = "label"
        val name = "name"
        val dto = TranslatableEntityDto(uuid, label, name)
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }.apply { this.name = name }
        // mock
        `when`(translatableEntityRepository.findByUuidAndLabel(uuid, label)).thenReturn(null)
        `when`(translatableEntityRepository.save(ArgumentMatchers.any(TranslatableEntity::class.java))).thenReturn(entity)
        // sut
        val result = translatableEntityService.create(dto)
        // verify
        verify(translatableEntityRepository, times(1)).findByUuidAndLabel(uuid, label)
        verify(translatableEntityRepository, times(1)).save(ArgumentMatchers.any(TranslatableEntity::class.java))

        Assert.assertEquals(uuid, result.uuid)
        Assert.assertEquals(label, result.label)
        Assert.assertEquals(name, result.name)
    }

    //endregion

}