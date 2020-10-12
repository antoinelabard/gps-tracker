package com.example.simplegpstracker.model.db

import com.example.simplegpstracker.Data.Companion.l1
import org.junit.Test

import org.junit.Assert.*
import java.util.*
import kotlin.math.exp

class DbConvertersTest {

    val converters = Converters()

    @Test
    fun timestampToDate() {
        val expected = Date(l1.time)
        val result = converters.timestampToDate(l1.time)
        assertEquals(expected, result)
    }

    @Test
    fun dateToTimestamp() {
        val expected = l1.time
        val result = converters.dateToTimestamp(Date(l1.time))
        assertEquals(expected, result)
    }
}