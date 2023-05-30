package ayds.newyork.songinfo.moredetails.data.repository

import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface Broker {
    fun getCards(artistName: String): List<Card>
}

internal class BrokerImpl(private val proxies: List<Proxy>) : Broker {

    override fun getCards(artistName: String): List<Card> {
        val cards: MutableList<Card> = mutableListOf()

        for (proxy in proxies) {
            cards.add(proxy.getCard(artistName))
        }

        return cards
    }
}
