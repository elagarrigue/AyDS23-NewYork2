package ayds.newyork.songinfo.moredetails.data.repository
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.injector.NYTimesInjector.nyTimesService

interface ProxyWikipedia {
    fun getArtistInfo(artistName: String) : ArtistDataExternal
}

internal class ProxyWikipediaImpl(wikipediaService : WikipediaService) : ProxyWikipedia {
    override fun getArtistInfo(artistName: String) : ArtistInfo {
        return wikipediaService.getInfo(artistName);
    }
}