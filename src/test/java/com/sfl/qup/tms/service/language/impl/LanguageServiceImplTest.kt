package com.sfl.qup.tms.service.language.impl

import com.sfl.qup.tms.domain.language.Language
import com.sfl.qup.tms.persistence.language.LanguageRepository
import com.sfl.qup.tms.service.language.LanguageService
import com.sfl.qup.tms.service.language.exception.LanguageExistByLangException
import com.sfl.qup.tms.service.language.exception.LanguageNotFoundByIdException
import com.sfl.qup.tms.service.language.exception.LanguageNotFoundByLangException
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

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

    @Test(expected = LanguageNotFoundByIdException::class)
    fun getByIdWhenLanguageNotFoundTest() {
        // test data
        val id = 1L
        // mock
        `when`(languageRepository.findById(id)).thenReturn(Optional.ofNullable<Language>(null))
        // sut
        languageService.get(id)
        // verify
        verify(languageRepository, times(1)).findById(ArgumentMatchers.eq(id))
    }

    @Test
    fun getByIdWhenLanguageFoundTest() {
        // test data
        val id = 1L
        // mock
        `when`(languageRepository.findById(id)).thenReturn(Optional.of<Language>(Language()))
        // sut
        languageService.get(id)
        // verify
        verify(languageRepository, times(1)).findById(ArgumentMatchers.eq(id))
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