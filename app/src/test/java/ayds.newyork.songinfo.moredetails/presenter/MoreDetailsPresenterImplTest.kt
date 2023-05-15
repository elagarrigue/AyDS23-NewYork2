package ayds.newyork.songinfo.moredetails.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenterImpl
import ayds.newyork.songinfo.moredetails.presentation.MoreDetailsUIState
import ayds.newyork.songinfo.moredetails.presentation.presenter.RepositoryToViewFormatter
import ayds.observer.Observer
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MoreDetailsPresenterImplTest {

    private val artistRepository: ArtistRepository = mockk()
    private val repositoryToViewFormatter: RepositoryToViewFormatter = mockk()

    private val uiStateObserver: Observer<MoreDetailsUIState> = mockk()
    private val onUIStateSubject = Subject<MoreDetailsUIState>()
    private val moreDetailsPresenter: MoreDetailsPresenterImpl =
        MoreDetailsPresenterImpl(artistRepository, repositoryToViewFormatter).apply {
            uiStateObservable.subscribe(uiStateObserver)
        }



    @Before
    fun setup() {
        every { onUIStateSubject.notify(any()) } returns Unit
    }

    @Test
    fun `openArtistInfoWindow should load artist info and notify UI state observer`() {
        val artistName = "Radiohead"
        val artistData: ArtistData = mockk()
        val expectedUiState = MoreDetailsUIState("formatted data", "https://example.com")

        every { artistRepository.getArtistData(artistName) } returns artistData
        every { repositoryToViewFormatter.format(artistData) } returns expectedUiState.info

        moreDetailsPresenter.openArtistInfoWindow(artistName)

        verify { onUIStateSubject.notify(expectedUiState) }
    }
}
