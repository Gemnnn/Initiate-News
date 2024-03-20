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
import com.google.firebase.Firebase
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.initiatetech.initiate_news.register.OtpActivity
import com.initiatetech.initiate_news.register.RegisterActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RegisterActivityTests {
    @get:Rule
    val intentsTestRule = IntentsTestRule(RegisterActivity::class.java)

    @Test
    fun testRegisterSuccess() {
        // Enter a valid email and password
        onView(withId(R.id.editTextEmail)).perform(typeText("initiatenews@gmail.com"))
        closeSoftKeyboard()
        onView(withId(R.id.editTextPassword)).perform(typeText("Test123!"))
        closeSoftKeyboard()

        // Click register
        onView(withId(R.id.buttonRegister)).perform(click())

        // Assert that the most recent intent was for OtpActivity
        Intents.getIntents().also { intents ->
            assertThat(intents).isNotEmpty()
            val mostRecentIntent = intents[intents.size - 1] // Get the most recent intent
            assertThat(mostRecentIntent.component?.className).isEqualTo(OtpActivity::class.java.name)
        }

        //TODO: Include Firebase OTP validation as part of registration success?
    }

    @Test
    fun testRegisterFailEmptyEmailAndPassword() {
        // Type a valid email with no password entered
        onView(withId(R.id.editTextEmail)).perform(typeText("test@gmail.com"))

        // Click register button
        onView(withId(R.id.buttonRegister)).perform(click())

        // Assert that the intent never moved from RegisterActivity
        assertThat(Intents.getIntents()).isEmpty()
    }

    @Test
    fun testRegisterFailInvalidEmail() {
        // Type an invalid email (e.g., missing "@" symbol)
        onView(withId(R.id.editTextEmail)).perform(typeText("invalidemail.com"))

        // Type a valid password
        onView(withId(R.id.editTextPassword)).perform(typeText("Test123!"))

        // Click register button
        onView(withId(R.id.buttonRegister)).perform(click())

        // Assert that the intent never moved from RegisterActivity
        assertThat(Intents.getIntents()).isEmpty()
    }
}