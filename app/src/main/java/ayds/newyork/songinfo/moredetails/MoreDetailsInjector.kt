package ayds.newyork.songinfo.moredetails

import android.content.Context
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorage
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.CursorToArtistDataMapperImpl
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.presenter.ArtistCardFormatter
import ayds.newyork.songinfo.moredetails.presentation.presenter.ArtistCardFormatterImpl
import ayds.newyork.songinfo.moredetails.presentation.view.MoreDetailsView
import ayds.aknewyork.external.service.injector.NYTimesInjector
import ayds.newyork.songinfo.moredetails.data.repository.*
import ayds.newyork.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.repository.external.BrokerImpl
import ayds.newyork.songinfo.moredetails.data.repository.ProxyNYTimesImpl
import ayds.winchester2.wikipediaexternal.injector.WikipediaInjector
import ayds.lisboa3.submodule.lastFm.LastFmInjector
import ayds.newyork.songinfo.moredetails.data.repository.external.Broker

object MoreDetailsInjector {
    private lateinit var artistRepository: ArtistRepository
    lateinit var presenter: MoreDetailsPresenter

    fun init(moreDetailsView: MoreDetailsView) {
        initRepository(moreDetailsView)
        initPresenter()
        moreDetailsView.setPresenter(presenter)
    }

    private fun initRepository(moreDetailsView: MoreDetailsView) {
        val artistStorage: ArtistLocalStorage =
            ArtistLocalStorageImpl(moreDetailsView as Context, CursorToArtistDataMapperImpl())
        val proxyNYTimes: Proxy = ProxyNYTimesImpl(NYTimesInjector.nyTimesService)
        val proxyLastFM: Proxy = ProxyLastFMImpl(LastFmInjector.getService())
        val proxyWikipedia: Proxy =
            ProxyWikipediaImpl(WikipediaInjector.wikipediaTrackService)

        val proxyList: MutableList<Proxy> = mutableListOf()
        proxyList.add(proxyNYTimes)
        proxyList.add(proxyLastFM)
        proxyList.add(proxyWikipedia)

        val broker: Broker = BrokerImpl(proxyList)
        this.artistRepository = ArtistRepositoryImpl(artistStorage, broker)
    }

    private fun initPresenter() {
        val format: ArtistCardFormatter = ArtistCardFormatterImpl()
        this.presenter = MoreDetailsPresenterImpl(artistRepository, format)
    }
}