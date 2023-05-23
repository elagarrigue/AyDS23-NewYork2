package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.ArtistWithData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.MoreDetailsUIState
import ayds.observer.Subject
import ayds.observer.Observable

interface MoreDetailsPresenter {

    val uiStateObservable: Observable<MoreDetailsUIState>
    val onUIStateSubject: Subject<MoreDetailsUIState>
    var uiState: MoreDetailsUIState
    fun openArtistInfoWindow(artistName:String)
}

internal class MoreDetailsPresenterImpl(private val repository: ArtistRepository, private val formatter: RepositoryToViewFormatter) : MoreDetailsPresenter {
    override val onUIStateSubject = Subject<MoreDetailsUIState>()
    override val  uiStateObservable = onUIStateSubject
    override var uiState = MoreDetailsUIState()

    override fun openArtistInfoWindow(artistName:String) {
        Thread {
            loadArtistInfo(artistName)
        }.start()
    }

    private fun loadArtistInfo(artistName:String) {
        val artistData = repository.getArtistData(artistName)
        updateUIState(artistData)
        uiStateObservable.notify(uiState)
    }

    private fun updateUIState(artist: ArtistData){
        when(artist){
            is ArtistWithData -> {
                uiState = uiState.copy(formatter.format(artist), artist.url)
            }
        }
    }
}