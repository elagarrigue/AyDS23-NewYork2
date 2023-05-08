package ayds.newyork.songinfo.moredetails.presentation

import ayds.newyork.songinfo.moredetails.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.domain.MoreDetailsDomain

interface MoreDetailsPresentation{
    fun setMoreDetailsDomain(moreDetailsDomain: MoreDetailsDomain)
    fun setMoreDetailsInjector(moreDetailsInjector: MoreDetailsInjector)
}

internal class MoreDetailsPresentationImpl: MoreDetailsPresentation{
    private lateinit var moreDetailsInjector: MoreDetailsInjector
    private lateinit var moreDetailsDomain: MoreDetailsDomain
    override fun setMoreDetailsDomain(moreDetailsDomain: MoreDetailsDomain) {
        this.moreDetailsDomain = moreDetailsDomain
    }

    override fun setMoreDetailsInjector(moreDetailsInjector: MoreDetailsInjector) {
        this.moreDetailsInjector = moreDetailsInjector
    }

}