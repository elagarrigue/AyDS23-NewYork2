package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.service

import ayds.newyork.songinfo.moredetails.model.entities.ArtistData
import ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.NYTimesAPI
import com.google.gson.JsonObject

interface NYTimesService {

    //similar a SpotifyTrackService
    fun getArtistInfo(artistName: String?): ArtistData
}