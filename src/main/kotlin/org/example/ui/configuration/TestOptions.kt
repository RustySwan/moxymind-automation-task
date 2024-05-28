package org.example.ui.configuration

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
    private val headless: Boolean,
    private val uiBaseUrl: String? = null,
    private val apiBaseUrl: String? = null
) : OptionsFactory {

    override fun getOptions(): Options {
        return Options()
            .setHeadless(headless)
            .apply {
                // Set ui base url if present
                if (uiBaseUrl != null) {
                    this.setContextOptions(Browser.NewContextOptions().setBaseURL(uiBaseUrl))
                }
                // Set api base url if present
                if (apiBaseUrl != null) {
                    this.setContextOptions(Browser.NewContextOptions().setBaseURL(apiBaseUrl))
                }
            }
    }
}