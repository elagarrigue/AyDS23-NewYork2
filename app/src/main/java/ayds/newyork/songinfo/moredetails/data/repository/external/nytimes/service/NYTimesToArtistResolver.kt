package ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Response

interface NYTimesToArtistResolver {
    fun getJson(callResponse: Response<String>): JsonObject
    fun getAsJsonObject(response: JsonObject): JsonElement?
    fun updateInfoArtist(abstract: JsonElement, nameArtist: String?): String
    fun generateResponse(nyTimesAPI: NYTimesAPI, artistName: String?): JsonObject
    fun getResponse(nyTimesAPI: NYTimesAPI, artistName: String?): Response<String>
}

internal class JsonToArtistResolver : NYTimesToArtistResolver{
    override fun getJson(callResponse: Response<String>): JsonObject {
        val gson = Gson()
        return gson.fromJson(callResponse.body(), JsonObject::class.java)
    }

    override fun getAsJsonObject(response: JsonObject): JsonElement? {
        return response["docs"].asJsonArray[0].asJsonObject["abstract"]
    }

    override fun updateInfoArtist(abstract: JsonElement, nameArtist: String?): String {
        return abstract.asString.replace("\\n", "\n")
    }

    override fun generateResponse(nyTimesAPI: NYTimesAPI, artistName: String?): JsonObject {
        val callResponse = getResponse(nyTimesAPI, artistName)
        val jObj = getJson(callResponse)
        return jObj[ayds.newyork.songinfo.moredetails.data.repository.PROP_RESPONSE].asJsonObject
    }

    override fun getResponse(nyTimesAPI: NYTimesAPI, artistName: String?) =
        nyTimesAPI.getArtistInfo(artistName).execute()
}