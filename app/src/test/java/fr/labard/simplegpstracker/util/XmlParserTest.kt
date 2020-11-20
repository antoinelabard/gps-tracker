package fr.labard.simplegpstracker.util

import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.labard.simplegpstracker.Data.Companion.le1
import fr.labard.simplegpstracker.Data.Companion.le2
import fr.labard.simplegpstracker.Data.Companion.r1
import fr.labard.simplegpstracker.data.local.Converters
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class XmlParserTest {

    // This is a GPX String storing the data of the RecordEntity r1 and the LocationEntities le1 and le2
    val r1xmlText =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<gpx>\n" +
            "    <trk id=\"${r1.id}\" creationdate=\"${Converters().dateToTimestamp(r1.creationDate)}\" lastmodification=\"${Converters().dateToTimestamp(r1.lastModification)}\">\n" +
            "    <name>${r1.name}</name>\n" +
            "    <trkseg>\n" +
            "        <trkpt id=\"${le1.id}\" lat=\"${le1.latitude}\" lon=\"${le1.longitude}\" speed=\"${le1.speed}\"><time>${le1.time}</time></trkpt>\n" +
            "        <trkpt id=\"${le2.id}\" lat=\"${le2.latitude}\" lon=\"${le2.longitude}\" speed=\"${le2.speed}\"><time>${le2.time}</time></trkpt>\n" +
            "    </trkseg>\n" +
            "    </trk>\n" +
            "</gpx>\n"

    @Test
    fun import() {
        val expect = Pair(mutableListOf(r1), mutableListOf(le1, le2))
        val result = XmlParser().import(r1xmlText.byteInputStream())
        assertEquals(expect, result)
    }

    @Test
    fun export() {
        val expect = r1xmlText
        val result = XmlParser().export(listOf(r1), listOf(le1, le2))
        assertEquals(expect, result)
    }
}