package ayds.newyork.songinfo.moredetails.presentation

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import java.util.*

private const val IN_LOCAL_REPOSITORY = "[*]"

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
private const val HTML_DIV_WIDTH = "400"
private const val HTML_FONT_FACE = "arial"


interface RepositoryToViewFormatter {
    //fun textToHTML(text: String?, term: String?): String
    fun format(artist: ArtistData?):String
}

class RepositoryToViewFormatterImpl:RepositoryToViewFormatter {
     private fun textToHTML(text: String?, term: String?): String {
        return with(StringBuilder()) {
            append(OPEN_LABEL_HTML)
            append(OPEN_DIV_WIDTH).append(HTML_DIV_WIDTH).append(CLOSE_LABEL)
            append(OPEN_FONT_FACE).append(HTML_FONT_FACE).append(CLOSE_LABEL)
            val textWithBold = text!!
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


    override fun format(artist: ArtistData?): String {
        if(artist is ArtistData.ArtistWithData){
            val artistName = "$IN_LOCAL_REPOSITORY$artist"
            return textToHTML(infoArtist, artist.info)

        }

    }


}