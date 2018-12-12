package com.sfl.qup.tms.service.translatablestatic

import com.sfl.qup.tms.domain.language.Language
import com.sfl.qup.tms.domain.translatablestastic.TranslatableStatic
import com.sfl.qup.tms.persistence.translatablestastics.TranslatableStaticRepository
import com.sfl.qup.tms.service.language.LanguageService
import com.sfl.qup.tms.service.language.exception.LanguageNotFoundByIdException
import com.sfl.qup.tms.service.translatablestatic.dto.TranslatableStaticDto
import com.sfl.qup.tms.service.translatablestatic.exception.TranslatableStaticExistException
import com.sfl.qup.tms.service.translatablestatic.exception.TranslatableStaticNotFoundByKeyAndLanguageIdException
import com.sfl.qup.tms.service.translatablestatic.exception.TranslatableStaticNotFoundByKeyException
import com.sfl.qup.tms.service.translatablestatic.impl.TranslatableStaticServiceImpl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.data.domain.PageRequest

/**
 * User: Vazgen Danielyan
 * Date: 12/12/18
 * Time: 4:04 PM
 */
@RunWith(MockitoJUnitRunner::class)
class TranslatableStaticServiceImplTest {

    //region Injection

    @Mock
    private lateinit var languageService: LanguageService

    @Mock
    private lateinit var translatableStaticRepository: TranslatableStaticRepository

    @InjectMocks
    private var translatableStaticService: TranslatableStaticService = TranslatableStaticServiceImpl()

    //endregion

    @Test
    fun findByKeyAndLanguageIdTest() {
        // test data
        val key = "key"
        val languageId = 1L
        // mock
        `when`(translatableStaticRepository.findByKeyAndLanguage_Id(key, languageId)).thenReturn(TranslatableStatic())
        // sut
        translatableStaticService.findByKeyAndLanguageId(key, languageId)
        // verify
        verify(translatableStaticRepository, times(1)).findByKeyAndLanguage_Id(key, languageId)
    }

    @Test(expected = TranslatableStaticNotFoundByKeyAndLanguageIdException::class)
    fun getByKeyAndLanguageIdWhenNotFoundTest() {
        // test data
        val key = "key"
        val languageId = 1L
        // mock
        `when`(translatableStaticRepository.findByKeyAndLanguage_Id(key, languageId)).thenReturn(null)
        // sut
        translatableStaticService.getByKeyAndLanguageId(key, languageId)
        // verify
        verify(translatableStaticRepository, times(1)).findByKeyAndLanguage_Id(key, languageId)
    }

    @Test
    fun getByKeyAndLanguageIdWhenFoundTest() {
        // test data
        val key = "key"
        val language = Language().apply { id = 1L }
        val translatableStatic = TranslatableStatic().apply { this.key = key }.apply { this.language = language }
        // mock
        `when`(translatableStaticRepository.findByKeyAndLanguage_Id(key, language.id)).thenReturn(translatableStatic)
        // sut
        val result = translatableStaticService.getByKeyAndLanguageId(key, language.id)
        // verify
        verify(translatableStaticRepository, times(1)).findByKeyAndLanguage_Id(key, language.id)

        assertEquals(key, result.key)
        assertEquals(language.id, result.language.id)
    }

    @Test(expected = TranslatableStaticNotFoundByKeyException::class)
    fun getByKeyWhenNotFoundBykeyTest() {
        // test data
        val key = "key"
        // mock
        `when`(translatableStaticRepository.findByKey(key)).thenReturn(emptyList())
        // sut
        translatableStaticService.getByKey(key)
        // verify
        verify(translatableStaticRepository, times(1)).findByKey(key)
    }

    @Test
    fun getByKeyWhenFoundByKeyTest() {
        // test data
        val key = "key"
        // mock
        val listOf = listOf(TranslatableStatic().apply { this.key = key })
        `when`(translatableStaticRepository.findByKey(key)).thenReturn(listOf)
        // sut
        val result = translatableStaticService.getByKey(key)
        // verify
        verify(translatableStaticRepository, times(1)).findByKey(key)

        assertEquals(listOf.size, result.size)
        listOf.forEach {
            assertTrue(result.contains(it))
        }
    }

    @Test(expected = LanguageNotFoundByIdException::class)
    fun createWhenLanguageNotFoundTest() {
        // test data
        val key = "key"
        val value = "value"
        val languageId = 1L
        val dto = TranslatableStaticDto(key, value, languageId)
        // mock
        `when`(languageService.get(ArgumentMatchers.eq(languageId))).thenThrow(LanguageNotFoundByIdException::class.java)
        // sut
        translatableStaticService.create(dto)
        // verify
        verify(languageService, times(1)).get(languageId)
    }

    @Test(expected = TranslatableStaticExistException::class)
    fun createWhenTranslatableStaticAlreadyExistTest() {
        // test data
        val key = "key"
        val value = "value"
        val languageId = 1L
        val language = Language().apply { id = languageId }
        val dto = TranslatableStaticDto(key, value, languageId)
        // mock
        `when`(languageService.get(languageId)).thenReturn(language)
        `when`(translatableStaticRepository.findByKeyAndLanguage_Id(key, languageId)).thenReturn(TranslatableStatic())
        // sut
        translatableStaticService.create(dto)
        // verify
        verify(languageService, times(1)).get(languageId)
        verify(translatableStaticRepository, times(1)).findByKeyAndLanguage_Id(key, languageId)
    }

    @Test
    fun createTest() {
        // test data
        val key = "key"
        val value = "value"
        val languageId = 1L
        val language = Language().apply { id = languageId }
        val dto = TranslatableStaticDto(key, value, languageId)
        val translatableStatic = TranslatableStatic().apply { this.key = key }.apply { this.value = value }.apply { this.language = language }
        // mock
        `when`(languageService.get(languageId)).thenReturn(language)
        `when`(translatableStaticRepository.findByKeyAndLanguage_Id(key, languageId)).thenReturn(null)
        `when`(translatableStaticRepository.save(ArgumentMatchers.any(TranslatableStatic::class.java))).thenReturn(translatableStatic)
        // sut
        val result = translatableStaticService.create(dto)
        // verify
        verify(languageService, times(1)).get(languageId)
        verify(translatableStaticRepository, times(1)).findByKeyAndLanguage_Id(key, languageId)
        verify(translatableStaticRepository, times(1)).save(ArgumentMatchers.any(TranslatableStatic::class.java))

        assertEquals(key, result.key)
        assertEquals(value, result.value)
        assertEquals(languageId, result.language.id)
    }

    @Test(expected = TranslatableStaticNotFoundByKeyAndLanguageIdException::class)
    fun updateWhenTranslatableStaticAlreadyExistTest() {
        // test data
        val key = "key"
        val value = "value"
        val languageId = 1L
        val dto = TranslatableStaticDto(key, value, languageId)
        // mock
        `when`(translatableStaticRepository.findByKeyAndLanguage_Id(key, languageId)).thenReturn(null)
        // sut
        translatableStaticService.update(dto)
        // verify
        verify(translatableStaticRepository, times(1)).findByKeyAndLanguage_Id(key, languageId)
    }

    @Test
    fun updateTest() {
        // test data
        val key = "key"
        val value = "value"
        val oldValue = "old value"
        val languageId = 1L
        val dto = TranslatableStaticDto(key, value, languageId)
        val translatableStatic = TranslatableStatic().apply { this.key = key }.apply { this.value = oldValue }
        // mock
        `when`(translatableStaticRepository.findByKeyAndLanguage_Id(key, languageId)).thenReturn(translatableStatic)
        `when`(translatableStaticRepository.save(translatableStatic)).thenReturn(translatableStatic)
        // sut
        val result = translatableStaticService.update(dto)
        // verify
        verify(translatableStaticRepository, times(1)).findByKeyAndLanguage_Id(key, languageId)
        verify(translatableStaticRepository, times(1)).save(translatableStatic)

        assertEquals(dto.key, result.key)
        assertEquals(dto.value, result.value)
    }

    @Test
    fun searchWithoutTermTest() {
        // test data
        val pageable = PageRequest.of(0, 15)
        // mock
        `when`(translatableStaticRepository.findByOrderByKeyAsc(pageable)).thenReturn(emptyList())
        // sut
        val result = translatableStaticService.search(null, null)
        // verify
        verify(translatableStaticRepository, times(1)).findByOrderByKeyAsc(pageable)

        assertTrue(result.isEmpty())
    }

    @Test
    fun searchWithTermTest() {
        // test data
        val term = "term"
        val pageable = PageRequest.of(0, 15)
        // mock
        `when`(translatableStaticRepository.findByKeyLikeOrderByKeyAsc("$term%", pageable)).thenReturn(emptyList())
        // sut
        val result = translatableStaticService.search(term, 0)
        // verify
        verify(translatableStaticRepository, times(1)).findByKeyLikeOrderByKeyAsc("$term%", pageable)

        assertTrue(result.isEmpty())
    }
}