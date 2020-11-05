package fr.labard.simplegpstracker.model.util

import android.util.Xml
import fr.labard.simplegpstracker.model.data.local.db.Converters
import fr.labard.simplegpstracker.model.data.local.db.location.LocationEntity
import fr.labard.simplegpstracker.model.data.local.db.record.RecordEntity
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.util.*

class XmlParser {

    // We don't use namespaces
    private val ns: String? = null

    data class RecordList(val records: MutableList<RecordTag>) {
        fun toXml(): String {
            var s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
            for (record in records) {
                s += "<record id=\"${record.id}\" name=\"${record.name}\" creationdate=\"${record.creationDate}\" lastmodification=\"${record.lastModification}\">"
                for (location in record.locations) {
                    s += "        <location id=\"${location.id}\" time=\"${location.time}\" latitude=\"${location.latitude}\" longitude=\"${location.longitude}\" speed=\"${location.speed}\"/>"
                }
                s += "</record>"
            }

            return s
        }
    }

    data class RecordTag(
        val id: String,
        val name: String,
        val creationDate: Date,
        val lastModification: Date,
        val locations: MutableList<LocationTag>
    )

    data class LocationTag(
        val id: Int,
        val time: Long,
        val latitude: Double,
        val longitude: Double,
        val speed: Float
    )

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): RecordList {
        inputStream.use { inputStream1 ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream1, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): RecordList {
        val recordList = RecordList(mutableListOf())

//        parser.require(XmlPullParser.START_TAG, ns, "feed")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            // Starts by looking for the record tag
            if (parser.name == "record") {
                recordList.records.add(readRecordTag(parser))
            } else {
                skip(parser)
            }
        }
        return recordList
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readRecordTag(parser: XmlPullParser): RecordTag {
        parser.require(XmlPullParser.START_TAG, ns, "record")

        var id = parser.getAttributeValue(null, "id")
        val name = parser.getAttributeValue(null, "id")
        val creationDate = Converters().timestampToDate(parser.getAttributeValue(null, "creationdate").toLong())!!
        val lastModification = Converters().timestampToDate(parser.getAttributeValue(null, "lastModification").toLong())!!
        val locations = mutableListOf<LocationTag>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "location" -> locations.add(readLocationTag(parser))
                else -> skip(parser)
            }
        }
        return RecordTag(id, name, creationDate, lastModification, locations)
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLocationTag(parser: XmlPullParser): LocationTag {
        parser.require(XmlPullParser.START_TAG, ns, "location")
        val id = parser.getAttributeValue(null, "id").toInt()
        val time = parser.getAttributeValue(null, "time").toLong()
        val latitude = parser.getAttributeValue(null, "latitude").toDouble()
        val longitude = parser.getAttributeValue(null, "longitude").toDouble()
        val speed = parser.getAttributeValue(null, "speed").toDouble()
        parser.require(XmlPullParser.END_TAG, ns, "location")
        return LocationTag(id, time, latitude, longitude, speed)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

    fun export(records: List<RecordEntity>, locations: List<LocationEntity>): String {
        val recordList = RecordList(mutableListOf())
        for (record in records) {
            val recordTag = RecordTag(
                record.id,
                record.name,
                record.creationDate,
                record.lastModification,
                mutableListOf()
            )
            for (location in locations) {
                val locationTag = LocationTag(location.id, location.time, location.latitude, location.longitude, location.speed)
                recordTag.locations.add(locationTag)
            }
            recordList.records.add(recordTag)
        }
        return recordList.toXml()
    }
}

/*<?xml version="1.0" encoding="utf-8"?>
<record id="1" name="1" creationdate="0" lastmodification="1">
        <location id="0" time="0" latitude="0.0" longitude="0.0" speed="0.0"/>
</record>*/
