package ayds.newyork.songinfo.moredetails.domain.repository

import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface ArtistRepository {

    fun getArtistData(artistName: String): List<Card>
}
