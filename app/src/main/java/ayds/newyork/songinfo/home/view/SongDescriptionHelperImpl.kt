package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.Song.EmptySong
import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl(private val releaseDateConverter: ReleaseDateConverter) : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release date: ${releaseDateConverter.convertToPrecision(song)}"
            else -> "Song not found"
        }
    }
}