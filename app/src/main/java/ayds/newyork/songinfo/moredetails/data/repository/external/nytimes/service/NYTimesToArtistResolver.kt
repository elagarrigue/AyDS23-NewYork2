package ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response

private const val PROP_RESPONSE = "response"
private const val WEB_URL = "web_url"
private const val DOCS = "docs"

interface NYTimesToArtistResolver {
    fun getJson(callResponse: Response<String>): JsonObject
    fun getAsJsonObject(response: JsonObject): JsonElement?
    fun artistInfoAbstractToString(abstract: JsonElement, nameArtist: String?): String
    fun getURL(artistName: String?): String
    fun generateFormattedResponse(nameArtist: String?): String?
    fun setNYTimesService(nyTimesService: NYTimesService)
}

internal class NYTimesToArtistResolverImpl: NYTimesToArtistResolver{
    private lateinit var nyTimesService: NYTimesService
    override fun getJson(callResponse: Response<String>): JsonObject {
        val gson = Gson()
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    override fun getAsJsonObject(response: JsonObject): JsonElement? {
        return response["docs"].asJsonArray[0].asJsonObject["abstract"]
    }

    override fun artistInfoAbstractToString(abstract: JsonElement, nameArtist: String?): String {
        return abstract.asString.replace("\\n", "\n")
    }

    override fun getURL(artistName: String?): String {
        val response = generateResponse(artistName)
        return response[DOCS].asJsonArray[0].asJsonObject[WEB_URL].asString
    }

    private fun generateResponse(artistName: String?): JsonObject {
        val callResponse = nyTimesService.getResponse(artistName)
        val jObj = getJson(callResponse)
        return jObj[PROP_RESPONSE].asJsonObject
    }

    override fun generateFormattedResponse(nameArtist: String?): String? {
        val response = generateResponse(nameArtist)
        val abstract = getAsJsonObject(response)
        return if (abstract == null)
            null
        else
            artistInfoAbstractToString(abstract, nameArtist)
    }

    override fun setNYTimesService(nyTimesService: NYTimesService) {
        this.nyTimesService = nyTimesService
    }
}