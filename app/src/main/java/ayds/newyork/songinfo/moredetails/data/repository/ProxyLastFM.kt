package ayds.newyork.songinfo.moredetails.data.repository
import ayds.lisboa3.submodule.lastFm.LastFmArtistInfo
import ayds.lisboa3.submodule.lastFm.LastFmService
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source

internal class ProxyLastFMImpl(private val lastFmService : LastFmService) : Proxy {
    private lateinit var artistName: String
    override fun getCard(artistName: String): Card {
        val lastInfo = lastFmService.getArtistInfo(artistName)
        this.artistName = artistName
        return adaptLastFMInfoToCard(lastInfo)
    }

    private fun adaptLastFMInfoToCard(lastFMInfo: LastFmArtistInfo?): Card {
        return if(lastFMInfo == null) {
            Card.EmptyCard
        } else {
            Card.ArtistCard(artistName, lastFMInfo.bioContent, lastFMInfo.url, Source.LastFm, lastFMInfo.logo, false)
        }
    }

}