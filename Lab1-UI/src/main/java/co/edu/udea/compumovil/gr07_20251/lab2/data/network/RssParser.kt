package co.edu.udea.compumovil.gr07_20251.lab2.data.network

import android.util.Log
import co.edu.udea.compumovil.gr07_20251.lab2.data.model.RssEpisode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

suspend fun fetchEpisodesFromRss(url: String): List<RssEpisode> = withContext(Dispatchers.IO) {
    val episodes = mutableListOf<RssEpisode>()

    try {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = false
        val parser = factory.newPullParser()

        val connection = URL(url).openConnection()
        parser.setInput(connection.getInputStream(), null)

        var eventType = parser.eventType
        var title = ""
        var pubDate = ""
        var audioUrl = ""

        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagName = parser.name
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (tagName) {
                        "title" -> title = parser.nextText()
                        "pubDate" -> pubDate = parser.nextText()
                        "enclosure" -> {
                            audioUrl = parser.getAttributeValue(null, "url")
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (tagName == "item") {
                        episodes.add(RssEpisode(title, pubDate, audioUrl))
                        title = ""
                        pubDate = ""
                        audioUrl = ""
                    }
                }
            }
            eventType = parser.next()
        }
    } catch (e: Exception) {
        Log.e("RSS", "Error parsing RSS: ${e.message}")
    }

    return@withContext episodes
}
