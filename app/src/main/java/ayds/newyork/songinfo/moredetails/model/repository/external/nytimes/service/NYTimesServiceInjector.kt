package ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.service

import ayds.newyork.songinfo.moredetails.model.repository.external.nytimes.NYTimesService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object NYTimesServiceInjector {
    private const val LINK_API_NYTIMES = "https://api.nytimes.com/svc/search/v2/"

    private val nyTimesAPIRetrofit = Retrofit.Builder()
        .baseUrl(LINK_API_NYTIMES)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val nyTimesAPI = nyTimesAPIRetrofit.create(NYTimesAPI::class.java)
    private val nyTimesToArtistResolver: NYTimesToArtistResolver = JsonToArtistResolver()

    val nyTimesService: NYTimesService = NYTimesServiceImpl(
        nyTimesAPI,
        nyTimesToArtistResolver
    )
}