package ayds.newyork.songinfo.moredetails.data.repository

import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface Broker {
    fun getCards(artistName: String): List<Card>
}

internal class BrokerImpl(
    private val proxyNYTimes: ProxyNYTimes,
    private val proxyLastFM: ProxyLastFM,
    private val proxyWikipedia: ProxyWikipedia
) : Broker {

    override fun getCards(artistName: String): List<Card> {
        val cards: MutableList<Card> = mutableListOf()

        with(cards){
            add(proxyNYTimes.getCard(artistName))
            add(proxyLastFM.getCard(artistName))
            add(proxyWikipedia.getCard(artistName))
        }

        return cards
    }
}
