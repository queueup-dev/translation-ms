package com.sfl.qup.tms.service.language.impl

import com.sfl.qup.tms.domain.language.Language
import com.sfl.qup.tms.persistence.language.LanguageRepository
import com.sfl.qup.tms.service.language.LanguageService
import com.sfl.qup.tms.service.language.exception.LanguageNotFoundByLangException
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

    @Test(expected = LanguageNotFoundByLangException::class)
    fun getByLangWhenLanguageNotFoundTest() {
        // test data
        val lang = "NL"
        // mock
        `when`(languageRepository.findByLang(lang)).thenReturn(null)
        // sut
        languageService.get(lang)
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
        val result = languageService.get(lang)
        // verify
        verify(languageRepository, times(1)).findByLang(lang)
        Assert.assertEquals(lang, result.lang)
    }

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
        val result = languageService.find(lang)
        // verify
        verify(languageRepository, times(1)).findByLang(lang)
        Assert.assertNotNull(lang, result)
        Assert.assertEquals(lang, result!!.lang)
    }

    @Test
    fun createLanguageWhenLanguageAlreadyExistsTest() {
        // test data
        val lang = "NL"
        val language = Language().apply {
            this.lang = lang
        }
        // mock
        `when`(languageRepository.findByLang(lang)).thenReturn(language)
        // sut
        val result = languageService.create(lang)
        // verify
        verify(languageRepository, times(1)).findByLang(lang)
        Assert.assertNotNull("Language should not be null", result)
        Assert.assertEquals(lang, result.lang)
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