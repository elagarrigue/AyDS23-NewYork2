package ayds.newyork.songinfo.moredetails.domain.entities

enum class Source {
    NYTimes
}

sealed class Card {

    data class ArtistCard(
        val name: String?,
        val description: String?,
        val infoUrl: String,
        var source: Source = Source.NYTimes,
        var sourceLogoUrl: String = "",
        var isInDatabase: Boolean = false,
    ): Card()

    object EmptyCard : Card()
}