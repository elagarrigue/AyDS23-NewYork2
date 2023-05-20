package ayds.newyork.moreDetails.data

import ayds.newyork.songinfo.moredetails.data.repository.ArtistRepositoryImpl
import ayds.aknewyork.external.service.NYTimesService
import ayds.newyork.songinfo.moredetails.data.repository.local.sqldb.ArtistLocalStorage
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.ArtistWithData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.EmptyArtistData
import ayds.newyork.songinfo.moredetails.domain.repository.ArtistRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import java.lang.Exception

class ArtistRepositoryTest {

    private val artistLocalStorage: ArtistLocalStorage = mockk(relaxUnitFun = true)
    private val nyTimesService: ayds.aknewyork.external.service.NYTimesService = mockk(relaxUnitFun = true)

    private val artistRepository: ArtistRepository by lazy {
        ArtistRepositoryImpl(artistLocalStorage, nyTimesService)
    }

    @Test
    fun `given local storaged artistInfo should return ArtistData`() {
        val artistData = ArtistWithData("name", "info", "url", true)
        every { artistLocalStorage.getArtist("artistName") } returns artistData

        val result = artistRepository.getArtistData("artistName")

        Assert.assertEquals(artistData, result)
    }

    @Test
    fun `given non local storaged artist should search on API`() {
        val artistData: ArtistWithData = mockk()
        every { artistLocalStorage.getArtist("artistName") } returns EmptyArtistData
        every { nyTimesService.getArtistInfo("artistName") } returns artistData

        val result = artistRepository.getArtistData("artistName")

        Assert.assertEquals(artistData, result)
    }

    @Test
    fun `given service exception should return empty artist`() {
        every { artistLocalStorage.getArtist("artistName") } returns EmptyArtistData
        every { nyTimesService.getArtistInfo("artistName") } throws mockk<Exception>()

        val result = artistRepository.getArtistData("artistName")

        Assert.assertEquals(EmptyArtistData, result)
    }
}