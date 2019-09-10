package com.challenge.tweetsviewerapp.ui.activities

import androidx.appcompat.widget.Toolbar
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.challenge.tweetsviewerapp.R
import junit.framework.TestCase
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @get:Rule
    var rule = ActivityTestRule<HomeActivity>(HomeActivity::class.java)

    private var mActivity: HomeActivity? = null
    @Before
    fun setUp() {
        mActivity = rule.activity
        rule.activity
            .supportFragmentManager.beginTransaction()
    }


    @Test
    fun testLaunch() {
        val view = mActivity?.findViewById<Toolbar>(R.id.tool_bar)
        TestCase.assertNotNull(view)
    }
    @After
    fun tearDown() {
        mActivity=null
    }
}