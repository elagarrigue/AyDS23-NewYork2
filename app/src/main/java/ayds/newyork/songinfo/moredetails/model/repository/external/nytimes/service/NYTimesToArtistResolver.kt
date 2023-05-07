package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.service

import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.NYTimesAPI

//similar a SpotifyToSongResolver
interface NYTimesToArtistResolver {
    fun getArtistInfoFromAPI(artistName: String?, nyTimesAPI: NYTimesAPI): Song.SpotifySong?
    fun getURL(artistName: String?, nyTimesAPI: NYTimesAPI): String
    fun generateFormattedResponse(api: NYTimesAPI, nameArtist: String?): String
}

internal class JsonToArtistResolver : NYTimesToArtistResolver{

    override fun getArtistInfoFromAPI(artistName: String?, nyTimesAPI: NYTimesAPI): Song.SpotifySong? {
        TODO("Not yet implemented")
    }

    override fun getURL(artistName: String?, nyTimesAPI: NYTimesAPI): String {
        TODO("Not yet implemented")
    }

    override fun generateFormattedResponse(api: NYTimesAPI, nameArtist: String?): String {
        TODO("Not yet implemented")
    }
}