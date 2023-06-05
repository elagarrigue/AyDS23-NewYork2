package ayds.newyork.songinfo.moredetails.data.repository
import ayds.lisboa3.submodule.lastFm.LastFmArtistInfo
import ayds.lisboa3.submodule.lastFm.LastFmService
import ayds.newyork.songinfo.moredetails.data.repository.external.DESCRIPTION_ERROR
import ayds.newyork.songinfo.moredetails.data.repository.external.IMG_ERROR
import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.newyork.songinfo.moredetails.domain.entities.Source

internal class ProxyLastFMImpl(private val lastFmService : LastFmService) : Proxy {
    override fun getCard(artistName: String): ArtistCard? {
        val lastInfo = lastFmService.getArtistInfo(artistName)
        return adaptLastFMInfoToCard(lastInfo)
    }

    private fun adaptLastFMInfoToCard(lastFMInfo: LastFmArtistInfo?): ArtistCard? {
        return if(lastFMInfo == null) {
            null
        } else {
            ArtistCard(lastFMInfo.bioContent, lastFMInfo.url, Source.LastFm, lastFMInfo.logo, false)
        }
    }

}