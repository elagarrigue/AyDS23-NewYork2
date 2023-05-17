package ayds.newyork.moreDetails.data

import ayds.newyork.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.newyork.songinfo.moredetails.data.repository.external.nytimes.service.NYTimesService
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import java.lang.Exception

class ArtistRepositoryTest {

    private val artistLocalStorage: ArtistLocalStorage = mockk(relaxUnitFun = true)
    private val nyTimesService: NYTimesService = mockk(relaxUnitFun = true)

    private val artistRepository: ArtistRepository by lazy {
        ArtistRepositoryImpl(artistLocalStorage, nyTimesService)
    }

    @Test
    fun `given local storaged artistInfo should return ArtistData`() {
        every { artistLocalStorage.getInfo("artistName") } returns "info"
        every { nyTimesService.getURLWithArtistName("artistName") } returns "url"

        val artistData = ArtistData.ArtistWithData(
            "artistName",
            "info",
            "url",
            true
        )

        var result = artistRepository.getArtistData("artistName")

        Assert.assertEquals(artistData, result)
    }

    @Test
    fun `given non local storaged artist should search on API`() {
        val artistData: ArtistData.ArtistWithData = mockk()
        every { artistLocalStorage.getInfo("artistName") } returns null
        every { nyTimesService.getArtistInfo("artistName") } returns artistData

        val result = artistRepository.getArtistData("artistName")

        Assert.assertEquals(artistData, result)
    }

    @Test
    fun `given service exception should return empty artist`() {
        every { artistLocalStorage.getInfo("artistName") } returns null
        every { nyTimesService.getArtistInfo("artistName") } throws mockk<Exception>()

        val result = artistRepository.getArtistData("artistName")

        Assert.assertEquals(null, result)
    }
}