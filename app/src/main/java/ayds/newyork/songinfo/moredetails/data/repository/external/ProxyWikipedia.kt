package ayds.newyork.songinfo.moredetails.data.repository

import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.newyork.songinfo.moredetails.domain.entities.Source
import ayds.winchester2.wikipediaexternal.data.wikipedia.WikipediaTrackService
import ayds.winchester2.wikipediaexternal.data.wikipedia.entity.ArtistInfo

internal class ProxyWikipediaImpl(private val wikipediaTrackService : WikipediaTrackService) : Proxy {

    override fun getCard(artistName: String): ArtistCard? {
        val wikipediaInfo = wikipediaTrackService.getInfo(artistName)
        return adaptWikipediaInfoToCard(wikipediaInfo)
    }


    private fun adaptWikipediaInfoToCard(wikipediaInfo: ArtistInfo?): ArtistCard? {
        return if(wikipediaInfo == null) {
            null
        } else {
            ArtistCard(wikipediaInfo.description, wikipediaInfo.wikipediaURL, Source.Wikipedia, wikipediaInfo.wikipediaLogoURL, false)
        }
    }

}