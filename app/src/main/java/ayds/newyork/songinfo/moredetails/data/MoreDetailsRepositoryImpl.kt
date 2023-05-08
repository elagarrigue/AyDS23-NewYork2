package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository

interface MoreDetailsData {
    fun searchArtistData(artistName: String): ArtistData?
}
internal class MoreDetailsDataImpl(private val repository: ArtistRepository) : MoreDetailsData {

    override fun searchArtistData(artistName: String): ArtistData? {
        return repository.getArtistData(artistName)
    }
}