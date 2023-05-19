package ayds.newyork.songinfo.moredetails.domain.repository

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData

interface ArtistRepository {

    fun getArtistData(artistName: String): ArtistData
}
