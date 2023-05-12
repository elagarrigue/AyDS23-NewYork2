package ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.ArtistWithData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.EmptyArtistData
import java.io.IOException

interface NYTimesService {
    fun getArtistInfo(artistName: String?): ArtistData
    fun getURLWithArtistName(artistName: String?):String
}

internal class NYTimesServiceImpl(
    private val nyTimesAPI: NYTimesAPI,
    private val nyTimesToArtistResolver: NYTimesToArtistResolver,
) : NYTimesService {

    override fun getArtistInfo(artistName: String?): ArtistData {
        var infoArtist: String? = null
        try {
            infoArtist = nyTimesToArtistResolver.generateFormattedResponse(getInfoFromAPI(artistName), artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return if(infoArtist == null){
            EmptyArtistData
        }
        else{
            val response = getInfoFromAPI(artistName)
            ArtistWithData(artistName, infoArtist, nyTimesToArtistResolver.getURL(response), false)
        }
    }

    override fun getURLWithArtistName(artistName: String?): String{
        val response = getInfoFromAPI(artistName)
        return nyTimesToArtistResolver.getURL(response)
    }

    private fun getInfoFromAPI(artistName: String?) = nyTimesAPI.getArtistInfo(artistName).execute()

}