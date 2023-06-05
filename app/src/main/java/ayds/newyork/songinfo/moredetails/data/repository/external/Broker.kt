package ayds.newyork.songinfo.moredetails.data.repository.external

import ayds.newyork.songinfo.moredetails.data.repository.Proxy
import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard

const val IMG_ERROR = "app/src/main/java/ayds/newyork/songinfo/moredetails/data/repository/local/sqldb/img/error.png"
const val DESCRIPTION_ERROR = "No se pudo obtener información"

interface Broker {
    fun getCards(artistName: String): List<ArtistCard>
}

internal class BrokerImpl(private val proxies: List<Proxy>) : Broker {

    override fun getCards(artistName: String): List<ArtistCard> {
        val cards: MutableList<ArtistCard> = mutableListOf()

        for (proxy in proxies) {
            var card = proxy.getCard(artistName)
            if(card != null)
                cards.add(card)
            else {
                card = proxy.getMessageCard()
                cards.add(card)
                break
            }
        }

        return cards
    }
}
