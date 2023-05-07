package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.service

import ayds.newyork.songinfo.moredetails.model.entities.ArtistData
import ayds.newyork.songinfo.moredetails.model.repository.*
import ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.NYTimesAPI
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import java.io.IOException
import java.util.*

private const val NO_RESULTS = "No Results"
private const val WEB_URL = "web_url"
private const val DOCS = "docs"
private const val PROP_RESPONSE = "response"

//similar a SpotifyToSongResolver
interface NYTimesToArtistResolver {
    fun getArtistInfoFromAPI(artistName: String?, nyTimesAPI: NYTimesAPI): ArtistData
    fun getURL(artistName: String?, nyTimesAPI: NYTimesAPI): String
    fun generateFormattedResponse(api: NYTimesAPI, nameArtist: String?): String
}

internal class JsonToArtistResolver : NYTimesToArtistResolver{

    override fun getArtistInfoFromAPI(artistName: String?, nyTimesAPI: NYTimesAPI): ArtistData {
        var infoArtist: String? = null
        try {
            infoArtist = generateFormattedResponse(nyTimesAPI, artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        val url = if (infoArtist == null) "" else getURL(artistName, nyTimesAPI)
        return ArtistData.ArtistWithData(infoArtist, url, false)
    }

    override fun getURL(artistName: String?, nyTimesAPI: NYTimesAPI): String {
        val response = generateResponse(nyTimesAPI, artistName)
        return response[DOCS].asJsonArray[0].asJsonObject[WEB_URL].asString
    }

    override fun generateFormattedResponse(api: NYTimesAPI, nameArtist: String?): String {
        val response = generateResponse(api, nameArtist)
        val abstract = getAsJsonObject(response)
        return if (abstract == null)
            NO_RESULTS
        else
            updateInfoArtist(abstract, nameArtist) //esto actualiza la vista, por lo tanto no deberia estar aca
    }

    private fun generateResponse(nyTimesAPI: NYTimesAPI, artistName: String?): JsonObject {
        val callResponse = getResponse(nyTimesAPI, artistName)
        val jObj = getJson(callResponse)
        return jObj[PROP_RESPONSE].asJsonObject
    }

    private fun getJson(callResponse: Response<String>): JsonObject {
        val gson = Gson()
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }
    private fun getResponse(nyTimesAPI: NYTimesAPI, artistName: String?) = nyTimesAPI.getArtistInfo(artistName).execute()
    private fun getAsJsonObject(response: JsonObject) = response["docs"].asJsonArray[0].asJsonObject["abstract"]


}