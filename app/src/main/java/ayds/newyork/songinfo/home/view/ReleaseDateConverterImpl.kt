package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.Song.EmptySong
import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.entities.Song.SpotifySong
import ayds.newyork.songinfo.home.view.ReleaseDateConverterInjector

interface ReleaseDateConverter{
    fun convertToPrecision(song: Song = EmptySong): String

}

internal class ReleaseDateConverterImpl: ReleaseDateConverter{
    override fun convertToPrecision(song: Song): String {
        return if(song is SpotifySong){
            with(ReleaseDateConverterInjector.getConverter(song.releaseDatePrecision)) {
                this.convertStringToDate(song.releaseDate)
            }
        }
        else
            "Could not find date"
    }

}