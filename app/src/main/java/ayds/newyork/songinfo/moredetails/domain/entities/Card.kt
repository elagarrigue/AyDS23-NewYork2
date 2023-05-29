package ayds.newyork.songinfo.moredetails.domain.entities

enum class Source {
    NYTimes,
    LastFm,
    Wikipedia
}

sealed class Card {

    data class ArtistCard(
        var name: String?,
        val description: String?,
        val infoUrl: String,
        var source: Source,
        var sourceLogoUrl: String = "",
        var isInDatabase: Boolean = false,
    ): Card()

    object EmptyCard : Card()
}