package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.observer.Subject
import ayds.observer.Observable

interface MoreDetailsPresenter {

    val uiStateObservable: Observable<MoreDetailsUIState>
    val onUIStateSubject: Subject<MoreDetailsUIState>
    var uiState: MoreDetailsUIState
    fun openArtistInfoWindow(artistName: String)
}

internal class MoreDetailsPresenterImpl(
    private val repository: ArtistRepository,
    private val formatter: ArtistCardFormatter
) : MoreDetailsPresenter {
    override val onUIStateSubject = Subject<MoreDetailsUIState>()
    override val uiStateObservable = onUIStateSubject
    override var uiState: MoreDetailsUIState = MoreDetailsUIState()

    override fun openArtistInfoWindow(artistName: String) {
        Thread {
            loadArtistInfo(artistName)
        }.start()
    }

    private fun loadArtistInfo(artistName: String) {
        val artistData = repository.getArtistData(artistName)
        uiState.artistCards = artistData.map { artist ->
            ArtistCard(
                description = formatter.format(artist, artistName),
                infoUrl = artist.infoUrl,
                source = artist.source,
                sourceLogoUrl = artist.sourceLogoUrl,
                isInDatabase = artist.isInDatabase
            )
        }
        uiStateObservable.notify(uiState)
    }
}