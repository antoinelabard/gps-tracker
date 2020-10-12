package com.example.simplegpstracker.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GpsServiceTest {

    @get:Rule
    val serviceRule = ServiceTestRule()
}