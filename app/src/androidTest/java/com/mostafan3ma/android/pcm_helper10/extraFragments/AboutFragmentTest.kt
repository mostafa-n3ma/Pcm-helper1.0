package com.mostafan3ma.android.pcm_helper10.extraFragments

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.mostafan3ma.android.pcm_helper10.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class AboutFragmentTest{


    @get:Rule
    var instantExecutorRule= InstantTaskExecutorRule()

    @Test
    fun launchAboutFragment_MakeBottomTxtVisible_ScrollToBottomText(){
        val scenario=launchFragmentInContainer<AboutFragment>(Bundle(), R.style.Theme_Pcmhelper10)

        scenario.onFragment {
            it.visibleBottomTxt(true)
        }

        onView(withId(R.id.headline_text)).check(matches(isDisplayed()))

        onView(withId(R.id.bottom_txt)).perform(scrollTo(), click())
        Thread.sleep(1000)
    }


}