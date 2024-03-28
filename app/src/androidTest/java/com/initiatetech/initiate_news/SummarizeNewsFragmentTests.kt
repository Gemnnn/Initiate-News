package com.initiatetech.initiate_news

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.initiatetech.initiate_news.login.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SummarizeNewsFragmentTests {
    @get:Rule
    val intentsTestRule = IntentsTestRule(LoginActivity::class.java)


    private val email = "test@gmail.com"
    private val password = "!1Qtesttest"

    @Test
    fun test_TC_GET_NEWS_001_DisplayNewsList() {
        // Log in and navigate to Home Page
        loginAndNavigateToAccount()

        // Click on a keyword that has news
        onView(withText("baseball")).perform(click())

        // Check that the RecyclerView with news list is displayed
        onView(withId(R.id.rv_timeline)).check(matches(isDisplayed()))

    }

    @Test
    fun test_TC_GET_NEWS_002_DisplayNoNewsMessage() {
        // Log in and navigate to Home Page
        loginAndNavigateToAccount()

        // Click on a keyword that has no news
        onView(withText("testtest")).perform(click())

        // Check for the no news message
        onView(withId(R.id.tvNoNewsMessage))
            .check(matches(isDisplayed()))
            .check(matches(withText("There is no news related to the testtest.")))
    }


    private fun loginAndNavigateToAccount() {
        //login
        onView(withId(R.id.et_email)).perform(ViewActions.typeText(email))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.et_password)).perform(ViewActions.typeText(password))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.btn_login)).perform(click())
    }
}
