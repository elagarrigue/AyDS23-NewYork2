package ayds.newyork.songinfo.moredetails.domain

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository

interface MoreDetailsDomain{
    fun searchArtistData(artistName: String): ArtistData?
}

internal class MoreDetailsDomainImpl(private val repository: ArtistRepository): MoreDetailsDomain{
    override fun searchArtistData(artistName: String): ArtistData? {
        return repository.getArtistData(artistName)
    }
}