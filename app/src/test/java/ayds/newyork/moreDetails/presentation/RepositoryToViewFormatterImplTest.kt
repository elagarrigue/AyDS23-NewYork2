package ayds.newyork.moreDetails.presentation

import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.EmptyArtistData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.ArtistWithData
import ayds.newyork.songinfo.moredetails.presentation.presenter.RepositoryToViewFormatter
import ayds.newyork.songinfo.moredetails.presentation.presenter.RepositoryToViewFormatterImpl
import org.junit.Assert.assertEquals
import org.junit.Test
import io.mockk.mockk

class RepositoryToViewFormatterImplTest {

    private val formatter: RepositoryToViewFormatter = RepositoryToViewFormatterImpl()

    @Test
    fun `given artist with data in database returns formatted text with local repository marker`() {
        val artist = ArtistWithData("Michael Jackson", "The King of Pop","url", true)
        val expected = "<html><div width=400><font face=arial>[*]The King of Pop</font></div></html>"
        assertEquals(expected, formatter.format(artist))
    }

    @Test
    fun `given artist with no data in database returns formatted text without local repository marker`() {
        val artist = ArtistWithData("Queen", "A British rock band", "url", false)
        val expected = "<html><div width=400><font face=arial>A British rock band</font></div></html>"
        assertEquals(expected, formatter.format(artist))
    }

    @Test
    fun `given artist without data returns no results`() {
        val artist: EmptyArtistData = mockk()
        assertEquals("No Results", formatter.format(artist))
    }
}
