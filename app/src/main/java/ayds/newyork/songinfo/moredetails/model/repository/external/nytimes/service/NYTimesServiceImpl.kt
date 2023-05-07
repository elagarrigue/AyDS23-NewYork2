package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.service

import ayds.newyork.songinfo.moredetails.model.entities.ArtistData
import ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.NYTimesAPI
import java.io.IOException

//similar a SpotifyTrackServiceImpl
internal class NYTimesServiceImpl(
    private val nyTimesAPI: NYTimesAPI,
    private val nyTimesToArtistResolver: NYTimesToArtistResolver,
) : NYTimesService {
    override fun getArtistInfo(artistName: String?): ArtistData {
        var infoArtist: String? = null
        try {
            infoArtist = nyTimesToArtistResolver.generateFormattedResponse(nyTimesAPI, artistName)
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        val url = if (infoArtist == null) "" else nyTimesToArtistResolver.getURL(artistName, nyTimesAPI)
        return ArtistData.ArtistWithData(infoArtist, url, false)
    }
}