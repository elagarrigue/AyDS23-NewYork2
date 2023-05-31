package ayds.newyork.songinfo.moredetails.data.repository

import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.Source

internal class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val broker: Broker
): ArtistRepository {

    override fun getArtistData(artistName: String): List<Card> {
        var artistData = artistLocalStorage.getArtist(artistName)

        when {
            artistData.hasAnyServiceAsSource() -> markArtistCardsAsLocal(artistData)
            else -> {
                try {
                    artistData = broker.getCards(artistName);
                    for (card in artistData) {
                        if(card is ArtistCard){
                            artistLocalStorage.saveArtist(artistName, card)
                        }
                    }
                } catch (e: Exception) {
                    null
                }
            }
        }

        return artistData
    }

    private fun markArtistCardsAsLocal(artistCards: List<Card>) {
        artistCards.forEach { if(it is ArtistCard) it.isInDatabase = true}
    }

    private fun List<Card>.hasAnyServiceAsSource() = (this.isNotEmpty())

}