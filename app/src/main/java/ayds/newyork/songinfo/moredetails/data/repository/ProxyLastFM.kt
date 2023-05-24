package ayds.newyork.songinfo.moredetails.data.repository
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.injector.NYTimesInjector.nyTimesService

interface ProxyLastFM {
    fun getArtistInfo(artistName: String) : ArtistDataExternal
}

internal class ProxyLastFMImpl(lastFMService : LastFMService) : ProxyLastFM {
    override fun getArtistInfo(artistName: String) : LastFmArtistInfo {
        return lastFMService.getArtistInfo(artistName);
    }
}