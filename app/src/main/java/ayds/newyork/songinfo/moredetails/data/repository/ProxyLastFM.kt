package ayds.newyork.songinfo.moredetails.data.repository
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source

const val LAST_FM_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

interface ProxyLastFM {
    fun getCard(artistName: String): Card
}

internal class ProxyLastFMImpl(lastFMService : LastFMService) : ProxyLastFM {

    override fun getCard(artistName: String): Card {
        val lastInfo = lastFMService.getArtistInfo(artistName)
        return adaptLastFMInfoToCard(lastInfo)
    }

    private fun adaptLastFMInfoToCard(lastFMInfo: LastFmArtistInfo?): Card {
        return if(lastFMInfo == null) {
            Card(null, null, null, Source.LastFm, LAST_FM_DEFAULT_IMAGE, false)
        } else {
            Card(lastFMInfo.name, lastFMInfo.info, lastFMInfo.url, Source.LastFm, LAST_FM_DEFAULT_IMAGE, false)
        }
    }

}