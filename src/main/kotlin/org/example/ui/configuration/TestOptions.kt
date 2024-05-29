package org.example.ui.configuration

import com.microsoft.playwright.APIRequest
import com.microsoft.playwright.Browser
import com.microsoft.playwright.junit.Options
import com.microsoft.playwright.junit.OptionsFactory

/**
 * Playwright Test options manageable by properties of class
 *
 * @property headless - headless Boolean parameter
 * @property uiBaseUrl - ui base url
 * @property apiBaseUrl - api base url
 */
open class TestOptions(
    private val headlessDriver: Boolean? = null,
    private val uiBaseUrl: String? = null,
    private val apiBaseUrl: String? = null
) : OptionsFactory {

    override fun getOptions(): Options {
        return Options()
            .apply {
                headlessDriver?.let { this.headless = it }
                // Set ui base url if present
                uiBaseUrl?.let { this.setContextOptions(Browser.NewContextOptions().setBaseURL(uiBaseUrl)) }
                // Set ui base url if present
                apiBaseUrl?.let { this.setApiRequestOptions(APIRequest.NewContextOptions().setBaseURL(apiBaseUrl)) }
            }
    }
}