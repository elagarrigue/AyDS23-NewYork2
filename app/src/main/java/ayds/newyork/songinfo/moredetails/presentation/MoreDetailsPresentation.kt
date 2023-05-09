package ayds.newyork.songinfo.moredetails.presentation

import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.moredetails.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.domain.MoreDetailsDomain
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository

interface MoreDetailsPresentation{
    fun initMoreDetailsDomain()
    fun initMoreDetailsView(moreDetailsView: MoreDetailsView)
    fun initPresenter()
    fun obtainArtistRepository(): ArtistRepository
}

internal class MoreDetailsPresentationImpl: AppCompatActivity(), MoreDetailsPresentation{
    lateinit var moreDetailsDomain: MoreDetailsDomain
    lateinit var moreDetailsPresenter: MoreDetailsPresenter
    private lateinit var moreDetailsView: MoreDetailsView
    private lateinit var repository: ArtistRepository
    override fun initMoreDetailsDomain() {
        setPresentation()
        MoreDetailsInjector.initMoreDetailsDomain()
        this.moreDetailsDomain = MoreDetailsInjector.moreDetailsDomain
    }

    private fun setPresentation(){
        MoreDetailsInjector.setPresentation(this)
    }

    override fun initMoreDetailsView(moreDetailsView: MoreDetailsView) {
        this.moreDetailsView = moreDetailsView
    }

    override fun initPresenter() {
        moreDetailsPresenter = MoreDetailsPresenterImpl(moreDetailsView, this)
    }

    override fun obtainArtistRepository() = MoreDetailsInjector.artistRepository

}