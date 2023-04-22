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


class OtherInfoWindow : AppCompatActivity() {
    private var textPane2: TextView? = null
    private var dataBase: DataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewInfo()
        initDataBase()
        open(intent.getStringExtra("artistName"))
    }

    private fun initDataBase(){
        dataBase = DataBase(this)
    }

    private fun initViewInfo(){
        setContentView(R.layout.activity_other_info)
        textPane2 = findViewById(R.id.textPane2)
    }

    private fun getArtistInfo(artistName: String?) {

        val retrofit = createRetroFit()
        val nyTimesAPI = createAPI(retrofit)

        Log.e("TAG", "artistName $artistName")
        Thread {
            var text: String? = dataBase?.getInfo(artistName)
            if (text != null) {
                text = "[*]$text"
            } else {
                val callResponse: Response<String>
                try {
                    callResponse = nyTimesAPI.getArtistInfo(artistName).execute()
                    Log.e("TAG", "JSON " + callResponse.body())
                    val gson = Gson()
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val response = jobj["response"].asJsonObject
                    val _abstract = response["docs"].asJsonArray[0].asJsonObject["abstract"]
                    val url = response["docs"].asJsonArray[0].asJsonObject["web_url"]
                    if (_abstract == null) {
                        text = "No Results"
                    } else {
                        text = _abstract.asString.replace("\\n", "\n")
                        text = textToHtml(text, artistName)


                        // save to DB  <o/
                        dataBase?.saveArtist(artistName, text)
                    }
                    val urlString = url.asString
                    findViewById<View>(R.id.openUrlButton).setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(urlString)
                        startActivity(intent)
                    }
                } catch (e1: IOException) {
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }
            val imageUrl = IMAGE_URL
            Log.e("TAG", "Get Image from $imageUrl")
            val finalText = text
            runOnUiThread {
                Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
                textPane2!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    private fun createAPI(retrofit: Retrofit): NYTimesAPI = retrofit.create(NYTimesAPI::class.java)

    private fun createRetroFit() : Retrofit = Retrofit.Builder()
        .baseUrl(LINK_API_NYTIMES)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private fun open(artist: String?) {
        dataBase?.saveArtist("test", "sarasa")
        var info = dataBase?.getInfo("test")
        Log.e("TAG", "" + info)
        Log.e("TAG", "" + dataBase?.getInfo("nada"))
        getArtistInfo(artist)
    }

    companion object {

        const val ARTIST_NAME_EXTRA = "artistName"
        const val LINK_API_NYTIMES = "https://api.nytimes.com/svc/search/v2/"
        const val HTML_DIV_WIDTH = "400"
        const val HTML_FONT_FACE = "arial"
        const val IMAGE_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

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