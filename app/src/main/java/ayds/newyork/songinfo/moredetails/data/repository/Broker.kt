package ayds.newyork.songinfo.moredetails.data.repository

import ayds.aknewyork.external.service.data.entities.ArtistDataExternal

interface Broker {
    fun getArtistInfoFromNYTimes(artistName: String): ArtistDataExternal
    fun getArtistInfoFromLastFM(artistName: String): ArtistDataExternal
    fun getArtistInfoFromWikipedia(artistName: String): ArtistDataExternal
}

internal class BrokerImpl(
    private val proxyNYTimes: ProxyNYTimes,
    private val proxyLastFM: ProxyLastFM,
    private val proxyWikipedia: ProxyWikipedia
) : Broker {

    override fun getArtistInfoFromNYTimes(artistName: String): ArtistDataExternal {
        return proxyNYTimes.getArtistInfo(artistName);
    }

    override fun getArtistInfoFromLastFM(artistName: String): ArtistDataExternal {
        return proxyLastFM.getArtistInfo(artistName);
    }

    override fun getArtistInfoFromWikipedia(artistName: String): ArtistDataExternal {
        return proxyWikipedia.getArtistInfo(artistName);
    }
}
