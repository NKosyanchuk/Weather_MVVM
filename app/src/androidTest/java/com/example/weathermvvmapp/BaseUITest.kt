package com.example.weathermvvmapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.example.weathermvvmapp.utils.MockedWeatherServer
import com.example.weathermvvmapp.utils.RecyclerViewMatcher
import org.hamcrest.Matchers
import org.junit.Before

abstract class BaseUITest : BaseTest() {

    protected lateinit var mockedServer: MockedWeatherServer

    @Before
    override fun setup() {
        super.setup()
        mockedServer = MockedWeatherServer()
        sleepShort()
    }

    /**
     * Check is view visible
     * @id id of view
     */
    protected fun isViewVisible(id: Int) {
        onView(withId(id)).check(matches(isDisplayed()))
    }

    /**
     * Check if view is not visible
     * @id id of view
     */
    protected fun isViewNotVisible(id: Int) {
        onView(withId(id)).check(matches(Matchers.not(isDisplayed())))
    }

    /**
     * Check is text visible
     * @id string resource id
     */
    protected fun isTextVisible(resourceId: Int) {
        onView(withText(resourceId)).check(matches(isDisplayed()))
    }

    /**
     * Check is text visible
     * @string string
     */
    protected fun isTextVisible(string: String) {
        onView(withText(string)).check(matches(isDisplayed()))
    }

    /**
     * Click on text
     * @id string resource id
     */
    protected fun clickOnText(resourceId: Int) {
        onView(withText(resourceId)).perform(click())
    }

    /**
     * Click on text
     * @id string resource id
     */
    protected fun clickOnText(string: String) {
        onView(withText(string)).perform(click())
    }

    /**
     * Check is text not visible
     * @string string
     */
    protected fun isTextNotVisible(string: String) {
        onView(withText(string)).check(doesNotExist())
    }

    /**
     * Check is text not visible
     * @id string resource id
     */
    protected fun isTextNotVisible(resourceId: Int) {
        onView(withText(resourceId)).check(matches(Matchers.not(isDisplayed())))
    }

    /**
     * Click on view
     * @id id of view
     */
    protected fun clickOnView(id: Int) {
        onView(withId(id)).perform(click())
    }

    /**
     * Click on view
     * @id id of view
     */
    protected fun longClickOnView(id: Int) {
        onView(withId(id)).perform(longClick())
    }

    /**
     * Updates text attribute of a view
     * @viewId view id
     * @text text to be replaced
     */
    protected fun replaceText(viewId: Int, text: String) {
        onView(withId(viewId)).perform(
            click(), ViewActions.replaceText(text), ViewActions.closeSoftKeyboard()
        )
    }

    /**
     * Returns an action that scrolls to the view
     * @viewId view resource id
     */
    protected fun scrollToView(viewId: Int) {
        onView(withId(viewId)).perform(ViewActions.scrollTo())
    }

    /**
     * Returns RecyclerViewMatcher with provided id
     * @recyclerViewId recycler resource id
     */
    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    /**
     * Press system back button
     */
    fun pressSystemBackButton() {
        val device = UiDevice.getInstance(getInstrumentation())
        device.pressBack()
    }
}