package ayds.newyork.songinfo.moredetails.data.repository
import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.aknewyork.external.service.injector.NYTimesInjector.nyTimesService

interface ProxyNYTimes {
    fun getArtistInfo(artistName: String) : ArtistDataExternal
}

internal class ProxyNYTimesImpl(nyTimesService : NYTimesService) : ProxyNYTimes {
    override fun getArtistInfo(artistName: String) : ArtistDataExternal {
        return nyTimesService.getArtistInfo(artistName);
    }
}