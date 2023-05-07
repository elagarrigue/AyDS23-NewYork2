package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes

import ayds.newyork.songinfo.moredetails.model.entities.ArtistData

interface NYTimesService {

    //similar a SpotifyTrackService
    fun getArtistInfo(artistName: String?): ArtistData
}