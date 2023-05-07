package ayds.newyork.songinfo.moredetails.model.repository

import ayds.newyork.songinfo.moredetails.model.entities.ArtistData
import ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.NYTimesService
import ayds.newyork.songinfo.moredetails.model.repository.local.ArtistLocalStorage

interface ArtistRepository {
    fun getArtistData(artistName: String): ArtistData?
}

internal class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val nyTimesService: NYTimesService
): ArtistRepository {

    override fun getArtistData(artistName: String): ArtistData? {
        var artistData = getArtistInfoFromDatabase(artistName)

        when {
            artistData != null -> markArtistAsLocal(artistData)
            else -> {
                try {
                    artistData = nyTimesService.getArtistInfo(artistName)
                    artistData.let {
                        if(artistData is ArtistData.ArtistWithData)
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
            val url = nyTimesService.getURL(artistName)
            ArtistData.ArtistWithData(infoArtist, url, true)
        }
    }

    private fun markArtistAsLocal(artistData: ArtistData){
        if(artistData is ArtistData.ArtistWithData)
            artistData.isInDatabase = true
    }
}