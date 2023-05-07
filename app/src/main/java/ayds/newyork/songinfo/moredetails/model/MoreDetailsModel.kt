package ayds.newyork.songinfo.moredetails.model

import ayds.newyork.songinfo.moredetails.model.entities.ArtistData
import ayds.newyork.songinfo.moredetails.model.repository.ArtistRepository

interface  MoreDetailsModel {

    fun searchArtistData(term: String): ArtistData?
}

internal class MoreDetailsModelImpl(private val repository: ArtistRepository) : MoreDetailsModel {

    override fun searchArtistData(artistName: String): ArtistData? {
        return repository.getArtistData(artistName)
    }
}