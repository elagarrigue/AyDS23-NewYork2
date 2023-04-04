package ayds.newyork.songinfo.home.model.entities

sealed class Song {

    data class SpotifySong(
        val id: String,
        val songName: String,
        val artistName: String,
        val albumName: String,
        val releaseDate: String,
        val spotifyUrl: String,
        val imageUrl: String,
        val precision: String,
        var isLocallyStored: Boolean = false
    ) : Song() {

        val year: String = releaseDate.split("-").first()

        val fechaExacta: String =
            when (precision) {
                "day" -> "dice dia"
                "month" -> "dice month"
                "year" -> "dice year"
                else -> "undefined"
            }
    }

    object EmptySong : Song()
}