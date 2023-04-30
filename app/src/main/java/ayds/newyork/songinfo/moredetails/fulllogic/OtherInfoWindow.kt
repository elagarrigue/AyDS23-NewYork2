package ayds.newyork.songinfo.moredetails.fulllogic
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*
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
class OtherInfoWindow : AppCompatActivity() {
    private lateinit var textInfoWindow: TextView
    private lateinit var dataBase: DataBase
    private val retrofit = createRetroFit()
    private val nyTimesAPI = createAPI(retrofit)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        initViewInfo()
        initDataBase()
        val artistName = intent.getStringExtra(ARTIST_NAME)
        loadArtistInfo(artistName)
    }
    private fun initDataBase() {
        dataBase = DataBase(this)
    }
    private fun initViewInfo() {
        textInfoWindow = findViewById(R.id.textInfo)
    }
    private fun generateResponse(nyTimesAPI: NYTimesAPI, artistName: String?): JsonObject {
        val callResponse = getResponse(nyTimesAPI, artistName)
        val jObj = getJson(callResponse)
        return jObj[PROP_RESPONSE].asJsonObject
    }
    private fun getResponse(nyTimesAPI: NYTimesAPI, artistName: String?) = nyTimesAPI.getArtistInfo(artistName).execute()
    private fun getJson(callResponse : Response<String>): JsonObject {
        val gson = Gson()
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }
    private fun loadArtistInfo(artistName: String?) {
        Thread {
            var infoArtista: String? = dataBase.getInfo(artistName)
            if (infoArtista != null) {
                infoArtista = "$IN_LOCAL_REPOSITORY$infoArtista"
            } else {
                try {
                    val response = generateResponse(nyTimesAPI, artistName)
                    val abstract = response["docs"].asJsonArray[0].asJsonObject["abstract"]
                    if (abstract == null) {
                        infoArtista = "No Results"
                    } else {
                        infoArtista = abstract.asString.replace("\\n", "\n")
                        infoArtista = textToHtml(infoArtista, artistName)
                        saveArtistOnDatabase(artistName, infoArtista)
                    }
                    setButtonClickListener(infoArtista)
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
            setImage(infoArtista)
        }.start()
    }

    private fun setImage(infoArtista: String?) {
        runOnUiThread {
            Picasso.get().load(IMAGE_URL).into(findViewById<View>(R.id.imageView) as ImageView)
            textInfoWindow.text = Html.fromHtml(infoArtista)
        }
    }

    private fun saveArtistOnDatabase(artistName: String?, infoArtista: String) {
        dataBase.saveArtist(artistName, infoArtista)
    }

    private fun setButtonClickListener(artistName: String?) {
        val response = generateResponse(nyTimesAPI, artistName)
        val url = response["docs"].asJsonArray[0].asJsonObject["web_url"]
        val urlString = url.asString
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlString)
            startActivity(intent)
        }
    }

    private fun createAPI(retrofit: Retrofit): NYTimesAPI = retrofit.create(NYTimesAPI::class.java)

    private fun createRetroFit(): Retrofit = Retrofit.Builder()
        .baseUrl(LINK_API_NYTIMES)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun textToHtml(text: String, term: String?): String {
        return with(StringBuilder()) {
            append(OPEN_LABEL_HTML)
            append(OPEN_DIV_WIDTH).append(HTML_DIV_WIDTH).append(CLOSE_LABEL)
            append(OPEN_FONT_FACE).append(HTML_FONT_FACE).append(CLOSE_LABEL)
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", LINE_JUMP_HTML)
                .replace("(?i)$term".toRegex(), OPEN_LABEL_WORD_BLACK + term!!.uppercase(Locale.getDefault()) + CLOSE_LABEL_WORD_BLACK)
            append(textWithBold)
            append("$CLOSE_LABEL_FONT$CLOSE_LABEL_DIV$CLOSE_LABEL_HTML")
            toString()
        }
    }

}