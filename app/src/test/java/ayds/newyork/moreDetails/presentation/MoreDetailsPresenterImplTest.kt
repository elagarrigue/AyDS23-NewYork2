package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.presentation.MoreDetailsUIState
import ayds.observer.Subject
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository as ArtistRepository

class MoreDetailsPresenterTest {

    private lateinit var uiStateSubject: Subject<MoreDetailsUIState>
    private lateinit var presenter: MoreDetailsPresenter
    private val repositoryMock: ArtistRepository = mockk()
    private val formatterMock: RepositoryToViewFormatter = mockk()

    @Before
    fun setUp() {
        presenter = MoreDetailsPresenterImpl(repositoryMock, formatterMock)
        uiStateSubject = presenter.onUIStateSubject
    }

    @Test
    fun `openArtistInfoWindow should update the UI state with the formatted data`() {
        val artistName = "Radiohead"
        val artistData = ArtistData.ArtistWithData("Radiohead","info" , "url", true)
        val expectedInfo = "[*]info"
        val expectedUIState = MoreDetailsUIState(
            expectedInfo,
            artistData.url,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU")
        every { repositoryMock.getArtistData(artistName) } returns artistData
        every { formatterMock.format(artistData) } returns expectedInfo

        presenter.openArtistInfoWindow(artistName)
        Thread.sleep(20000)

        val actualUiState = uiStateSubject.lastValue()
        assertEquals(expectedUIState, actualUiState)
    }

    @Test
    fun `on search artist it should notify the result`() {
        val uiState: MoreDetailsUIState = presenter.uiState
        val artistData: ArtistData = mockk()
        every { repositoryMock.getArtistData("artistName") } returns artistData
        val observableTester: (MoreDetailsUIState) -> Unit = mockk(relaxed = true)
        presenter.uiStateObservable.subscribe {
            observableTester(it)
        }

        presenter.openArtistInfoWindow("artistName")

        verify { observableTester(uiState) }
    }
}