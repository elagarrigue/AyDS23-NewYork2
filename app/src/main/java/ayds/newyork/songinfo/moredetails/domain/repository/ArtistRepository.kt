package ayds.newyork.songinfo.moredetails.domain.repository

import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard

interface ArtistRepository {

    fun getArtistData(artistName: String): List<ArtistCard>
}
