package ayds.newyork.songinfo.moredetails.data.repository

import ayds.aknewyork.external.service.data.entities.ArtistDataExternal
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.EmptyArtistData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.ArtistWithData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.aknewyork.external.service.data.NYTimesService
import ayds.aknewyork.external.service.data.entities.ArtistDataExternal.ArtistWithDataExternal
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorage

internal class ArtistRepositoryImpl(
    private val artistLocalStorage: ArtistLocalStorage,
    private val nyTimesService: NYTimesService
): ArtistRepository {

    override fun getArtistData(artistName: String): ArtistData {
        var artistData = artistLocalStorage.getArtist(artistName)

        when {
            artistData != EmptyArtistData -> markArtistAsLocal(artistData)
            else -> {
                try {
                    val artistDataExternal = nyTimesService.getArtistInfo(artistName)
                    artistData = adaptArtistData(artistDataExternal)
                    artistData.let {
                        if(artistData is ArtistWithData)
                            artistLocalStorage.saveArtist(artistData)
                    }
                } catch (e: Exception) {
                    null
                }
            }
        }
        return artistData
    }

    private fun markArtistAsLocal(artistData: ArtistData){
        if(artistData is ArtistWithData)
            artistData.isInDatabase = true
    }

    private fun adaptArtistData(artist: ArtistDataExternal): ArtistData{
        return when(artist){
            is ArtistWithDataExternal -> {
                ArtistWithData(
                    artist.name,
                    artist.info,
                    artist.url,
                    artist.isInDatabase
                )
            }
            else ->
                EmptyArtistData
        }
    }
}