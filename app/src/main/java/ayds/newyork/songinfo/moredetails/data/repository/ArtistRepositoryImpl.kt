package ayds.newyork.songinfo.moredetails.data.repository

import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.NYTimesService
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.NYTimesToArtistResolver
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.ArtistWithData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository

internal class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val nyTimesService: NYTimesService,
    private val nyTimesToArtistResolver: NYTimesToArtistResolver
): ArtistRepository {

    override fun getArtistData(artistName: String): ArtistData? {
        var artistData = getArtistInfoFromDatabase(artistName)

        when {
            artistData != null -> markArtistAsLocal(artistData)
            else -> {
                try {
                    artistData = nyTimesService.getArtistInfo(artistName)
                    artistData.let {
                        if(artistData is ArtistWithData)
                            artistLocalStorage.saveArtist(artistName, artistData.info!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return artistData
    }

    private fun getArtistInfoFromDatabase(artistName: String): ArtistData? {
        val infoArtist: String? = artistLocalStorage.getInfo(artistName)
        return if(infoArtist == null)
            null
        else {
            val url = nyTimesToArtistResolver.getURL(artistName)
            ArtistWithData(artistName, infoArtist, url, true)
        }
    }

    private fun markArtistAsLocal(artistData: ArtistData){
        if(artistData is ArtistWithData)
            artistData.isInDatabase = true
    }
}