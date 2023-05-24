package ayds.newyork.songinfo.moredetails.data.repository
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo

const val WIKIPEDIA_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Wikipedia-logo-v2-es.png"

interface ProxyWikipedia {
    fun getCard(artistName: String): Card
}

internal class ProxyWikipediaImpl(private val wikipediaTrackService : WikipediaTrackService) : ProxyWikipedia {

    override fun getCard(artistName: String): Card {
        val wikipediaInfo = wikipediaTrackService.getInfo(artistName)
        return adaptWikipediaInfoToCard(wikipediaInfo)
    }

    private fun adaptWikipediaInfoToCard(wikipediaInfo: ArtistInfo?): Card {
        return if(wikipediaInfo == null) {
            Card(null, null, null, Source.Wikipedia, WIKIPEDIA_DEFAULT_IMAGE, false)
        } else {
            Card("",wikipediaInfo.description, wikipediaInfo.wikipediaURL, Source.Wikipedia, WIKIPEDIA_DEFAULT_IMAGE, false)
        }
    }

}