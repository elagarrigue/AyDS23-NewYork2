package ayds.newyork.songinfo.moredetails.data.repository

import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard

interface Broker {
    fun getCards(artistName: String): List<ArtistCard>
}

internal class BrokerImpl(private val proxies: List<Proxy>) : Broker {

    override fun getCards(artistName: String): List<ArtistCard> {
        val cards: MutableList<ArtistCard> = mutableListOf()

        for (proxy in proxies) {
            val card = proxy.getCard(artistName)
            if(card != null)
                cards.add(card)
        }

        return cards
    }
}
