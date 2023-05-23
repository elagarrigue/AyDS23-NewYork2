package ayds.newyork.songinfo.moredetails.data.repository

import ayds.aknewyork.external.service.data.entities.ArtistDataExternal

interface Broker {
    fun getArtistInfoFromNYTimes(artistName: String): ArtistDataExternal
//    fun getArtistInfoFromServiceB(artistName: String): ArtistDataExternal
//    fun getArtistInfoFromServiceC(artistName: String): ArtistDataExternal
}

internal class BrokerImpl(
    private val proxyNYTimes: ProxyNYTimes
    //private val proxyServiceB: NYTimesService
    //private val proxyServiceC: NYTimesService
) : Broker {

    override fun getArtistInfoFromNYTimes(artistName: String): ArtistDataExternal {
        return proxyNYTimes.getArtistInfo(artistName);
    }

//    override fun getArtistInfoFromServiceB(artistName: String): ArtistDataExternal {
//        return proxyServiceB.getArtistInfo(artistName);
//    }
//
//    override fun getArtistInfoFromServiceC(artistName: String): ArtistDataExternal {
//        return proxyServiceC.getArtistInfo(artistName);
//    }
}
