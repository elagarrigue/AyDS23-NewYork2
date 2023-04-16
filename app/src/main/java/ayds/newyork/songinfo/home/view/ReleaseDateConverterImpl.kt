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
        var releaseDateConverted = ""
        var releaseDateConverterFormat : ReleaseDateConverterFormat
        if(song is SpotifySong){
            releaseDateConverterFormat = ReleaseDateConverterInjector.getConverter(song.releaseDatePrecision)
            releaseDateConverted = releaseDateConverterFormat.convertStringToDate(song.releaseDate)
        }
        return releaseDateConverted
    }

}