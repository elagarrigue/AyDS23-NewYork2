package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.MoreDetailsUIState
import ayds.observer.Subject
import ayds.observer.Observable

interface MoreDetailsPresenter {
    val uiStateObservable: Observable<MoreDetailsUIState>
    fun loadArtistInfo(artistName:String)
}

internal class MoreDetailsPresenterImpl(private val repository: ArtistRepository, private val formatter: RepositoryToViewFormatter) : MoreDetailsPresenter {
    private val onUIStateSubject = Subject<MoreDetailsUIState>()
    override val uiStateObservable:Observable<MoreDetailsUIState> = onUIStateSubject

    override fun loadArtistInfo(artistName:String) {
        val artistData = repository.getArtistData(artistName!!)
        val uiState = createUIState(artistData)
        onUIStateSubject.notify(uiState!!)
    }

    private fun createUIState(artist: ArtistData?): MoreDetailsUIState? {
        return if(artist is ArtistData.ArtistWithData){
            MoreDetailsUIState(formatter.format(artist), artist.url)
        }
        else
            null
    }
}