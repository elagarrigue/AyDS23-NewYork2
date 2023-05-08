package ayds.newyork.songinfo.moredetails.data

import ayds.newyork.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.NYTimesToArtistResolver
import ayds.newyork.songinfo.moredetails.domain.MoreDetailsDomain
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData

interface MoreDetailsData{

    fun searchArtistData(artistName: String): ArtistData?
    fun setMoreDetailsDomain(moreDetailsDomain: MoreDetailsDomain)
}

internal class MoreDetailsDataImpl() : MoreDetailsData{

    private lateinit var moreDetailsDomain: MoreDetailsDomain

    override fun searchArtistData(artistName: String): ArtistData? = moreDetailsDomain.searchArtistData(artistName)

    override fun setMoreDetailsDomain(moreDetailsDomain: MoreDetailsDomain) {
        this.moreDetailsDomain = moreDetailsDomain
    }
}