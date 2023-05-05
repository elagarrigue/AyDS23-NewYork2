package ayds.newyork.songinfo.moredetails.model.entities

sealed class Artist {

    data class ArtistData(
        val info: String?,
        val url: String,
        val isInDataBase: Boolean,
        val isLocallyStored: Boolean
    ): Artist()

    object EmptyArtist : Artist()
}