package com.sfl.qup.tms.service.translatable.impl

import com.sfl.qup.tms.domain.language.Language
import com.sfl.qup.tms.domain.translatable.TranslatableEntity
import com.sfl.qup.tms.domain.translatable.TranslatableEntityTranslation
import com.sfl.qup.tms.persistence.translatable.TranslatableEntityTranslationRepository
import com.sfl.qup.tms.service.language.LanguageService
import com.sfl.qup.tms.service.language.exception.LanguageNotFoundByLangException
import com.sfl.qup.tms.service.translatable.TranslatableEntityService
import com.sfl.qup.tms.service.translatable.TranslatableEntityTranslationService
import com.sfl.qup.tms.service.translatable.dto.entity.TranslatableEntityTranslationDto
import com.sfl.qup.tms.service.translatable.exception.TranslatableEntityNotFoundByUuidException
import com.sfl.qup.tms.service.translatable.exception.TranslatableEntityTranslationExistException
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
class TranslatableEntityTranslationServiceImplTest {

    //region Injection

    @Mock
    private lateinit var translatableEntityService: TranslatableEntityService

    @Mock
    private lateinit var languageService: LanguageService

    @Mock
    private lateinit var translatableEntityTranslationRepository: TranslatableEntityTranslationRepository

    @InjectMocks
    private var translatableEntityTranslationService: TranslatableEntityTranslationService = TranslatableEntityTranslationServiceImpl()

    //endregion

    @Test(expected = LanguageNotFoundByLangException::class)
    fun createTranslatableEntityTranslationWhenLanguageNotFoundTest() {
        // test data
        val uuid = "uuid"
        val lang = "en"
        val text = "text"
        val dto = TranslatableEntityTranslationDto(uuid, lang, text)
        // mock
        `when`(languageService.getByLang(lang)).thenThrow(LanguageNotFoundByLangException::class.java)
        // sut
        translatableEntityTranslationService.create(dto)
        // verify
        verify(languageService, times(1)).getByLang(lang)
    }

    @Test(expected = TranslatableEntityNotFoundByUuidException::class)
    fun createTranslatableEntityTranslationWhenTranslatableEntityNotFoundTest() {
        // test data
        val uuid = "uuid"
        val lang = "en"
        val text = "text"
        val dto = TranslatableEntityTranslationDto(uuid, lang, text)
        val language = Language().apply { this.lang = lang }
        // mock
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityService.getByUuid(uuid)).thenThrow(TranslatableEntityNotFoundByUuidException::class.java)
        // sut
        translatableEntityTranslationService.create(dto)
        // verify
        verify(languageService, times(1)).getByLang(lang)
        verify(translatableEntityService, times(1)).getByUuid(uuid)
    }

    @Test(expected = TranslatableEntityTranslationExistException::class)
    fun createTranslatableEntityTranslationWhenTranslationAlreadyExistTest() {
        // test data
        val uuid = "uuid"
        val lang = "en"
        val text = "text"
        val dto = TranslatableEntityTranslationDto(uuid, lang, text)
        val language = Language().apply { id = 1 }.apply { this.lang = lang }
        val entity = TranslatableEntity().apply { id = 1 }.apply { this.uuid = uuid }
        // mock
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityService.getByUuid(uuid)).thenReturn(entity)
        `when`(translatableEntityTranslationRepository.findByLanguage_IdAndEntity_Id(language.id, entity.id)).thenReturn(TranslatableEntityTranslation())
        // sut
        translatableEntityTranslationService.create(dto)
        // verify
        verify(languageService, times(1)).getByLang(lang)
        verify(translatableEntityService, times(1)).getByUuid(uuid)
        verify(translatableEntityTranslationRepository, times(1)).findByLanguage_IdAndEntity_Id(language.id, entity.id)
    }

    @Test
    fun createTranslatableEntityTranslationTest() {
        // test data
        val uuid = "uuid"
        val lang = "en"
        val text = "text"
        val dto = TranslatableEntityTranslationDto(uuid, lang, text)
        val language = Language().apply { id = 1 }.apply { this.lang = lang }
        val entity = TranslatableEntity().apply { id = 1 }.apply { this.uuid = uuid }
        val translation = TranslatableEntityTranslation().apply { this.text = text }.apply { this.language = language }.apply { this.entity = entity }
        // mock
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityService.getByUuid(uuid)).thenReturn(entity)
        `when`(translatableEntityTranslationRepository.findByLanguage_IdAndEntity_Id(language.id, entity.id)).thenReturn(null)
        `when`(translatableEntityTranslationRepository.save(ArgumentMatchers.any(TranslatableEntityTranslation::class.java))).thenReturn(translation)
        // sut
        val result = translatableEntityTranslationService.create(dto)
        // verify
        verify(languageService, times(1)).getByLang(lang)
        verify(translatableEntityService, times(1)).getByUuid(uuid)
        verify(translatableEntityTranslationRepository, times(1)).findByLanguage_IdAndEntity_Id(language.id, entity.id)
        verify(translatableEntityTranslationRepository, times(1)).save(ArgumentMatchers.any(TranslatableEntityTranslation::class.java))

        Assert.assertEquals(translation.text, result.text)
        Assert.assertEquals(translation.entity.uuid, result.entity.uuid)
        Assert.assertEquals(translation.language.lang, result.language.lang)
    }
}