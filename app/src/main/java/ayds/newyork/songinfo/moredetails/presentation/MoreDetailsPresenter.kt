package ayds.newyork.songinfo.moredetails.presentation

import ayds.newyork.songinfo.moredetails.data.repository.*
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.CursorToArtistDataMapper
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import java.util.*

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

interface MoreDetailsPresenter {
    fun initDataBase()
    fun initThreadLoadArtistInfo()
    fun markArtistAsLocal(artistData: ArtistData)
    fun textToHtml(text: String, term: String?): String
}

class MoreDetailsPresenterImpl(moreDetailsView: MoreDetailsView) : MoreDetailsPresenter {
    private lateinit var view : MoreDetailsView
    private lateinit var repository: ArtistRepository

    override fun initDataBase() {
        repository = ArtistLocalStorageImpl(this)
    }

    override fun initThreadLoadArtistInfo() {
        Thread {
            val artistData = repository.getArtistData()
            if(artistData != null)
                view.setView(artistData)
        }.start()
    }

    override fun markArtistAsLocal(artistData: ArtistData){
        if(artistData is ArtistData.ArtistWithData)
            artistData.isInDatabase = true
    }

    override fun textToHtml(text: String, term: String?): String {
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