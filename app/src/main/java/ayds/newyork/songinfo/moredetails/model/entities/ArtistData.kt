package ayds.newyork.songinfo.moredetails.model.entities


sealed class ArtistData {

    data class ArtistWithData(
        val info: String?,
        val url: String,
        var isInDatabase: Boolean,
    ): ArtistData()


    object EmptyArtistData : ArtistData()
}