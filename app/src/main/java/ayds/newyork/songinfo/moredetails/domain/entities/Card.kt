package ayds.newyork.songinfo.moredetails.domain.entities

enum class Source {
    NYTimes,
    LastFm,
    Wikipedia
}

data class Card (
    var name: String?,
    var description: String? = "",
    var infoUrl: String?,
    var source: Source,
    var sourceLogoUrl: String = "",
    var isLocallyStored: Boolean = false
)

