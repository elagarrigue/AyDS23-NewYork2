package ayds.newyork.songinfo.moredetails.data.repository
import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.aknewyork.external.service.data.entities.NYT_LOGO_URL
import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.newyork.songinfo.moredetails.domain.entities.Source


interface Proxy {
    fun getCard(artistName: String): ArtistCard?
}

internal class ProxyNYTimesImpl(private val nyTimesService : NYTimesService) : Proxy {

    override fun getCard(artistName: String): ArtistCard? {
        val nyInfo = nyTimesService.getArtistInfo(artistName)
        return adaptNYTimesInfoToCard(nyInfo)
    }

    private fun adaptNYTimesInfoToCard(nyTimesInfo: ArtistDataExternal): ArtistCard? {
        return if(nyTimesInfo is ArtistDataExternal.ArtistWithDataExternal) {
            ArtistCard(nyTimesInfo.info, nyTimesInfo.url, Source.NYTimes, NYT_LOGO_URL, false)
        } else {
            null
        }
    }

}