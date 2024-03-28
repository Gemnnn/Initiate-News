package com.initiatetech.initiate_news

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.hasSibling
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.initiatetech.initiate_news.login.LoginActivity
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchFragmentTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(LoginActivity::class.java)


    private val email = "test@gmail.com"
    private val password = "!1Qtesttest"

    @Test
    fun TC_SEARCH_001_AddKeyword(){
        // Login
        loginAndNavigateToAccount()

        //navigate to account page
        onView(withId(R.id.action_search)).perform(click())

        // Type Keyword
        onView(withId(R.id.et_addKeyword))
            .perform(typeText("testkeyword"))
        closeSoftKeyboard()

        // Click Add Keyword
        onView(withId(R.id.btn_addKeyword)).perform(click())

        // Wait 15 second
        try {
            Thread.sleep(15000)  // Wait for 15 seconds
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        // Navigate to Home page
        onView(withId(R.id.action_home)).perform(click())

        // Click on a keyword that has news
        onView(withText("testkeyword"))
            .check(matches(isDisplayed()))
            .check(matches(withText("testkeyword")))
    }

    @Test
    fun TC_SEARCH_002_DeleteKeyword(){
        // Login
        loginAndNavigateToAccount()

        // Click Delete Keyword associated with the text "testkeyword"
        onView(allOf(withId(R.id.btnRemoveKeyword), hasSibling(withText("testkeyword")))).perform(click())
    }


    private fun loginAndNavigateToAccount() {
        //login
        onView(withId(R.id.et_email)).perform(typeText(email))
        closeSoftKeyboard()

        onView(withId(R.id.et_password)).perform(typeText(password))
        closeSoftKeyboard()

        onView(withId(R.id.btn_login)).perform(click())
    }
}