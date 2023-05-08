package ayds.newyork.songinfo.moredetails.data.repository.external.nytimes

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData

interface NYTimesService {

    //similar a SpotifyTrackService
    fun getArtistInfo(artistName: String?): ArtistData
    fun getURL(artistName: String?): String
    fun generateFormattedResponse(nameArtist: String?): String
}