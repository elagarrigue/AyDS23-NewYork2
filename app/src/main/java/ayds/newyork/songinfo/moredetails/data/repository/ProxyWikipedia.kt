package ayds.newyork.songinfo.moredetails.data.repository
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Source

const val WIKIPEDIA_DEFAULT_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"

interface ProxyWikipedia {
    fun getCard(artistName: String): Card
}

internal class ProxyWikipediaImpl(wikipediaTrackService : WikipediaTrackService) : ProxyWikipedia {

    override fun getCard(artistName: String): Card {
        val wikipediaInfo = wikipediaTrackService.getInfo(artistName)
        return adaptWikipediaInfoToCard(wikipediaInfo)
    }

    private fun adaptWikipediaInfoToCard(wikipediaInfo: ArtistInfo?): Card {
        return if(wikipediaInfo == null) {
            Card(null, null, null, Source.Wikipedia, WIKIPEDIA_DEFAULT_IMAGE, false)
        } else {
            Card(wikipediaInfo.name, wikipediaInfo.info, wikipediaInfo.url, Source.Wikipedia, WIKIPEDIA_DEFAULT_IMAGE, false)
        }
    }

}