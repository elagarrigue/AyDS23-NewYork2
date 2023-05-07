package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.ArtistData

interface NYTimesService {

    //similar a SpotifyTrackService
    fun getArtistInfo(artistName: String?): ArtistData
    fun getURL(artistName: String?): String
    fun generateFormattedResponse(nameArtist: String?): String
}