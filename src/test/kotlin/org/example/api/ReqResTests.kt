package org.example.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.microsoft.playwright.APIRequestContext
import com.microsoft.playwright.APIResponse
import com.microsoft.playwright.junit.UsePlaywright
import com.microsoft.playwright.options.RequestOptions
import org.example.api.reqres.models.CreatedUser
import org.example.api.reqres.models.UserInput
import org.example.api.reqres.models.Users
import org.example.ui.configuration.TestOptions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.test.assertEquals

@Tag("API")
@UsePlaywright(ReqResTests.ReqRes::class)
class ReqResTests {

    class ReqRes : TestOptions(
        apiBaseUrl = "https://reqres.in/"
    )

    @Test
    fun testGetListUsers(request: APIRequestContext) {
        val response = request.get("$USERS_PATH?page=2")

        // Assert if status code is 200
        assertResponseCode(response, 200)

        // Parsing response via jackson objectmapper provides data types check
        val jsonResponseObj = objectMapper.readValue(response.body(), Users::class.java)

        // Total value assertion
        jsonResponseObj.total.let {
            assert(it == 12) { "Total value is not 12 but $it." }
        }

        // Assertion of last names of first two users
        jsonResponseObj.data.let {
            assert(it[0].lastName == "Lawson") { "Last name of first user is not Lawson but '${it[0].lastName}'." }
            assert(it[1].lastName == "Ferguson") { "Last name of first user is not Ferguson but '${it[0].lastName}'." }
        }

        // Assert if total value represents real data size.
        assert(jsonResponseObj.perPage == jsonResponseObj.data.size)

        // Ensure total is at least the number of users on this page
        assert(jsonResponseObj.total >= jsonResponseObj.data.size) {
            "The total number of users should be greater than or equal to the number of users in the current page"
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/post_data.csv"], numLinesToSkip = 1)
    fun testPostCreateUser(name: String, job: String, request: APIRequestContext ) {
        val startDateTime = Date()
        val response = request.post(USERS_PATH, RequestOptions.create().setData(UserInput(name, job)))
        val afterResponseDateTime = Date()

        // Assert if status code is 201
        assertResponseCode(response, 201)

        val body = objectMapper.readValue(response.body(), CreatedUser::class.java)

        // Assert if id is present
        assert(!body.id.isNullOrEmpty()) {"Id field is null or empty. Value: ${body.id}"}

        // Assert if createdAt is valid
        assert(isValidISO8601(body.createdAt!!)) { "'createdAt' is not a valid ISO 8601 date string" }

        val elapsedTime = afterResponseDateTime.time - startDateTime.time

        // Response time assert - may differ from reality a bit because of request method time overhead.
        // Playwright do not support retrieval of information about response time - more here: https://github.com/microsoft/playwright/issues/19621
        assert(elapsedTime < 700) {"Response time is higher than 100 ms. Value: ${elapsedTime}ms;"}
    }

    private fun assertResponseCode(apiResponse: APIResponse, responseStatus: Int) {
        assertEquals(
            apiResponse.status(),
            responseStatus,
            "Response is not 200. Response code: ${apiResponse.status()}"
        )
    }

    private fun isValidISO8601(dateString: String): Boolean {
        return try {
            Instant.parse(dateString)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    companion object {
        var objectMapper = jacksonObjectMapper()

        const val USERS_PATH = "api/users"
    }

}