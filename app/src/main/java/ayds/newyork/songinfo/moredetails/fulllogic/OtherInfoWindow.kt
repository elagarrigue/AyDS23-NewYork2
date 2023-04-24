package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
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

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var textPane2: TextView
    private lateinit var dataBase: DataBase

    private val retrofit = createRetroFit()
    private val nyTimesAPI = createAPI(retrofit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewInfo()
        initDataBase()
        val artistName = intent.getStringExtra(ARTIST_NAME)
        loadArtistInfo(artistName)
    }

    private fun initDataBase() {
        dataBase = DataBase(this)
    }

    private fun initViewInfo() {
        setContentView(R.layout.activity_other_info)
        textPane2 = findViewById(R.id.textPane2)
    }

    private fun generateResponse(nyTimesAPI: NYTimesAPI, artistName: String?): JsonObject {
        val callResponse: Response<String> = nyTimesAPI.getArtistInfo(artistName).execute()
        Log.e("TAG", "JSON" + callResponse.body())
        val gson = Gson()
        val jObj = gson.fromJson(callResponse.body(), JsonObject::class.java)
        return jObj["response"].asJsonObject
    }

    private fun loadArtistInfo(artistName: String?) {
        Log.e("TAG", "artistName $artistName")
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
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }
            setImage(infoArtista)
        }.start()
    }

    private fun setImage(infoArtista: String?) {
        Log.e("TAG", "Get Image from $IMAGE_URL")
        runOnUiThread {
            Picasso.get().load(IMAGE_URL).into(findViewById<View>(R.id.imageView) as ImageView)
            textPane2.text = Html.fromHtml(infoArtista)
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

    companion object {

        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html>")
            builder.append("<div width=").append(HTML_DIV_WIDTH).append(">")
            builder.append("<font face=").append(HTML_FONT_FACE).append(">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
                )
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }
}