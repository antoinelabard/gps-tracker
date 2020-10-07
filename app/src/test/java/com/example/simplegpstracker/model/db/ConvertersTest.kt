package com.example.simplegpstracker.model.db

import junit.framework.TestCase
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`

@RunWith(JUnit4::class)
class ConvertersTest : TestCase() {

    val t = 946729800000 // date of 01/01/2000
    val d = Date(t)

    @Test
    fun testTimestampToDate() {
        val result = Converters().timestampToDate(t)
        assertThat(d, `is`(equalTo(result)))
    }

    @Test
    fun testDateToTimestamp() {
        val result = Converters().dateToTimestamp(d)
        assertThat(t, `is`(equalTo(result)))
    }
}