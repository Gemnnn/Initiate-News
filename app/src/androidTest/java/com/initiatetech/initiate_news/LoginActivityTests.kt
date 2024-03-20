package com.initiatetech.initiate_news

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import com.initiatetech.initiate_news.login.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTests {
    @get:Rule
    val intentsTestRule = IntentsTestRule(LoginActivity::class.java)

    @Test
    fun testLoginSuccess() {
        // Enter valid email and password
        onView(withId(R.id.et_email)).perform(typeText("test@gmail.com"))
        closeSoftKeyboard()
        onView(withId(R.id.et_password)).perform(typeText("!1Qtesttest"))
        closeSoftKeyboard()

        // Click login
        onView(withId(R.id.btn_login)).perform(click())

        Intents.getIntents().also { intents ->
            assertThat(intents).isNotEmpty()
            val mostRecentIntent = intents[intents.size - 1] // Get the most recent intent
            assertThat(mostRecentIntent.component?.className).isEqualTo(MainActivity::class.java.name)
        }
    }

    @Test
    fun testLoginFailEmptyEmailAndPassword() {
        // Click login
        onView(withId(R.id.btn_login)).perform(click())

        // Assert that there is no intent because login failed with empty email/password
        assertThat(Intents.getIntents()).isEmpty()
    }

    @Test
    fun testLoginFailIncorrectEmail() {
        // Enter incorrect email
        onView(withId(R.id.et_email)).perform(typeText("wrong_email"))
        closeSoftKeyboard()

        // Enter valid password
        onView(withId(R.id.et_password)).perform(typeText("!1Qtesttest"))
        closeSoftKeyboard()

        // Click login
        onView(withId(R.id.btn_login)).perform(click())

        // Assert that there is no intent because login failed with incorrect email
        assertThat(Intents.getIntents()).isEmpty()
    }
}