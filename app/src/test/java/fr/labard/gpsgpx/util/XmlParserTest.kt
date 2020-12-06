package fr.labard.gpsgpx.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.gpsgpx.Data.Companion.le1
import fr.labard.gpsgpx.Data.Companion.le2
import fr.labard.gpsgpx.Data.Companion.r1
import fr.labard.gpsgpx.Data.Companion.r1GpxText
import fr.labard.gpsgpx.Data.Companion.r1GpxTextInvalid
import fr.labard.gpsgpx.data.local.LocationEntity
import fr.labard.gpsgpx.data.local.RecordEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class XmlParserTest {

    @Test
    fun import() {
        val expect = Pair(mutableListOf(r1), mutableListOf(le1, le2))
        val result = XmlParser().import(r1GpxText.byteInputStream())
        assertEquals(expect, result)
    }
    @Test
    fun importUnreadableFile_returnEmptyRecordList() {
        val expect = Pair(mutableListOf<RecordEntity>(), mutableListOf<LocationEntity>())
        val result = XmlParser().import(r1GpxTextInvalid.byteInputStream())
        assertEquals(expect, result)
    }

    @Test
    fun export() {
        val expect = r1GpxText
        val result = XmlParser().export(listOf(r1), listOf(le1, le2))
        assertEquals(expect, result)
    }
}