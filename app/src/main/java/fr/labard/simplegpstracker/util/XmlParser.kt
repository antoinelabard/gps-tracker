package fr.labard.simplegpstracker.util

import android.util.Xml
import fr.labard.simplegpstracker.data.local.Converters
import fr.labard.simplegpstracker.data.local.LocationEntity
import fr.labard.simplegpstracker.data.local.RecordEntity
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.util.*

class XmlParser {

    private val ns: String? = null

    data class RecordList(val recordTags: MutableList<RecordTag>) {
        fun toGpx(): String {
            var s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<${Constants.Gpx.GPX}>\n"
            for (record in recordTags) {
                s += "    <${Constants.Gpx.TRK} " +
                        "${Constants.Gpx.ID}=\"${record.id}\" " +
                        "${Constants.Gpx.CREATION_DATE}=\"${Converters().dateToTimestamp(record.creationDate)}\" " +
                        "${Constants.Gpx.LAST_MODIFICATION}=\"${Converters().dateToTimestamp(record.lastModification)}\">\n" +
                        "    <${Constants.Gpx.NAME}>${record.name}</${Constants.Gpx.NAME}>\n" +
                        "    <${Constants.Gpx.TRKSEG}>\n"
                for (location in record.locations) {
                    s += "        <${Constants.Gpx.TRKPT} " +
                            "${Constants.Gpx.ID}=\"${location.id}\" " +
                            "${Constants.Gpx.LATITUDE}=\"${location.latitude}\" " +
                            "${Constants.Gpx.LONGITUDE}=\"${location.longitude}\" " +
                            "${Constants.Gpx.SPEED}=\"${location.speed}\">" +
                            "<${Constants.Gpx.TIME}>${location.time}</${Constants.Gpx.TIME}>" +
                            "</${Constants.Gpx.TRKPT}>\n"
                }
                s += "    </${Constants.Gpx.TRKSEG}>\n    </${Constants.Gpx.TRK}>\n"
            }
            s += "</${Constants.Gpx.GPX}>\n"
            return s
        }

        fun toRecordsAndLocations(): Pair<MutableList<RecordEntity>, MutableList<LocationEntity>> {
            val r = mutableListOf<RecordEntity>()
            val l = mutableListOf<LocationEntity>()

            for (i in recordTags) {
                r.add(i.toRecordEntity())
                for (j in i.locations) {
                    l.add(j.toLocationEntity(i.id))
                }
            }
            return Pair(r, l)
        }
    }

    data class RecordTag(
        val id: String,
        val name: String,
        val creationDate: Date,
        val lastModification: Date,
        val locations: MutableList<LocationTag>
    ) {
        fun toRecordEntity() = RecordEntity(
            this@RecordTag.name,
            this@RecordTag.creationDate,
            this@RecordTag.lastModification
        ).apply { id = this@RecordTag.id }
    }

    data class LocationTag(
        val id: String,
        val time: Long,
        val latitude: Double,
        val longitude: Double,
        val speed: Float
    ) {
        fun toLocationEntity(recordId: String) = LocationEntity(
            recordId,
            this@LocationTag.time,
            this@LocationTag.latitude,
            this@LocationTag.longitude,
            this@LocationTag.speed
        ).apply { id = this@LocationTag.id }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun import(inputStream: InputStream): Pair<MutableList<RecordEntity>, MutableList<LocationEntity>> {
        inputStream.use { inputStream1 ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream1, null)
            parser.nextTag()
            return readFeed(parser).toRecordsAndLocations()
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readFeed(parser: XmlPullParser): RecordList {
        val recordList = RecordList(mutableListOf())

        parser.require(XmlPullParser.START_TAG, ns, Constants.Gpx.GPX)
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            if (parser.name == Constants.Gpx.TRK) {
                recordList.recordTags.add(readRecordTag(parser))
            } else {
                skip(parser)
            }
        }
        return recordList
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readRecordTag(parser: XmlPullParser): RecordTag {
        parser.require(XmlPullParser.START_TAG, ns, Constants.Gpx.TRK)

        val id = parser.getAttributeValue(ns, Constants.Gpx.ID)
        var name: String? = null
        val creationDate = parser.getAttributeValue(ns, Constants.Gpx.CREATION_DATE)
        val lastModification = parser.getAttributeValue(ns, Constants.Gpx.LAST_MODIFICATION)
        val locations = mutableListOf<LocationTag>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) continue
            when (parser.name) {
                Constants.Gpx.NAME -> name = readName(parser)
                Constants.Gpx.TRKPT -> locations.add(readLocationTag(parser))
                Constants.Gpx.TRKSEG -> continue
                else -> skip(parser)
            }
        }
        return RecordTag(
                id ?: UUID.randomUUID().toString(),
                name ?: "Record: ${Date()}", //TODO
                if (creationDate != null) Converters().timestampToDate(creationDate.toLong())!! else Date(),
                if (lastModification != null) Converters().timestampToDate(lastModification.toLong())!! else Date(),
                locations
        )
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readLocationTag(parser: XmlPullParser): LocationTag {
        parser.require(XmlPullParser.START_TAG, ns, Constants.Gpx.TRKPT)
        val id = parser.getAttributeValue(ns, Constants.Gpx.ID)
        var time = 0L
        val latitude = parser.getAttributeValue(ns, Constants.Gpx.LATITUDE)
        val longitude = parser.getAttributeValue(ns, Constants.Gpx.LONGITUDE)
        val speed = parser.getAttributeValue(ns, Constants.Gpx.SPEED)
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                Constants.Gpx.TIME -> time = readTime(parser)
                else -> skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, Constants.Gpx.TRKPT)
        return LocationTag(
            id?.toString() ?: UUID.randomUUID().toString(),
            time,
            latitude?.toDouble() ?: 0.0,
            longitude?.toDouble() ?: 0.0,
            speed?.toFloat() ?: 0f
        )
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTime(parser: XmlPullParser): Long {
        parser.require(XmlPullParser.START_TAG, ns, Constants.Gpx.TIME)
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, Constants.Gpx.TIME)
        return title.toLong()
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readName(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, Constants.Gpx.NAME)
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, Constants.Gpx.NAME)
        return title
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
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
            recordList.recordTags.add(recordTag)
        }
        return recordList.toGpx()
    }
}
