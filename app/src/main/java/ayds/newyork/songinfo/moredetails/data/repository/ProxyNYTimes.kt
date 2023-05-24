package ayds.newyork.songinfo.moredetails.data.repository
import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source

const val NY_TIMES_DEFAULT_IMAGE = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

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
            Card(nyTimesInfo.name, nyTimesInfo.info, nyTimesInfo.url, Source.NYTimes, NY_TIMES_DEFAULT_IMAGE, false)
        } else {
            Card(null, null, null, Source.NYTimes, NY_TIMES_DEFAULT_IMAGE, false)
        }
    }

}