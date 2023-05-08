package ayds.newyork.songinfo.moredetails

import android.content.Context
import ayds.newyork.songinfo.moredetails.data.MoreDetailsData
import ayds.newyork.songinfo.moredetails.data.MoreDetailsDataImpl
import ayds.newyork.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.NYTimesService
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.JsonToArtistResolver
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.NYTimesAPI
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.NYTimesServiceImpl
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.NYTimesToArtistResolver
import ayds.newyork.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.CursorToArtistDataMapperImpl
import ayds.newyork.songinfo.moredetails.domain.MoreDetailsDomain
import ayds.newyork.songinfo.moredetails.domain.MoreDetailsDomainImpl
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.MoreDetailsPresentation
import ayds.newyork.songinfo.moredetails.presentation.MoreDetailsPresentationImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LINK_API_NYTIMES = "https://api.nytimes.com/svc/search/v2/"

object MoreDetailsInjector {

    private lateinit var moreDetailsData: MoreDetailsData
    private lateinit var moreDetailsDomain: MoreDetailsDomain
    private lateinit var moreDetailsPresentation: MoreDetailsPresentation

    fun initMoreDetailsData(){
        moreDetailsData = MoreDetailsDataImpl()
        moreDetailsData.setMoreDetailsDomain(moreDetailsDomain)
    }

    fun initMoreDetailsDomain(){
        val artistLocalStorage: ArtistLocalStorage = ArtistLocalStorageImpl(moreDetailsPresentation as Context, CursorToArtistDataMapperImpl())
        val nyTimesService: NYTimesService = initNYTimesService()
        val artistRepository: ArtistRepository = ArtistRepositoryImpl(artistLocalStorage, nyTimesService)
        moreDetailsDomain = MoreDetailsDomainImpl(artistRepository)
    }

    private fun initNYTimesService(): NYTimesService {
        val nyTimesAPIRetrofit = Retrofit.Builder()
            .baseUrl(LINK_API_NYTIMES)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val nyTimesAPI = nyTimesAPIRetrofit.create(NYTimesAPI::class.java)
        val nyTimesToArtistResolver: NYTimesToArtistResolver = JsonToArtistResolver()
        return NYTimesServiceImpl(nyTimesAPI, nyTimesToArtistResolver)
    }

    fun initMoreDetailsPresentation(){
        moreDetailsPresentation = MoreDetailsPresentationImpl()
        moreDetailsPresentation.setMoreDetailsInjector(this)
        moreDetailsPresentation.setMoreDetailsDomain(moreDetailsDomain)
    }
}