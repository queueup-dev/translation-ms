package com.sfl.tms.service.translatablestatic

import com.sfl.tms.TmsApplication
import com.sfl.tms.domain.language.Language
import com.sfl.tms.domain.translatable.TranslatableEntity
import com.sfl.tms.domain.translatablestastic.TranslatableStatic
import com.sfl.tms.persistence.translatablestastics.TranslatableStaticRepository
import com.sfl.tms.service.language.LanguageService
import com.sfl.tms.service.language.exception.LanguageNotFoundByLangException
import com.sfl.tms.service.translatable.entity.TranslatableEntityService
import com.sfl.tms.service.translatable.entity.exception.TranslatableEntityNotFoundByUuidException
import com.sfl.tms.service.translatablestatic.dto.TranslatableStaticDto
import com.sfl.tms.service.translatablestatic.exception.TranslatableStaticExistException
import com.sfl.tms.service.translatablestatic.exception.TranslatableStaticNotFoundByKeyAndEntityUuidAndLanguageLangException
import com.sfl.tms.service.translatablestatic.exception.TranslatableStaticNotFoundByKeyAndEntityUuidException
import com.sfl.tms.service.translatablestatic.impl.TranslatableStaticServiceImpl
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
    private lateinit var translatableEntityService: TranslatableEntityService

    @Mock
    private lateinit var languageService: LanguageService

    @Mock
    private lateinit var translatableStaticRepository: TranslatableStaticRepository

    @InjectMocks
    private var translatableStaticService: TranslatableStaticService = TranslatableStaticServiceImpl()

    //endregion

    //region Get by key, entity id and language lang

    @Test
    fun findByKeyAndLanguageIdTest() {
        // test data
        val key = "key"
        val entityUuid = "uuid"
        val lang = "en"
        // mock
        `when`(translatableStaticRepository.findByKeyAndEntity_UuidAndLanguage_Lang(key, entityUuid, lang)).thenReturn(TranslatableStatic())
        // sut
        translatableStaticService.findByKeyAndEntityUuidAndLanguageLang(key, entityUuid, lang)
        // verify
        verify(translatableStaticRepository, times(1)).findByKeyAndEntity_UuidAndLanguage_Lang(key, entityUuid, lang)
    }

    //endregion

    //region Get by key and entity uuid

    @Test(expected = TranslatableEntityNotFoundByUuidException::class)
    fun getByKeyAndEntityUuidWhenEntityNotFoundTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        // mock
        `when`(translatableEntityService.getByUuid(uuid)).thenThrow(TranslatableEntityNotFoundByUuidException::class.java)
        // sut
        translatableStaticService.getByKeyAndEntityUuid(key, uuid)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(uuid)
    }

    @Test(expected = TranslatableStaticNotFoundByKeyAndEntityUuidException::class)
    fun getByKeyAndEntityUuidWhenStaticNotFoundByKeyAndEntityTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val entityId = 1L
        val entity = TranslatableEntity().apply { this.id = entityId }.apply { this.uuid = uuid }
        // mock
        `when`(translatableEntityService.getByUuid(uuid)).thenReturn(entity)
        `when`(translatableStaticRepository.findByKeyAndEntity_Id(key, entityId)).thenReturn(listOf())
        // sut
        translatableStaticService.getByKeyAndEntityUuid(key, uuid)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(uuid)
        verify(translatableStaticRepository, times(1)).findByKeyAndEntity_Id(key, entityId)
    }

    @Test
    fun getByKeyAndEntityUuidTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val entityId = 1L
        val entity = TranslatableEntity().apply { this.id = entityId }.apply { this.uuid = uuid }
        val static = TranslatableStatic().apply { this.key = key }.apply { this.entity = entity }
        // mock
        `when`(translatableEntityService.getByUuid(uuid)).thenReturn(entity)
        `when`(translatableStaticRepository.findByKeyAndEntity_Id(key, entityId)).thenReturn(listOf(static))
        // sut
        val result = translatableStaticService.getByKeyAndEntityUuid(key, uuid)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(uuid)
        verify(translatableStaticRepository, times(1)).findByKeyAndEntity_Id(key, entityId)

        assertTrue(result.isNotEmpty())
    }

    //endregion

    //region Get by key, entity uuid and language lang

    @Test(expected = TranslatableEntityNotFoundByUuidException::class)
    fun getByKeyAndEntityUuidAndLanguageLangWhenEntityNotFoundTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val lang = "en"
        // mock
        `when`(translatableEntityService.getByUuid(uuid)).thenThrow(TranslatableEntityNotFoundByUuidException::class.java)
        // sut
        translatableStaticService.getByKeyAndEntityUuidAndLanguageLang(key, uuid, lang)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(uuid)
    }

    @Test(expected = TranslatableStaticNotFoundByKeyAndEntityUuidAndLanguageLangException::class)
    fun getByKeyAndEntityUuidAndLanguageLangWhenTranslatableStaticNotFoundTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val lang = "en"
        val entity = TranslatableEntity().apply { id = 1L }.apply { this.uuid = uuid }
        val language = Language().apply { id = 1L }.apply { this.lang = lang }
        // mock
        `when`(translatableEntityService.getByUuid(uuid)).thenReturn(entity)
        `when`(translatableStaticRepository.findByKeyAndEntity_IdAndLanguage_Lang(key, entity.id, language.lang)).thenReturn(null)
        // sut
        translatableStaticService.getByKeyAndEntityUuidAndLanguageLang(key, uuid, lang)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(uuid)
        verify(translatableStaticRepository, times(1)).findByKeyAndEntity_IdAndLanguage_Lang(key, entity.id, language.lang)
    }

    @Test
    fun getByKeyAndEntityUuidAndLanguageLangTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val lang = "en"
        val entity = TranslatableEntity().apply { id = 1L }.apply { this.uuid = uuid }
        val language = Language().apply { id = 1L }.apply { this.lang = lang }
        val translatableStatic = TranslatableStatic().apply { this.key = key }.apply { this.language = language }.apply { this.entity = entity }.apply { this.language = language }
        // mock
        `when`(translatableEntityService.getByUuid(uuid)).thenReturn(entity)
        `when`(translatableStaticRepository.findByKeyAndEntity_IdAndLanguage_Lang(key, entity.id, language.lang)).thenReturn(translatableStatic)
        // sut
        val result = translatableStaticService.getByKeyAndEntityUuidAndLanguageLang(key, uuid, lang)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(uuid)
        verify(translatableStaticRepository, times(1)).findByKeyAndEntity_IdAndLanguage_Lang(key, entity.id, language.lang)

        assertEquals(key, result.key)
        assertEquals(language.id, result.language.id)
    }

    //endregion

    //region Create translatable static

    @Test(expected = LanguageNotFoundByLangException::class)
    fun createWhenLanguageNotFoundTest() {
        // test data
        val key = "key"
        val entityUuid = "uuid"
        val value = "value"
        val lang = "en"
        val dto = TranslatableStaticDto(key, entityUuid, value, lang)
        // mock
        `when`(languageService.getByLang(lang)).thenThrow(LanguageNotFoundByLangException::class.java)
        // sut
        translatableStaticService.create(dto)
        // verify
        verify(languageService, times(1)).getByLang(lang)
    }

    @Test(expected = TranslatableEntityNotFoundByUuidException::class)
    fun createWhenEntityNotFoundTest() {
        // test data
        val key = "key"
        val entityUuid = "uuid"
        val value = "value"
        val lang = "en"
        val dto = TranslatableStaticDto(key, entityUuid, value, lang)
        val language = Language().apply { this.id = 1L }.apply { this.lang = lang }
        // mock
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityService.getByUuid(entityUuid)).thenThrow(TranslatableEntityNotFoundByUuidException::class.java)
        // sut
        translatableStaticService.create(dto)
        // verify
        verify(languageService, times(1)).getByLang(lang)
        verify(translatableEntityService, times(1)).getByUuid(entityUuid)
    }

    @Test(expected = TranslatableStaticExistException::class)
    fun createWhenTranslatableStaticAlreadyExistTest() {
        // test data
        val key = "key"
        val entityUuid = "uuid"
        val value = "value"
        val lang = "en"
        val dto = TranslatableStaticDto(key, entityUuid, value, lang)
        val language = Language().apply { this.id = 1L }.apply { this.lang = lang }
        val entity = TranslatableEntity().apply { id = 1L }
        val static = TranslatableStatic()
        // mock
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityService.getByUuid(entityUuid)).thenReturn(entity)
        `when`(translatableStaticRepository.findByKeyAndEntity_IdAndLanguage_Id(key, entityId = entity.id, languageId = language.id)).thenReturn(static)
        // sut
        translatableStaticService.create(dto)
        // verify
        verify(languageService, times(1)).getByLang(lang)
        verify(translatableEntityService, times(1)).getByUuid(entityUuid)
        verify(translatableStaticRepository, times(1)).findByKeyAndEntity_IdAndLanguage_Id(key, entityId = entity.id, languageId = language.id)
    }

    @Test
    fun createTest() {
        // test data
        val key = "key"
        val entityUuid = "uuid"
        val value = "value"
        val lang = "en"
        val languageId = 1L
        val language = Language().apply { id = languageId }.apply { this.lang = lang }
        val entity = TranslatableEntity().apply { id = 1L }
        val dto = TranslatableStaticDto(key, entityUuid, value, lang)
        val translatableStatic = TranslatableStatic().apply { this.key = key }.apply { this.value = value }.apply { this.language = language }
        // mock
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityService.getByUuid(entityUuid)).thenReturn(entity)
        `when`(translatableStaticRepository.findByKeyAndEntity_IdAndLanguage_Id(key, entityId = entity.id, languageId = language.id)).thenReturn(null)
        `when`(translatableStaticRepository.save(ArgumentMatchers.any(TranslatableStatic::class.java))).thenReturn(translatableStatic)
        // sut
        val result = translatableStaticService.create(dto)
        // verify
        verify(languageService, times(1)).getByLang(lang)
        verify(translatableEntityService, times(1)).getByUuid(entityUuid)
        verify(translatableStaticRepository, times(1)).findByKeyAndEntity_IdAndLanguage_Id(key, entityId = entity.id, languageId = language.id)
        verify(translatableStaticRepository, times(1)).save(ArgumentMatchers.any(TranslatableStatic::class.java))

        assertEquals(key, result.key)
        assertEquals(value, result.value)
        assertEquals(languageId, result.language.id)
    }

    //endregion

    //region Update translatable static

    @Test(expected = TranslatableEntityNotFoundByUuidException::class)
    fun updateWhenTranslatableEntityNotFoundTest() {
        // test data
        val key = "key"
        val entityUuid = "uuid"
        val value = "value"
        val lang = "en"
        val dto = TranslatableStaticDto(key, entityUuid, value, lang)
        // mock
        `when`(translatableEntityService.getByUuid(entityUuid)).thenThrow(TranslatableEntityNotFoundByUuidException::class.java)
        // sut
        translatableStaticService.updateValue(dto)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(entityUuid)
    }

    @Test(expected = TranslatableStaticNotFoundByKeyAndEntityUuidAndLanguageLangException::class)
    fun updateWhenTranslatableStaticNotFoundTest() {
        // test data
        val key = "key"
        val entityUuid = "uuid"
        val value = "value"
        val lang = "en"
        val entity = TranslatableEntity().apply { id = 1L }.apply { uuid = entityUuid }
        val dto = TranslatableStaticDto(key, entityUuid, value, lang)
        // mock
        `when`(translatableEntityService.getByUuid(entityUuid)).thenReturn(entity)
        `when`(translatableStaticRepository.findByKeyAndEntity_IdAndLanguage_Lang(key, entity.id, lang)).thenReturn(null)
        // sut
        translatableStaticService.updateValue(dto)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(entityUuid)
        verify(translatableStaticRepository, times(1)).findByKeyAndEntity_IdAndLanguage_Lang(key, entity.id, lang)
    }

    @Test
    fun updateTest() {
        // test data
        val key = "key"
        val newValue = "value"
        val entityUuid = "uuid"
        val oldValue = "old value"
        val lang = "en"
        val entity = TranslatableEntity().apply { id = 1L }.apply { uuid = entityUuid }
        val dto = TranslatableStaticDto(key, entityUuid, newValue, lang)
        val translatableStatic = TranslatableStatic().apply { this.key = key }.apply { this.value = oldValue }.apply { this.entity = entity }
        // mock
        `when`(translatableEntityService.getByUuid(entityUuid)).thenReturn(entity)
        `when`(translatableStaticRepository.findByKeyAndEntity_IdAndLanguage_Lang(key, entity.id, lang)).thenReturn(translatableStatic)
        `when`(translatableStaticRepository.save(ArgumentMatchers.any(TranslatableStatic::class.java))).thenReturn(translatableStatic.apply { value = newValue })
        // sut
        val result = translatableStaticService.updateValue(dto)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(entityUuid)
        verify(translatableStaticRepository, times(1)).findByKeyAndEntity_IdAndLanguage_Lang(key, entity.id, lang)
        verify(translatableStaticRepository, times(1)).save(ArgumentMatchers.any(TranslatableStatic::class.java))

        assertEquals(dto.key, result.key)
        assertEquals(dto.value, result.value)
    }

    //endregion

    //region Search

    @Test
    fun searchWithoutTermTest() {
        // test data
        val lang = "en"
        val pageable = PageRequest.of(0, 15)
        // mock
        `when`(translatableStaticRepository.findByLangOrderByKeyAsc(lang, pageable)).thenReturn(emptyList())
        // sut
        val result = translatableStaticService.search(null, lang, null)
        // verify
        verify(translatableStaticRepository, times(1)).findByLangOrderByKeyAsc(lang, pageable)

        assertTrue(result.isEmpty())
    }

    @Test
    fun searchWithTermTest() {
        // test data
        val lang = "en"
        val term = "term"
        val pageable = PageRequest.of(0, 15)
        // mock
        `when`(translatableStaticRepository.findByLangAndKeyLikeOrderByKeyAsc(lang, "$term%", pageable)).thenReturn(emptyList())
        // sut
        val result = translatableStaticService.search(term, lang, 0)
        // verify
        verify(translatableStaticRepository, times(1)).findByLangAndKeyLikeOrderByKeyAsc(lang, "$term%", pageable)

        assertTrue(result.isEmpty())
    }

    //endregion

    //region Copy

    @Test(expected = TranslatableEntityNotFoundByUuidException::class)
    fun copyTranslatableStaticForEntityWhenEntityNotFoundTest() {
        // test data
        val entityUuid = "uuid"
        // mock
        `when`(translatableEntityService.getByUuid(entityUuid)).thenThrow(TranslatableEntityNotFoundByUuidException::class.java)
        // sut
        translatableStaticService.copy(entityUuid)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(entityUuid)
    }

    @Test(expected = TranslatableEntityNotFoundByUuidException::class)
    fun copyTranslatableStaticForEntityWhenTemplateEntityNotFoundTest() {
        // test data
        val entityUuid = "uuid"
        val entity = TranslatableEntity().apply { uuid = entityUuid }
        // mock
        `when`(translatableEntityService.getByUuid(entityUuid)).thenReturn(entity)
        `when`(translatableEntityService.getByUuid(TmsApplication.templateUuid)).thenThrow(TranslatableEntityNotFoundByUuidException::class.java)
        // sut
        translatableStaticService.copy(entityUuid)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(entityUuid)
        verify(translatableEntityService, times(1)).getByUuid(TmsApplication.templateUuid)
    }

    @Test()
    fun copyTranslatableStaticForEntityTest() {
        // test data
        val key = "key"
        val value = "value"
        val entityUuid = "uuid"
        val entity = TranslatableEntity().apply { uuid = entityUuid }
        val language = Language().apply { lang = "en" }
        val templateEntity = TranslatableEntity().apply { uuid = TmsApplication.templateUuid }
        val static = TranslatableStatic()
                .apply { this.key = key }
                .apply { this.value = value }
                .apply { this.language = language }
                .apply { this.entity = templateEntity }

        templateEntity.apply { statics = setOf(static) }
        // mock
        `when`(translatableEntityService.getByUuid(entityUuid)).thenReturn(entity)
        `when`(translatableEntityService.getByUuid(TmsApplication.templateUuid)).thenReturn(templateEntity)
        `when`(translatableStaticRepository.save(ArgumentMatchers.any(TranslatableStatic::class.java))).thenReturn(static.apply { this.entity = entity })
        // sut
        val result = translatableStaticService.copy(entityUuid)
        // verify
        verify(translatableEntityService, times(1)).getByUuid(entityUuid)
        verify(translatableEntityService, times(1)).getByUuid(TmsApplication.templateUuid)
        verify(translatableStaticRepository, times(1)).save(ArgumentMatchers.any(TranslatableStatic::class.java))

        assertTrue(result.isNotEmpty())
        assertEquals(1, result.size)
        result.forEach {
            assertEquals(entityUuid, it.entity.uuid)
        }
    }


    //endregion
}