package com.sfl.qup.tms.service.translatable.impl

import com.sfl.qup.tms.domain.translatable.TranslatableEntity
import com.sfl.qup.tms.persistence.translatable.TranslatableEntityRepository
import com.sfl.qup.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.qup.tms.service.translatable.entity.dto.TranslatableEntityDto
import com.sfl.qup.tms.service.translatable.entity.impl.TranslatableEntityServiceImpl
import com.sfl.qup.tms.service.translatable.entity.exception.TranslatableEntityExistsByUuidException
import com.sfl.qup.tms.service.translatable.entity.exception.TranslatableEntityNotFoundByUuidException
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

    @Test(expected = TranslatableEntityExistsByUuidException::class)
    fun createTranslatableEntityWhenEntityAlreadyExistsTest() {
        // test data
        val uuid = "uuid"
        val dto = TranslatableEntityDto(uuid, "name")
        val entity = TranslatableEntity()
        // mock
        `when`(translatableEntityRepository.findByUuid(uuid)).thenReturn(entity)
        // sut
        translatableEntityService.create(dto)
        // verify
        verify(translatableEntityRepository, times(1)).findByUuid(uuid)
    }

    @Test
    fun createTranslatableEntityWhenEntityNotFoundTest() {
        // test data
        val uuid = "uuid"
        val name = "name"
        val dto = TranslatableEntityDto(uuid, name)
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.name = name }
        // mock
        `when`(translatableEntityRepository.findByUuid(uuid)).thenReturn(null)
        `when`(translatableEntityRepository.save(ArgumentMatchers.any(TranslatableEntity::class.java))).thenReturn(entity)
        // sut
        val result = translatableEntityService.create(dto)
        // verify
        verify(translatableEntityRepository, times(1)).findByUuid(uuid)
        verify(translatableEntityRepository, times(1)).save(ArgumentMatchers.any(TranslatableEntity::class.java))
        Assert.assertEquals(uuid, result.uuid)
        Assert.assertEquals(name, result.name)
    }

    @Test
    fun findByUuidTest() {
        // test data
        val uuid = "uuid"
        val entity = TranslatableEntity().apply { this.uuid = uuid }
        // mock
        `when`(translatableEntityRepository.findByUuid(uuid)).thenReturn(entity)
        // sut
        val result = translatableEntityService.findByUuid(uuid)
        // verify
        verify(translatableEntityRepository, times(1)).findByUuid(uuid)
        Assert.assertNotNull(result)
        Assert.assertEquals(uuid, result!!.uuid)
    }

    @Test(expected = TranslatableEntityNotFoundByUuidException::class)
    fun getByUuidWhenTranslatableEntityNotFoundTest() {
        // test data
        val uuid = "uuid"
        // mock
        `when`(translatableEntityRepository.findByUuid(uuid)).thenReturn(null)
        // sut
        translatableEntityService.getByUuid(uuid)
        // verify
        verify(translatableEntityRepository, times(1)).findByUuid(uuid)
    }

    @Test
    fun getByUuidWhenTranslatableEntityFoundTest() {
        // test data
        val uuid = "uuid"
        val entity = TranslatableEntity()
                .apply { this.uuid = uuid }
        // mock
        `when`(translatableEntityRepository.findByUuid(uuid)).thenReturn(entity)
        // sut
        val result = translatableEntityService.getByUuid(uuid)
        // verify
        verify(translatableEntityRepository, times(1)).findByUuid(uuid)
        Assert.assertEquals(uuid, result.uuid)
    }
}