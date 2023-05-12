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
    fun getURL(response: Response<String>): String
    fun generateFormattedResponse(response: Response<String>, nameArtist: String?): String?
}

internal class NYTimesToArtistResolverImpl : NYTimesToArtistResolver{
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

    override fun getURL(response: Response<String>): String {
        val jsonResponse = generateResponse(response)
        return jsonResponse[DOCS].asJsonArray[0].asJsonObject[WEB_URL].asString
    }

    private fun generateResponse(response: Response<String>): JsonObject {
        val jObj = getJson(response)
        return jObj[PROP_RESPONSE].asJsonObject
    }

    override fun generateFormattedResponse(response: Response<String>, nameArtist: String?): String? {
        val jsonResponse = generateResponse(response)
        val abstract = getAsJsonObject(jsonResponse)
        return if (abstract == null)
            null
        else
            artistInfoAbstractToString(abstract, nameArtist)
    }

}