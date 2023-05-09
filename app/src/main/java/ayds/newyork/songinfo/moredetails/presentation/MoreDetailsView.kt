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
import ayds.newyork.songinfo.moredetails.data.repository.ARTIST_NAME
import ayds.newyork.songinfo.moredetails.data.repository.IMAGE_URL
import ayds.newyork.songinfo.moredetails.data.repository.IN_LOCAL_REPOSITORY
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import com.google.gson.JsonElement
import com.squareup.picasso.Picasso

private const val IN_LOCAL_REPOSITORY = "[*]"
private const val IMAGE_URL =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

interface MoreDetailsView {
    fun initViewInfo()
    fun setView(artistData: ArtistData)
    fun setButtonClickListener(urlString: String)
    fun setImage(infoArtist: String?)
}

class MoreDetailsViewImpl(moreDetailsPresenter: MoreDetailsPresenter) : MoreDetailsView, AppCompatActivity() {
    private var presenter = moreDetailsPresenter
    private lateinit var textInfoWindow: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initViewInfo()
        presenter.initDataBase()
        //presenter.setArtistName(intent.getStringExtra(ARTIST_NAME)!!)
        presenter.initThreadLoadArtistInfo()
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

    private fun updateInfoArtist(abstract: JsonElement, nameArtist: String?): String {
        val artistName = "$IN_LOCAL_REPOSITORY$nameArtist"
        val infoArtist = abstract.asString.replace("\\n", "\n")
        return textToHtml(infoArtist, artistName)
    }
}