package ayds.newyork.songinfo.moredetails.data.repository
import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.aknewyork.external.service.data.entities.NYT_LOGO_URL
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source


interface ProxyNYTimes {
    fun getCard(artistName: String): Card
}

internal class ProxyNYTimesImpl(private val nyTimesService : NYTimesService) : ProxyNYTimes {

    override fun getCard(artistName: String): Card {
        val nyInfo = nyTimesService.getArtistInfo(artistName)
        return adaptNYTimesInfoToCard(nyInfo)
    }

    private fun adaptNYTimesInfoToCard(nyTimesInfo: ArtistDataExternal): Card {
        return if(nyTimesInfo is ArtistDataExternal.ArtistWithDataExternal) {
            Card.ArtistCard(nyTimesInfo.name, nyTimesInfo.info, nyTimesInfo.url, Source.NYTimes, NYT_LOGO_URL, false)
        } else {
            Card.EmptyCard
        }
    }

}