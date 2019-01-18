package com.sfl.tms.service.translatable.translation.impl

import com.sfl.tms.domain.language.Language
import com.sfl.tms.domain.translatable.TranslatableEntity
import com.sfl.tms.domain.translatable.TranslatableEntityField
import com.sfl.tms.domain.translatable.TranslatableEntityFieldTranslation
import com.sfl.tms.domain.translatable.TranslatableEntityFieldType
import com.sfl.tms.persistence.translatable.TranslatableEntityFieldTranslationRepository
import com.sfl.tms.service.language.LanguageService
import com.sfl.tms.service.language.exception.LanguageNotFoundByLangException
import com.sfl.tms.service.translatable.field.TranslatableEntityFieldService
import com.sfl.tms.service.translatable.field.exception.TranslatableEntityFieldNotFoundException
import com.sfl.tms.service.translatable.translation.TranslatableEntityFieldTranslationService
import com.sfl.tms.service.translatable.translation.dto.TranslatableEntityFieldTranslationDto
import com.sfl.tms.service.translatable.translation.exception.TranslatableFieldTranslationExistException
import com.sfl.tms.service.translatable.translation.exception.TranslatableFieldTranslationNotFoundException
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

/**
 * User: Vazgen Danielyan
 * Date: 1/16/19
 * Time: 12:26 PM
 */
@RunWith(MockitoJUnitRunner::class)
class TranslatableEntityFieldTranslationServiceImplTest {

    //region Injection

    @Mock
    private lateinit var translatableEntityFieldTranslationRepository: TranslatableEntityFieldTranslationRepository

    @Mock
    private lateinit var translatableEntityFieldService: TranslatableEntityFieldService

    @Mock
    private lateinit var languageService: LanguageService

    @InjectMocks
    private var translatableEntityFieldTranslationService: TranslatableEntityFieldTranslationService = TranslatableEntityFieldTranslationServiceImpl()


    //endregion

    //region findByFieldAndLanguage

    @Test(expected = TranslatableEntityFieldNotFoundException::class)
    fun findByKeyAndEntityUuidAndEntityLabelAndLanguageLangWhenFieldNotFoundTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val lang = "en"
        val type = TranslatableEntityFieldType.STATIC
        // mock
        `when`(translatableEntityFieldService.getByKeyAndEntity(key, type, uuid, label)).thenThrow(TranslatableEntityFieldNotFoundException::class.java)
        // sut
        translatableEntityFieldTranslationService.findByFieldAndLanguage(key, type, uuid, label, lang)
        // verify
        verify(translatableEntityFieldService, times(1)).getByKeyAndEntity(key, type, uuid, label)
    }

    @Test(expected = LanguageNotFoundByLangException::class)
    fun findByKeyAndEntityUuidAndEntityLabelAndLanguageLangWhenLanguageNotFoundTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val lang = "en"
        val type = TranslatableEntityFieldType.STATIC
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.key = key }.apply { this.entity = entity }
        // mock
        `when`(translatableEntityFieldService.getByKeyAndEntity(key, type, uuid, label)).thenReturn(field)
        `when`(languageService.getByLang(lang)).thenThrow(LanguageNotFoundByLangException::class.java)
        // sut
        translatableEntityFieldTranslationService.findByFieldAndLanguage(key, type, uuid, label, lang)
        // verify
        verify(translatableEntityFieldService, times(1)).getByKeyAndEntity(key, type, uuid, label)
        verify(languageService, times(1)).getByLang(lang)
    }

    @Test
    fun findByKeyAndEntityUuidAndEntityLabelAndLanguageLangTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val lang = "en"
        val value = "value"
        val type = TranslatableEntityFieldType.STATIC
        val language = Language().apply { this.lang = lang }
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.key = key }.apply { this.entity = entity }
        val translation = TranslatableEntityFieldTranslation().apply { this.value = value }.apply { this.field = field }.apply { this.language = language }
        // mock
        `when`(translatableEntityFieldService.getByKeyAndEntity(key, type, uuid, label)).thenReturn(field)
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityFieldTranslationRepository.findByFieldAndLanguage(field, language)).thenReturn(translation)
        // sut
        val result = translatableEntityFieldTranslationService.findByFieldAndLanguage(key, type, uuid, label, lang)
        // verify
        verify(translatableEntityFieldService, times(1)).getByKeyAndEntity(key, type, uuid, label)
        verify(languageService, times(1)).getByLang(lang)
        verify(translatableEntityFieldTranslationRepository, times(1)).findByFieldAndLanguage(field, language)

        Assert.assertNotNull(result)
        Assert.assertEquals(value, result!!.value)
        Assert.assertEquals(key, result.field.key)
    }

    //endregion

    //region getByFieldAndLanguage

    @Test(expected = TranslatableFieldTranslationNotFoundException::class)
    fun getByKeyAndEntityUuidAndEntityLabelAndLanguageLangWhenFieldNotFoundTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val lang = "en"
        val type = TranslatableEntityFieldType.STATIC
        val language = Language().apply { this.lang = lang }
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.key = key }.apply { this.entity = entity }
        // mock
        `when`(translatableEntityFieldService.getByKeyAndEntity(key, type, uuid, label)).thenReturn(field)
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityFieldTranslationRepository.findByFieldAndLanguage(field, language)).thenReturn(null)
        // sut
        translatableEntityFieldTranslationService.getByFieldAndLanguage(key, type, uuid, label, lang)
        // verify
        verify(translatableEntityFieldService, times(1)).getByKeyAndEntity(key, type, uuid, label)
        verify(languageService, times(1)).getByLang(lang)
        verify(translatableEntityFieldTranslationRepository, times(1)).findByFieldAndLanguage(field, language)
    }

    @Test
    fun getByKeyAndEntityUuidAndEntityLabelAndLanguageLangTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val lang = "en"
        val value = "value"
        val type = TranslatableEntityFieldType.STATIC
        val language = Language().apply { this.lang = lang }
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.key = key }.apply { this.entity = entity }
        val translation = TranslatableEntityFieldTranslation().apply { this.value = value }.apply { this.field = field }.apply { this.language = language }
        // mock
        `when`(translatableEntityFieldService.getByKeyAndEntity(key, type, uuid, label)).thenReturn(field)
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityFieldTranslationRepository.findByFieldAndLanguage(field, language)).thenReturn(translation)
        // sut
        val result = translatableEntityFieldTranslationService.getByFieldAndLanguage(key, type, uuid, label, lang)
        // verify
        verify(translatableEntityFieldService, times(1)).getByKeyAndEntity(key, type, uuid, label)
        verify(languageService, times(1)).getByLang(lang)
        verify(translatableEntityFieldTranslationRepository, times(1)).findByFieldAndLanguage(field, language)

        Assert.assertNotNull(result)
        Assert.assertEquals(value, result.value)
        Assert.assertEquals(key, result.field.key)
    }

    //endregion

    //region getByKeyAndEntity

    @Test
    fun getByKeyAndEntityUuidAndEntityLabelTest() {
        // test data
        val key = "key"
        val uuid = "uuid"
        val label = "label"
        val lang = "en"
        val value = "value"
        val type = TranslatableEntityFieldType.STATIC
        val language = Language().apply { this.lang = lang }
        val entity = TranslatableEntity().apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.key = key }.apply { this.entity = entity }
        val translation = TranslatableEntityFieldTranslation().apply { this.value = value }.apply { this.field = field }.apply { this.language = language }
        val list = listOf(translation)
        // mock
        `when`(translatableEntityFieldService.getByKeyAndEntity(key, type, uuid, label)).thenReturn(field)
        `when`(translatableEntityFieldTranslationRepository.findByField(field)).thenReturn(list)
        // sut
        val result = translatableEntityFieldTranslationService.getByKeyAndEntity(key, type, uuid, label)
        // verify
        verify(translatableEntityFieldService, times(1)).getByKeyAndEntity(key, type, uuid, label)
        verify(translatableEntityFieldTranslationRepository, times(1)).findByField(field)

        Assert.assertTrue(result.isNotEmpty())
        result.forEach {
            Assert.assertEquals(value, it.value)
            Assert.assertEquals(key, it.field.key)
        }
    }

    //endregion

    //region create

    @Test(expected = LanguageNotFoundByLangException::class)
    fun createWhenLanguageNotFoundTest() {
        // test data
        val key = "key"
        val value = "value"
        val uuid = "uuid"
        val label = "label"
        val lang = "en"
        val type = TranslatableEntityFieldType.STATIC
        val dto = TranslatableEntityFieldTranslationDto(key, type, value, uuid, label, lang)
        // mock
        `when`(languageService.getByLang(lang)).thenThrow(LanguageNotFoundByLangException::class.java)
        // sut
        translatableEntityFieldTranslationService.create(dto)
        // verify
        verify(languageService, times(1)).getByLang(lang)
    }

    @Test(expected = TranslatableFieldTranslationExistException::class)
    fun createWhenEntityFieldTranslationExistTest() {
        // test data
        val key = "key"
        val value = "value"
        val uuid = "uuid"
        val label = "label"
        val lang = "en"
        val type = TranslatableEntityFieldType.STATIC
        val dto = TranslatableEntityFieldTranslationDto(key, type, value, uuid, label, lang)
        val language = Language().apply { this.id = 1L }.apply { this.lang = lang }
        val entity = TranslatableEntity().apply { this.id = 1L }.apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.id = 1L }.apply { this.key = key }.apply { this.entity = entity }
        val translation = TranslatableEntityFieldTranslation().apply { this.id = 1L }.apply { this.value = value }.apply { this.field = field }.apply { this.language = language }
        // mock
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityFieldService.findByKeyAndEntity(key, type, uuid, label)).thenReturn(null)
        `when`(translatableEntityFieldService.create(com.nhaarman.mockito_kotlin.any())).thenReturn(field)
        //retrieve translation
        `when`(translatableEntityFieldService.getByKeyAndEntity(key, type, uuid, label)).thenReturn(field)
        `when`(translatableEntityFieldTranslationRepository.findByFieldAndLanguage(field, language)).thenReturn(translation)
        // sut
        translatableEntityFieldTranslationService.create(dto)
        // verify
        verify(languageService, times(2)).getByLang(lang)
        verify(translatableEntityFieldService, times(1)).findByKeyAndEntity(key, type, uuid, label)
        verify(translatableEntityFieldService, times(1)).create(com.nhaarman.mockito_kotlin.any())

        verify(translatableEntityFieldService, times(1)).getByKeyAndEntity(key, type, uuid, label)
        verify(translatableEntityFieldTranslationRepository, times(1)).findByFieldAndLanguage(field, language)
    }

    @Test(expected = TranslatableFieldTranslationExistException::class)
    fun createWhenEntityFieldTranslationExistTest1() {
        // test data
        val key = "key"
        val value = "value"
        val uuid = "uuid"
        val label = "label"
        val lang = "en"
        val type = TranslatableEntityFieldType.STATIC
        val dto = TranslatableEntityFieldTranslationDto(key, type, value, uuid, label, lang)
        val language = Language().apply { this.id = 1L }.apply { this.lang = lang }
        val entity = TranslatableEntity().apply { this.id = 1L }.apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.id = 1L }.apply { this.key = key }.apply { this.entity = entity }
        val translation = TranslatableEntityFieldTranslation().apply { this.id = 1L }.apply { this.value = value }.apply { this.field = field }.apply { this.language = language }
        // mock
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityFieldService.findByKeyAndEntity(key, type, uuid, label)).thenReturn(field)
        //retrieve translation
        `when`(translatableEntityFieldService.getByKeyAndEntity(key, type, uuid, label)).thenReturn(field)
        `when`(translatableEntityFieldTranslationRepository.findByFieldAndLanguage(field, language)).thenReturn(translation)
        // sut
        translatableEntityFieldTranslationService.create(dto)
        // verify
        verify(languageService, times(2)).getByLang(lang)
        verify(translatableEntityFieldService, times(1)).findByKeyAndEntity(key, type, uuid, label)

        verify(translatableEntityFieldService, times(1)).getByKeyAndEntity(key, type, uuid, label)
        verify(translatableEntityFieldTranslationRepository, times(1)).findByFieldAndLanguage(field, language)
    }

    @Test
    fun createTest() {
        // test data
        val key = "key"
        val value = "value"
        val uuid = "uuid"
        val label = "label"
        val lang = "en"
        val type = TranslatableEntityFieldType.STATIC
        val dto = TranslatableEntityFieldTranslationDto(key, type, value, uuid, label, lang)
        val language = Language().apply { this.id = 1L }.apply { this.lang = lang }
        val entity = TranslatableEntity().apply { this.id = 1L }.apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.id = 1L }.apply { this.key = key }.apply { this.entity = entity }
        val translation = TranslatableEntityFieldTranslation().apply { this.id = 1L }.apply { this.value = value }.apply { this.field = field }.apply { this.language = language }
        // mock
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityFieldService.findByKeyAndEntity(key, type, uuid, label)).thenReturn(null)
        `when`(translatableEntityFieldService.create(com.nhaarman.mockito_kotlin.any())).thenReturn(field)
        //retrieve translation
        `when`(translatableEntityFieldService.getByKeyAndEntity(key, type, uuid, label)).thenReturn(field)
        `when`(translatableEntityFieldTranslationRepository.findByFieldAndLanguage(field, language)).thenReturn(null)
        `when`(translatableEntityFieldTranslationRepository.save(any(TranslatableEntityFieldTranslation::class.java))).thenReturn(translation)
        // sut
        val result = translatableEntityFieldTranslationService.create(dto)
        // verify
        verify(languageService, times(2)).getByLang(lang)
        verify(translatableEntityFieldService, times(1)).findByKeyAndEntity(key, type, uuid, label)
        verify(translatableEntityFieldService, times(1)).create(com.nhaarman.mockito_kotlin.any())

        verify(translatableEntityFieldService, times(1)).getByKeyAndEntity(key, type, uuid, label)
        verify(translatableEntityFieldTranslationRepository, times(1)).findByFieldAndLanguage(field, language)
        verify(translatableEntityFieldTranslationRepository, times(1)).save(any(TranslatableEntityFieldTranslation::class.java))

        Assert.assertEquals(value, result.value)
        Assert.assertEquals(lang, result.language.lang)
        Assert.assertEquals(key, result.field.key)
        Assert.assertEquals(uuid, result.field.entity.uuid)
        Assert.assertEquals(label, result.field.entity.label)
    }

    //endregion

    //region updateValue

    @Test
    fun updateValueTest() {
        // test data
        val key = "key"
        val oldValue = "old value"
        val newValue = "new value"
        val uuid = "uuid"
        val label = "label"
        val lang = "en"
        val type = TranslatableEntityFieldType.STATIC
        val dto = TranslatableEntityFieldTranslationDto(key, type, newValue, uuid, label, lang)
        val language = Language().apply { this.id = 1L }.apply { this.lang = lang }
        val entity = TranslatableEntity().apply { this.id = 1L }.apply { this.uuid = uuid }.apply { this.label = label }
        val field = TranslatableEntityField().apply { this.id = 1L }.apply { this.key = key }.apply { this.entity = entity }
        val translation = TranslatableEntityFieldTranslation().apply { this.id = 1L }.apply { this.value = oldValue }.apply { this.field = field }.apply { this.language = language }
        // mock
        `when`(languageService.getByLang(lang)).thenReturn(language)
        `when`(translatableEntityFieldService.getByKeyAndEntity(key, type, uuid, label)).thenReturn(field)
        `when`(translatableEntityFieldTranslationRepository.findByFieldAndLanguage(field, language)).thenReturn(translation)
        `when`(translatableEntityFieldTranslationRepository.save(any(TranslatableEntityFieldTranslation::class.java))).thenReturn(translation.apply { this.value = newValue })
        // sut
        val result = translatableEntityFieldTranslationService.updateValue(dto)
        // verify
        verify(languageService, times(1)).getByLang(lang)
        verify(translatableEntityFieldService, times(1)).getByKeyAndEntity(key, type, uuid, label)
        verify(translatableEntityFieldTranslationRepository, times(1)).findByFieldAndLanguage(field, language)
        verify(translatableEntityFieldTranslationRepository, times(1)).save(any(TranslatableEntityFieldTranslation::class.java))

        Assert.assertEquals(newValue, result.value)
        Assert.assertNotEquals(oldValue, result.value)
        Assert.assertEquals(lang, result.language.lang)
        Assert.assertEquals(key, result.field.key)
        Assert.assertEquals(uuid, result.field.entity.uuid)
        Assert.assertEquals(label, result.field.entity.label)
    }

    //endregion
}