package ayds.newyork.songinfo.moredetails.domain.entities

sealed class ArtistData {

    data class ArtistWithData(
        val name: String?,
        val info: String?,
        val url: String,
        var isInDatabase: Boolean,
    ): ArtistData()
}