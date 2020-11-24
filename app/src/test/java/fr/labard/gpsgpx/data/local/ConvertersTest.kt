package fr.labard.gpsgpx.data.local

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.*

class ConvertersTest {

    val t = 946729800000 // date of 01/01/2000, 00h00
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