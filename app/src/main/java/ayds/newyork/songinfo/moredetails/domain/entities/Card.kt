package ayds.newyork.songinfo.moredetails.domain.entities

enum class Source {
    NYTimes,
    LastFm,
    Wikipedia,
    Error
}

sealed class Card {

    data class ArtistCard(
        val description: String?,
        val infoUrl: String,
        var source: Source,
        var sourceLogoUrl: String = "",
        var isInDatabase: Boolean = false,
    ): Card()

}