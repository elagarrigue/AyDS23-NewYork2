package ayds.newyork.songinfo.moredetails

import android.content.Context
import ayds.newyork.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorage
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.CursorToArtistDataMapperImpl
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.RepositoryToViewFormatter
import ayds.newyork.songinfo.moredetails.presentation.presenter.RepositoryToViewFormatterImpl
import ayds.newyork.songinfo.moredetails.presentation.view.MoreDetailsView
import ayds.aknewyork.external.service.injector.NYTimesInjector

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
        this.artistRepository = ArtistRepositoryImpl(artistStorage, NYTimesInjector.nyTimesService)
    }

    private fun initPresenter(){
        val format : RepositoryToViewFormatter = RepositoryToViewFormatterImpl()
        this.presenter = MoreDetailsPresenterImpl(artistRepository, format)
    }
}