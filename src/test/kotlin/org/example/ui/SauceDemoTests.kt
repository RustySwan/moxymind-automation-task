package org.example.ui

import com.microsoft.playwright.Page
import com.microsoft.playwright.junit.UsePlaywright
import org.example.ui.configuration.TestOptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("UI")
@UsePlaywright(SauceDemoTests.SauceDemoOptions::class)
class SauceDemoTests {

    class SauceDemoOptions : TestOptions(
        headlessDriver = System.getProperty("headless").toBoolean(),
        uiBaseUrl = "https://www.saucedemo.com/"
    )

    @BeforeEach
    fun setupForTest(page: Page) {
        // Playwright does not start with base url - needs to be navigated to.
        page.navigate("/")
    }

    @Test
    fun testValidLogin(page: Page) {
        // Login with valid inputs
        logIn(page, "standard_user", "secret_sauce")

        assert(page.url().contains("/inventory.html")) { "Page does not contain '/inventory.html' in its path." }
        assert(
            page.locator(INVENTORY_CONTAINER_LOCATOR).first().isVisible
        ) { "Page does not have inventory displayed." }
    }

    @Test
    fun testUnsuccessfulLogin(page: Page) {
        // Login with invalid inputs
        logIn(page, "invalid_user", "incorrect_password")

        assert(
            page.locator(ERROR_MESSAGE_CONTAINER_LOCATOR).textContent()
                .equals("Epic sadface: Username and password do not match any user in this service")
        )
        { "Page does not contain '/inventory.html' in its path." }
    }

    @Test
    fun testAddingItemsToTheCart(page: Page) {
        logIn(page, "standard_user", "secret_sauce")

        // Pick random inventory item and add it to shopping cart
        page.locator(INVENTORY_ADD_ITEM_BUTTON_LOCATOR).elementHandles().shuffled().first().click()
        assert(page.locator(SHOPPING_CART_BADGE_LOCATOR).textContent().equals("1"))

        // Pick second item and verify if the badge text is updated
        page.locator(INVENTORY_ADD_ITEM_BUTTON_LOCATOR).elementHandles().shuffled().first().click()
        assert(page.locator(SHOPPING_CART_BADGE_LOCATOR).textContent().equals("2"))

    }

    @Test
    fun testItemPurchase(page: Page) {
        logIn(page, "standard_user", "secret_sauce")

        // Pick random inventory item and add it to shopping cart
        page.locator(INVENTORY_ADD_ITEM_BUTTON_LOCATOR).elementHandles().shuffled().first().click()
        assert(page.locator(SHOPPING_CART_BADGE_LOCATOR).textContent().equals("1"))

        page.locator(SHOPPING_CART_BADGE_LOCATOR).click()
        page.locator(CHECKOUT_BUTTON).click()

        // Fill up checkout information form
        page.locator(FIRST_NAME_CHECKOUT_LOCATOR).fill("John")
        page.locator(LAST_NAME_CHECKOUT_LOCATOR).fill("Doe")
        page.locator(ZIP_NAME_CHECKOUT_LOCATOR).fill("60654")

        // Clicks continue button on checkout page
        page.locator(CONTINUE_BUTTON_CHECKOUT_LOCATOR).click()

        // Finish order
        page.locator(FINISH_BUTTON_CHECKOUT_LOCATOR).click()

        assert(page.locator(ORDER_COMPLETE_CONTAINER_LOCATOR).isVisible)
        assert(page.locator(ORDER_COMPLETE_HEADER_LOCATOR).textContent().equals("Thank you for your order!"))
        assert(
            page.locator(ORDER_COMPLETE_TEXT_LOCATOR).textContent()
                .equals("Your order has been dispatched, and will arrive just as fast as the pony can get there!")
        )
    }

    private fun logIn(loginPage: Page, userName: String, password: String) {
        with(loginPage) {
            this.locator(USER_INPUT_LOCATOR).fill(userName)
            this.locator(PASSWORD_INPUT_LOCATOR).fill(password)
            this.locator(LOGIN_BUTTON_LOCATOR).click()
        }
    }

    companion object Locators {
        // Login page
        const val ERROR_MESSAGE_CONTAINER_LOCATOR = "[data-test='error']"
        const val LOGIN_BUTTON_LOCATOR = "[data-test='login-button']"
        const val PASSWORD_INPUT_LOCATOR = "[data-test='password']"
        const val USER_INPUT_LOCATOR = "[data-test='username']"

        // Inventory page
        const val INVENTORY_ADD_ITEM_BUTTON_LOCATOR = "[data-test^='add-to-cart-']"
        const val INVENTORY_CONTAINER_LOCATOR = "[data-test='inventory-item']"
        const val SHOPPING_CART_BADGE_LOCATOR = "[data-test='shopping-cart-badge']"

        // Checkout pages
        const val CONTINUE_BUTTON_CHECKOUT_LOCATOR = "[data-test='continue']"
        const val FINISH_BUTTON_CHECKOUT_LOCATOR = "[data-test='finish']"
        const val FIRST_NAME_CHECKOUT_LOCATOR = "[data-test='firstName']"
        const val CHECKOUT_BUTTON = "[data-test='checkout']"
        const val LAST_NAME_CHECKOUT_LOCATOR = "[data-test='lastName']"
        const val ORDER_COMPLETE_CONTAINER_LOCATOR = "[data-test='checkout-complete-container']"
        const val ORDER_COMPLETE_HEADER_LOCATOR = "[data-test='complete-header']"
        const val ORDER_COMPLETE_TEXT_LOCATOR = "[data-test='complete-text']"
        const val ZIP_NAME_CHECKOUT_LOCATOR = "[data-test='postalCode']"
    }
}