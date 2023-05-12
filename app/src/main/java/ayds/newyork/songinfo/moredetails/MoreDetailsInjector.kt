package ayds.newyork.songinfo.moredetails

import android.content.Context
import ayds.newyork.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.*
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.NYTimesServiceImpl
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.NYTimesToArtistResolverImpl
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorage
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
    private lateinit var nyTimesToArtistResolver: NYTimesToArtistResolver
    private lateinit var nyTimesService: NYTimesService

    fun init(moreDetailsView: MoreDetailsView){
        initRepository(moreDetailsView)
        initPresenter()
        moreDetailsView.setPresenter(presenter)
    }

    private fun initRepository(moreDetailsView: MoreDetailsView){
        val artistStorage: ArtistLocalStorage = ArtistLocalStorageImpl(moreDetailsView as Context, CursorToArtistDataMapperImpl())
        val nyTimesService: NYTimesService = initNYTimesService()
        setNYTimesServiceInResolver()
        this.artistRepository = ArtistRepositoryImpl(artistStorage, nyTimesService, nyTimesToArtistResolver)
    }

    private fun initNYTimesService(): NYTimesService {
        val nyTimesAPIRetrofit = Retrofit.Builder()
            .baseUrl(LINK_API_NYTIMES)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val nyTimesAPI = nyTimesAPIRetrofit.create(NYTimesAPI::class.java)
        nyTimesToArtistResolver = NYTimesToArtistResolverImpl()
        nyTimesService = NYTimesServiceImpl(nyTimesAPI, nyTimesToArtistResolver)
        return nyTimesService
    }

    private fun setNYTimesServiceInResolver(){
        nyTimesToArtistResolver.setNYTimesService(nyTimesService)
    }

    private fun initPresenter(){
        val format : RepositoryToViewFormatter = RepositoryToViewFormatterImpl()
        this.presenter = MoreDetailsPresenterImpl(artistRepository, format)
    }
}