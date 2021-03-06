package com.sfl.tms.service.language.impl

import com.sfl.tms.core.domain.language.Language
import com.sfl.tms.core.persistence.language.LanguageRepository
import com.sfl.tms.core.service.language.LanguageService
import com.sfl.tms.core.service.language.exception.LanguageExistByLangException
import com.sfl.tms.core.service.language.exception.LanguageNotFoundByLangException
import com.sfl.tms.core.service.language.impl.LanguageServiceImpl
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
 * Date: 12/4/18
 * Time: 6:32 PM
 */
@RunWith(MockitoJUnitRunner::class)
class LanguageServiceImplTest {

    //region Injection

    @Mock
    private lateinit var languageRepository: LanguageRepository

    @InjectMocks
    private var languageService: LanguageService = LanguageServiceImpl()

    //endregion

    @Test
    fun findByLangTest() {
        // test data
        val lang = "NL"
        val language = Language().apply {
            this.lang = lang
        }
        // mock
        `when`(languageRepository.findByLang(lang)).thenReturn(language)
        // sut
        val result = languageService.findByLang(lang)
        // verify
        verify(languageRepository, times(1)).findByLang(lang)
        Assert.assertNotNull(lang, result)
        Assert.assertEquals(lang, result!!.lang)
    }

    @Test(expected = LanguageNotFoundByLangException::class)
    fun getByLangWhenLanguageNotFoundTest() {
        // test data
        val lang = "NL"
        // mock
        `when`(languageRepository.findByLang(lang)).thenReturn(null)
        // sut
        languageService.getByLang(lang)
        // verify
        verify(languageRepository, times(1)).findByLang(ArgumentMatchers.eq(lang))
    }

    @Test
    fun getByLangWhenLanguageFoundTest() {
        // test data
        val lang = "NL"
        val language = Language().apply {
            this.lang = lang
        }
        // mock
        `when`(languageRepository.findByLang(lang)).thenReturn(language)
        // sut
        val result = languageService.getByLang(lang)
        // verify
        verify(languageRepository, times(1)).findByLang(lang)
        Assert.assertEquals(lang, result.lang)
    }

    @Test
    fun getAllLanguagesTest() {
        // test data
        val lang = "NL"
        val language = Language().apply {
            this.lang = lang
        }
        val list = listOf(language)
        // mock
        `when`(languageRepository.findAll()).thenReturn(list)
        // sut
        val result = languageService.getAll()
        // verify
        verify(languageRepository, times(1)).findAll()
        Assert.assertEquals(list.size, result.size)
        Assert.assertTrue(result.containsAll(list))
    }

    @Test(expected = LanguageExistByLangException::class)
    fun createLanguageWhenLanguageAlreadyExistsTest() {
        // test data
        val lang = "NL"
        val language = Language().apply {
            this.lang = lang
        }
        // mock
        `when`(languageRepository.findByLang(lang)).thenReturn(language)
        // sut
        languageService.create(lang)
        // verify
        verify(languageRepository, times(1)).findByLang(lang)
    }

    @Test
    fun createLanguageWhenLanguageDoesNotExistsTest() {
        // test data
        val lang = "NL"
        val language = Language().apply {
            this.lang = lang
        }
        // mock
        `when`(languageRepository.findByLang(lang)).thenReturn(null)
        `when`(languageRepository.save(ArgumentMatchers.any(Language::class.java))).thenReturn(language)
        // sut
        val result = languageService.create(lang)
        // verify
        verify(languageRepository, times(1)).findByLang(lang)
        verify(languageRepository, times(1)).save(ArgumentMatchers.any(Language::class.java))
        Assert.assertNotNull("Language should not be null", result)
        Assert.assertEquals(lang, result.lang)
    }
}