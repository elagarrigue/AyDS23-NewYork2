package ayds.newyork.songinfo.moredetails.data.repository

import ayds.newyork.songinfo.moredetails.data.repository.external.Broker
import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorage

internal class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val broker: Broker
): ArtistRepository {

    override fun getArtistData(artistName: String): List<ArtistCard> {
        var artistData = artistLocalStorage.getArtist(artistName)

        when {
            artistData.hasAnyServiceAsSource() -> markArtistCardsAsLocal(artistData)
            else -> {
                try {
                    artistData = broker.getCards(artistName);
                    for (card in artistData) {
                        artistLocalStorage.saveArtist(artistName, card)
                    }
                } catch (e: Exception) {
                    null
                }
            }
        }

        return artistData
    }

    private fun markArtistCardsAsLocal(artistCards: List<ArtistCard>) {
        artistCards.forEach { it.isInDatabase = true}
    }

    private fun List<ArtistCard>.hasAnyServiceAsSource() = (this.isNotEmpty())

}