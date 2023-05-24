package ayds.newyork.songinfo.moredetails.data.repository

import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.newyork.songinfo.moredetails.domain.entities.Card

interface Broker {
    fun getCards(artistName: String):List<Card>
}

internal class BrokerImpl(
    private val proxyNYTimes: ProxyNYTimes,
    private val proxyLastFM: ProxyLastFM,
    private val proxyWikipedia: ProxyWikipedia
) : Broker {

    override fun getCards(artistName: String): List<Card> {
        val cards: MutableList<Card> = mutableListOf()

        cards.add(proxyNYTimes.getCard(artistName))
        cards.add(proxyLastFM.getCard(artistName))
        cards.add(proxyWikipedia.getCard(artistName))

        return cards
    }
}
