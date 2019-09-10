package com.challenge.tweetsviewerapp.ui.fragments

import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.ui.activities.HomeActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class TweetMapFragmentTest {


    @get:Rule
    var rule = ActivityTestRule<HomeActivity>(HomeActivity::class.java)

    var mActivity: HomeActivity? = null
    @Before
    fun setUp() {
        mActivity = rule.activity
        rule.activity
            .supportFragmentManager.beginTransaction();
    }

    @Test
    fun testFragment()
    {
        val container = mActivity?.findViewById<FrameLayout>(R.id.container)
        TestCase.assertNotNull(container)

        val fragment = TweetMapFragment()

        mActivity?.supportFragmentManager?.beginTransaction()?.replace(container?.id!!, fragment)
            ?.commitAllowingStateLoss()
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        val view = fragment.view?.findViewById<SeekBar>(R.id.seek_bar_distance)
        TestCase.assertNotNull(view)
        onView(withId(R.id.seek_bar_distance)).perform(ViewActions.swipeRight()).perform(ViewActions.swipeLeft())

    }

    @After
    fun tearDown() {
        mActivity = null
    }
}