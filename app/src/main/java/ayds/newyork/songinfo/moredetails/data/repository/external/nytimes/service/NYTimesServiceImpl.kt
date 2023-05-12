package ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.ArtistWithData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.EmptyArtistData
import com.google.gson.JsonObject
import retrofit2.Response
import java.io.IOException

interface NYTimesService {

    fun getArtistInfo(artistName: String?): ArtistData
    fun getResponse(artistName: String?): Response<String>
}

internal class NYTimesServiceImpl(
    private val nyTimesAPI: NYTimesAPI,
    private val nyTimesToArtistResolver: NYTimesToArtistResolver,
) : NYTimesService {

    override fun getArtistInfo(artistName: String?): ArtistData {
        var infoArtist: String? = null
        try {
            infoArtist = nyTimesToArtistResolver.generateFormattedResponse(artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return if(infoArtist == null){
            EmptyArtistData
        }
        else{
            ArtistWithData(artistName, infoArtist, nyTimesToArtistResolver.getURL(artistName), false)
        }
    }

    override fun getResponse(artistName: String?): Response<String> = nyTimesAPI.getArtistInfo(artistName).execute()
}