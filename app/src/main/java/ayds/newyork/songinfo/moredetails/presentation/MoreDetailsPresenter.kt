package ayds.newyork.songinfo.moredetails.presentation

import ayds.newyork.songinfo.home.view.HomeUiEvent
import ayds.newyork.songinfo.moredetails.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.data.repository.*
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorageImpl
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.CursorToArtistDataMapper
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import java.util.*


interface MoreDetailsPresenter {
    fun obtainArtistRepository()
    fun loadArtistInfo()
    fun markArtistAsLocal(artistData: ArtistData)
    fun setArtistName(artistName: String?)
}

internal class MoreDetailsPresenterImpl(private val moreDetailsView: MoreDetailsView) : MoreDetailsPresenter {
    private lateinit var view : MoreDetailsView
    private lateinit var repository: ArtistRepository
    private var artistName: String? = null

    override fun obtainArtistRepository() {
        repository = MoreDetailsInjector.artistRepository
    }

    override fun loadArtistInfo() {
        Thread {
            var artistData: ArtistData? = null
            if(artistName != null)
                artistData = repository.getArtistData(artistName!!)
            if(artistData != null)
                view.setView(artistData)
        }.start()
    }

    override fun markArtistAsLocal(artistData: ArtistData){
        if(artistData is ArtistData.ArtistWithData)
            artistData.isInDatabase = true
    }

    override fun setArtistName(artistName: String?) {
        this.artistName = artistName
    }


}