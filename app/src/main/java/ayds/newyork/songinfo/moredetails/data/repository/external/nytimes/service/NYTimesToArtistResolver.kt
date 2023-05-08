package ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response
import java.util.*

private const val IN_LOCAL_REPOSITORY = "[*]"
private const val HTML_DIV_WIDTH = "400"
private const val HTML_FONT_FACE = "arial"
private const val OPEN_LABEL_HTML = "<html>"
private const val OPEN_DIV_WIDTH = "<div width="
private const val OPEN_FONT_FACE = "<font face="
private const val CLOSE_LABEL = ">"
private const val LINE_JUMP_HTML = "<br>"
private const val OPEN_LABEL_WORD_BLACK = "<b>"
private const val CLOSE_LABEL_WORD_BLACK = "</b>"
private const val CLOSE_LABEL_HTML = "</html>"
private const val CLOSE_LABEL_DIV = "</div>"
private const val CLOSE_LABEL_FONT = "</font>"

interface NYTimesToArtistResolver {
    fun getJson(callResponse: Response<String>): JsonObject
    fun getAsJsonObject(response: JsonObject): JsonElement?
    fun updateInfoArtist(abstract: JsonElement, nameArtist: String?): String
}

internal class JsonToArtistResolver : NYTimesToArtistResolver{
    override fun getJson(callResponse: Response<String>): JsonObject {
        val gson = Gson()
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    override fun getAsJsonObject(response: JsonObject): JsonElement? {
        return response["docs"].asJsonArray[0].asJsonObject["abstract"]
    }

    override fun updateInfoArtist(abstract: JsonElement, nameArtist: String?): String {
        val artistName = "$IN_LOCAL_REPOSITORY$nameArtist"
        val infoArtist = abstract.asString.replace("\\n", "\n")
        return textToHtml(infoArtist, artistName)
    }

    private fun textToHtml(text: String, term: String?): String {
        return with(StringBuilder()) {
            append(OPEN_LABEL_HTML)
            append(OPEN_DIV_WIDTH).append(HTML_DIV_WIDTH).append(CLOSE_LABEL)
            append(OPEN_FONT_FACE).append(HTML_FONT_FACE).append(CLOSE_LABEL)
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", LINE_JUMP_HTML)
                .replace(
                    "(?i)$term".toRegex(),
                    OPEN_LABEL_WORD_BLACK + term!!.uppercase(Locale.getDefault()) + CLOSE_LABEL_WORD_BLACK
                )
            append(textWithBold)
            append("$CLOSE_LABEL_FONT$CLOSE_LABEL_DIV$CLOSE_LABEL_HTML")
            toString()
        }
    }
}