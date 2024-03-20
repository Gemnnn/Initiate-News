package com.initiatetech.initiate_news;

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import com.initiatetech.initiate_news.login.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class UserFragmentTests {
    @get:Rule
    val intentsTestRule = IntentsTestRule(LoginActivity::class.java)

    private val email = "test@gmail.com"
    private val password = "!1Qtesttest"

    @Test
    fun testLogout() {
        //login and navigate to account page
        loginAndNavigateToAccount()

        //click logout button
        onView(withId(R.id.btn_logout)).perform(click())

        //Assert that that logout took the user back to the login screen
        Intents.getIntents().also { intents ->
            assertThat(intents).isNotEmpty()
            val mostRecentIntent = intents[intents.size - 1] // Get the most recent intent
            assertThat(mostRecentIntent.component?.className).isEqualTo(LoginActivity::class.java.name)
        }
    }

    @Test
    fun testGetPreferences() {
        //login and navigate to account page
        loginAndNavigateToAccount()

        //assert that preferences have been got
        onView(withId(R.id.tv_userEmail)).check(matches(withText(email)))
        // TODO: Maybe make more assertions on the rest of preference data?
    }

    @Test
    fun testSetPreferences() {
        //login and navigate to account page
        loginAndNavigateToAccount()

        //change time preference
        onView(withId(R.id.et_setTime)).perform(clearText())
        onView(withId(R.id.et_setTime)).perform(typeText("09:30"))

        //save preferences
        onView(withId(R.id.btn_set_preference)).perform(click())

        //Logout and then re-login/navigate
        onView(withId(R.id.btn_logout)).perform(click())
        loginAndNavigateToAccount()

        //Assert that the time preference has remained the same as what was set
        onView(withId(R.id.et_setTime)).check(matches(withText("09:30")))
        // TODO: Maybe make more assertions on the rest of preference data?

        // Cleanup by erasing and resetting the time before ending
        onView(withId(R.id.et_setTime)).perform(clearText())
        onView(withId(R.id.btn_set_preference)).perform(click())
    }

    private fun loginAndNavigateToAccount() {
        //login
        onView(withId(R.id.et_email)).perform(typeText(email))
        closeSoftKeyboard()
        onView(withId(R.id.et_password)).perform(typeText(password))
        closeSoftKeyboard()
        onView(withId(R.id.btn_login)).perform(click())

        //navigate to account page
        onView(withId(R.id.action_account)).perform(click())
    }
}
