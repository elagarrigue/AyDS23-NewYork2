package ayds.newyork.songinfo.moredetails.data.repository

import ayds.newyork.songinfo.moredetails.domain.entities.Card.EmptyCard
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorage

internal class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val broker: Broker
): ArtistRepository {

    override fun getArtistData(artistName: String): List<Card> { //Debe retornar list of Card

        //Refactorizar esto, usando list of Card
        var artistData = artistLocalStorage.getArtist(artistName)

        when {
            artistData != EmptyCard -> markArtistAsLocal(artistData)
            else -> {
                try {
                    val artistDataExternal = broker.getArtistInfoFromNYTimes(artistName);
                    artistData = adaptArtistData(artistDataExternal)
                    artistData.let {
                        if(artistData is ArtistCard)
                            artistLocalStorage.saveArtist(artistData)
                    }
                } catch (e: Exception) {
                    null
                }
            }
        }
        return artistData
    }

    private fun markArtistCardsAsLocal(artistCards: List<Card>) {
        artistCards.forEach { it.isInDatabase = true}
    }

    //No es necesario, se encarga el proxy
//    private fun adaptArtistData(artist: ArtistDataExternal): Card{
//        return when(artist){
//            is ArtistWithDataExternal -> {
//                ArtistCard(
//                    artist.name,
//                    artist.info,
//                    artist.url
//                )
//            }
//            else ->
//                EmptyCard
//        }
//    }
}