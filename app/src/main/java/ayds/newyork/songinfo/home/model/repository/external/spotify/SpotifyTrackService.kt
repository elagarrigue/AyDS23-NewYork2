package ayds.newyork.songinfo.home.model.repository.external.spotify

import ayds.newyork.songinfo.home.model.entities.Song.SpotifySong

interface SpotifyTrackService {

    fun getSong(title: String): SpotifySong?
}