package ayds.newyork.songinfo.moredetails.data.repository
import ayds.lisboa3.submodule.lastFm.LastFmArtistInfo
import ayds.lisboa3.submodule.lastFm.LastFmService
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source

internal class ProxyLastFMImpl(private val lastFmService : LastFmService) : Proxy {

    override fun getCard(artistName: String): Card {
        val lastInfo = lastFmService.getArtistInfo(artistName)
        return adaptLastFMInfoToCard(lastInfo)
    }

    private fun adaptLastFMInfoToCard(lastFMInfo: LastFmArtistInfo?): Card {
        return if(lastFMInfo == null) {
            Card.EmptyCard
        } else {
            Card.ArtistCard("", lastFMInfo.bioContent, lastFMInfo.url, Source.LastFm, lastFMInfo.logo, false)
        }
    }

}