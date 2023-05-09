package ayds.newyork.songinfo.moredetails.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import com.google.gson.JsonElement
import com.squareup.picasso.Picasso
import java.util.*

private const val IN_LOCAL_REPOSITORY = "[*]"
private const val IMAGE_URL =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
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

interface MoreDetailsView {
    fun initViewInfo()
    fun setView(artistData: ArtistData)
    fun setButtonClickListener(urlString: String)
    fun setImage(infoArtist: String?)
    fun textToHTML(text: String, term: String?): String
}

abstract class MoreDetailsViewImpl(moreDetailsPresenter: MoreDetailsPresenter) : MoreDetailsView, AppCompatActivity() {
    private var presenter = moreDetailsPresenter
    private lateinit var textInfoWindow: TextView
    private lateinit var artistName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initViewInfo()
        presenter.initDataBase()
        presenter.setArtistName(intent.getStringExtra(artistName))
        presenter.loadArtistInfo()
    }

    //Esto no se si va aca, lo puede pasar como setArtistName
//    override fun obtainArtistName() {
//        artistName = intent.getStringExtra(ARTIST_NAME)!!
//    }

    override fun initViewInfo() {
        textInfoWindow = findViewById(R.id.textInfo)
    }

    override fun setView(artistData: ArtistData) {
        if(artistData is ArtistData.ArtistWithData) {
            setButtonClickListener(artistData.url)
            setImage(artistData.info)
        }
    }

    override fun setButtonClickListener(urlString: String) {
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    override fun setImage(infoArtist: String?) {
        runOnUiThread {
            val imageView = findViewById<ImageView>(R.id.imageView)
            Picasso.get().load(IMAGE_URL).into(imageView)
            if (infoArtist != null)
                textInfoWindow.text =
                    HtmlCompat.fromHtml(infoArtist, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }
    override fun textToHTML(text: String, term: String?): String {
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
    private fun updateInfoArtist(abstract: JsonElement, nameArtist: String?): String {
        val artistName = "$IN_LOCAL_REPOSITORY$nameArtist"
        val infoArtist = abstract.asString.replace("\\n", "\n")
        return textToHTML(infoArtist, artistName)
    }
}