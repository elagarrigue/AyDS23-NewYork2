package ayds.newyork.songinfo.moredetails.model.repository

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.text.HtmlCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.NYTimesAPI
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*
import ayds.newyork.songinfo.moredetails.model.entities.ArtistData
import ayds.newyork.songinfo.moredetails.model.repository.local.sqldb.ArtistLocalStorageImpl

private const val IN_LOCAL_REPOSITORY = "[*]"
const val ARTIST_NAME = "artistName"
private const val LINK_API_NYTIMES = "https://api.nytimes.com/svc/search/v2/"
private const val HTML_DIV_WIDTH = "400"
private const val HTML_FONT_FACE = "arial"
private const val IMAGE_URL =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
private const val PROP_RESPONSE = "response"
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
private const val NO_RESULTS = "No Results"
private const val WEB_URL = "web_url"
private const val DOCS = "docs"

@Suppress("KotlinConstantConditions")
class OtherInfoWindow : AppCompatActivity() {
    private lateinit var textInfoWindow: TextView
    private lateinit var artistLocalStorageImpl: ArtistLocalStorageImpl
    private val retrofit = createRetroFit()
    private val nyTimesAPI = createAPI(retrofit)
    private lateinit var artistName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initViewInfo()
        initDataBase()
        obtainArtistName()
        initThreadLoadArtistInfo()
    }

    private fun obtainArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME)!!
    }

    private fun initDataBase() {
        artistLocalStorageImpl = ArtistLocalStorageImpl(this)
    }

    private fun initViewInfo() {
        textInfoWindow = findViewById(R.id.textInfo)
    }

    //esto va en NYTimesToArtistResolver o al menos lo del asJsonObject
    private fun generateResponse(nyTimesAPI: NYTimesAPI, artistName: String?): JsonObject {
        val callResponse = getResponse(nyTimesAPI, artistName)
        val jObj = getJson(callResponse)
        return jObj[PROP_RESPONSE].asJsonObject
    }

    private fun getResponse(nyTimesAPI: NYTimesAPI, artistName: String?) =
        nyTimesAPI.getArtistInfo(artistName).execute()

    //esto va en NYTimesToArtistResolver
    private fun getJson(callResponse: Response<String>): JsonObject {
        val gson = Gson()
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    //controller
    private fun initThreadLoadArtistInfo() {
        Thread {
            val artistData = getArtistData()
            if(artistData != null)
                setView(artistData)
        }.start()
    }

    //model, repository
    private fun getArtistData(): ArtistData? {
        var artistData = getArtistInfoFromDatabase()
        when {
            artistData != null -> markArtistAsLocal(artistData)
            else -> {
                try {
                    artistData = getArtistInfoFromAPI()
                    artistData.let {
                        if(artistData is ArtistData.ArtistWithData)
                            artistLocalStorageImpl.saveArtist(artistName, artistData.info!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return artistData
    }

    //controller
    private fun markArtistAsLocal(artistData: ArtistData){
        if(artistData is ArtistData.ArtistWithData)
            artistData.isInDatabase = true
    }

    //vista
    private fun setView(artistData: ArtistData) {
        if(artistData is ArtistData.ArtistWithData) {
            setButtonClickListener(artistData.url)
            setImage(artistData.info)
        }
    }

    //va en model
    private fun getArtistInfoFromDatabase(): ArtistData? {
        val infoArtist: String? = artistLocalStorageImpl.getInfo(artistName)
        return if(infoArtist == null)
            null
        else {
            val url = getURL(artistName)
            ArtistData.ArtistWithData(infoArtist, url, true)
        }
    }

    //esto va en NYTimesToArtistResolver
    private fun getArtistInfoFromAPI(): ArtistData {
        var infoArtist: String? = null
        try {
            infoArtist = generateFormattedResponse(nyTimesAPI, artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        val url = if (infoArtist == null) "" else getURL(artistName)
        return ArtistData.ArtistWithData(infoArtist, url, false)
    }

    //esto va en NYTimesToArtistResolver
    private fun generateFormattedResponse(api: NYTimesAPI, nameArtist: String?): String {
        val response = generateResponse(api, nameArtist)
        val abstract = getAsJsonObject(response)
        return if (abstract == null)
            NO_RESULTS
        else
            updateInfoArtist(abstract, nameArtist)
    }

    //esto actualiza la vista
    private fun updateInfoArtist(abstract: JsonElement, nameArtist: String?): String {
        artistName = "$IN_LOCAL_REPOSITORY$nameArtist"
        val infoArtist = abstract.asString.replace("\\n", "\n")
        return textToHtml(infoArtist, artistName)
    }

    //esto va en NYTimesToArtistResolver
    private fun getAsJsonObject(response: JsonObject) =
        response["docs"].asJsonArray[0].asJsonObject["abstract"]

    //vista
    private fun setImage(infoArtist: String?) {
        runOnUiThread {
            val imageView = findViewById<ImageView>(R.id.imageView)
            Picasso.get().load(IMAGE_URL).into(imageView)
            if (infoArtist != null)
                textInfoWindow.text =
                    HtmlCompat.fromHtml(infoArtist, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    //esto va en NYTimesToArtistResolver
    private fun getURL(artistName: String?): String {
        val response = generateResponse(nyTimesAPI, artistName)
        return response[DOCS].asJsonArray[0].asJsonObject[WEB_URL].asString
    }

    //vista
    private fun setButtonClickListener(urlString: String) {
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    //NYTimesServiceInjector
    private fun createAPI(retrofit: Retrofit): NYTimesAPI = retrofit.create(
        NYTimesAPI::class.java)

    //NYTimesServiceInjector
    private fun createRetroFit(): Retrofit = Retrofit.Builder()
        .baseUrl(LINK_API_NYTIMES)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    //view o presenter
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