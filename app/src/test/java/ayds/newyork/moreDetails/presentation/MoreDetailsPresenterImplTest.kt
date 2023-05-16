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

    private lateinit var presenter: MoreDetailsPresenter
    private val repositoryMock: ArtistRepository = mockk()
    private val formatterMock: RepositoryToViewFormatter = mockk()
    private val uiStateSubject: Subject<MoreDetailsUIState> = Subject()

    @Before
    fun setUp() {
        presenter = MoreDetailsPresenterImpl(repositoryMock, formatterMock)
        every { formatterMock.format(any()) } returns "[*]formato"
        val artistData: ArtistData = mockk()
        every { repositoryMock.getArtistData(any()) } returns artistData
    }

    @Test
    fun `openArtistInfoWindow should update the UI state with the formatted data`() {
        val artistName = "Radiohead"
        presenter.openArtistInfoWindow(artistName)
        Thread.sleep(10000)

        val artistData = ArtistData.ArtistWithData("Radiohead","info" , "url", true)
        val expectedFormattedData = "[*]formato"
        every { repositoryMock.getArtistData(artistName) } returns artistData
        every { formatterMock.format(artistData) } returns expectedFormattedData

        val expectedUIState = MoreDetailsUIState(
            expectedFormattedData,
            artistData.url,
            "imagen")

        verify { formatterMock.format(artistData) }
        val actualUiState = uiStateSubject.lastValue()

        assertEquals(expectedUIState, actualUiState)
        assertEquals("imagen",actualUiState?.urlImagen)
        assertEquals("info",actualUiState?.info)
        assertEquals("url", actualUiState?.url)
    }

    @Test
    fun `loadArtistInfo should update the UI state with no results if the artist data is null`() {

        val artistName = ""
        val expectedFormattedData = "No Results"

        presenter.openArtistInfoWindow(artistName)

        val actualUiState = uiStateSubject.lastValue()

        assertEquals(expectedFormattedData, actualUiState)
        assertEquals("", actualUiState?.url)
    }
}