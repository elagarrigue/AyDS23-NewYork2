package ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service


import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.NYTimesService
import com.google.gson.JsonObject
import java.io.IOException

private const val PROP_RESPONSE = "response"
private const val NO_RESULTS = "No Results"
private const val WEB_URL = "web_url"
private const val DOCS = "docs"

internal class NYTimesServiceImpl(
    private val nyTimesAPI: NYTimesAPI,
    private val nyTimesToArtistResolver: NYTimesToArtistResolver,
) : NYTimesService {
    override fun getArtistInfo(artistName: String?): ArtistData {
        var infoArtist: String? = null
        try {
            infoArtist = generateFormattedResponse(artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        val url = if (infoArtist == null) "" else getURL(artistName)
        return ArtistData.ArtistWithData(infoArtist, url, false)
    }

    override fun getURL(artistName: String?): String {
        val response = generateResponse(artistName)
        return response[DOCS].asJsonArray[0].asJsonObject[WEB_URL].asString
    }

    private fun generateResponse(artistName: String?): JsonObject {
        val callResponse = getResponse(artistName)
        val jObj = nyTimesToArtistResolver.getJson(callResponse)
        return jObj[PROP_RESPONSE].asJsonObject
    }
    override fun generateFormattedResponse(nameArtist: String?): String {
        val response = generateResponse(nameArtist)
        val abstract = nyTimesToArtistResolver.getAsJsonObject(response)
        return if (abstract == null)
            NO_RESULTS
        else
            nyTimesToArtistResolver.updateInfoArtist(abstract, nameArtist)
    }
    private fun getResponse(artistName: String?) = nyTimesAPI.getArtistInfo(artistName).execute()
}