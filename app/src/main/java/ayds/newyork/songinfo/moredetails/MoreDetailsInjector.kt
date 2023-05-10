package ayds.newyork.songinfo.moredetails

import android.content.Context
import ayds.newyork.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.NYTimesService
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.*
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.NYTimesServiceImpl
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.NYTimesToArtistResolverImpl
import ayds.newyork.songinfo.moredetails.data.repository.local.ArtistLocalStorage
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.CursorToArtistDataMapperImpl
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.RepositoryToViewFormatter
import ayds.newyork.songinfo.moredetails.presentation.presenter.RepositoryToViewFormatterImpl
import ayds.newyork.songinfo.moredetails.presentation.view.MoreDetailsView
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val LINK_API_NYTIMES = "https://api.nytimes.com/svc/search/v2/"

object MoreDetailsInjector {
    private lateinit var artistRepository : ArtistRepository
    lateinit var presenter : MoreDetailsPresenter

    fun init(moreDetailsView: MoreDetailsView){
        initRepository(moreDetailsView)
        initPresenter()
        moreDetailsView.setPresenter(presenter)
    }

    private fun initRepository(moreDetailsView: MoreDetailsView){
        val artistStorage: ArtistLocalStorage = ArtistLocalStorageImpl(moreDetailsView as Context, CursorToArtistDataMapperImpl())
        val nyTimesService: NYTimesService = initNYTimesService()
        this.artistRepository = ArtistRepositoryImpl(artistStorage, nyTimesService)
    }

    private fun initNYTimesService(): NYTimesService {
        val nyTimesAPIRetrofit = Retrofit.Builder()
            .baseUrl(LINK_API_NYTIMES)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val nyTimesAPI = nyTimesAPIRetrofit.create(NYTimesAPI::class.java)
        return NYTimesServiceImpl(nyTimesAPI, NYTimesToArtistResolverImpl())
    }

    private fun initPresenter(){
        val format : RepositoryToViewFormatter = RepositoryToViewFormatterImpl()
        this.presenter = MoreDetailsPresenterImpl(artistRepository, format)
    }

    /*fun setPresentation(moreDetailsPresentation: MoreDetailsPresentation){
        this.moreDetailsPresentation = moreDetailsPresentation
    }

    fun initMoreDetailsDomain(){
        val artistLocalStorage: ArtistLocalStorage = ArtistLocalStorageImpl(moreDetailsPresentation as Context, CursorToArtistDataMapperImpl())
        val nyTimesService: NYTimesService = initNYTimesService()
        artistRepository = ArtistRepositoryImpl(artistLocalStorage, nyTimesService)
        moreDetailsDomain = MoreDetailsDomainImpl(artistRepository)
    }*/
}