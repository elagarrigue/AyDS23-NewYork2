package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.model.entities.Song.EmptySong
import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.entities.Song.SpotifySong
import ayds.newyork.songinfo.utils.view.LeapYear.Companion.isLeapYear
import java.text.SimpleDateFormat

interface ReleaseDateConverter{
    fun convertToPrecision(song: Song = EmptySong): String

    fun convertStringToDateForDay(songReleaseDate: String): String

    fun convertStringToDateForMonth(songReleaseDate: String): String

    fun convertStringToDateForYear(songReleaseDate: String): String
}

internal class ReleaseDateConverterImpl: ReleaseDateConverter{
    override fun convertToPrecision(song: Song): String {
        var releaseDateConverted = "Release Date: "

        if(song is SpotifySong)
            when(song.precision){
                "day" -> {
                    releaseDateConverted += convertStringToDateForDay(song.releaseDate)
                }
                "month" -> {
                    releaseDateConverted += convertStringToDateForMonth(song.releaseDate)
                }
                "year" -> {
                    releaseDateConverted += convertStringToDateForYear(song.releaseDate)
                }
            }

        return releaseDateConverted
    }

    override fun convertStringToDateForDay(songReleaseDate: String): String {
        val inputDate = SimpleDateFormat("yyyy-MM-dd")
        val outputDate = SimpleDateFormat("dd/MM/yyyy")
        val oldDate = inputDate.parse(songReleaseDate)
        return outputDate.format(oldDate)
    }

    override fun convertStringToDateForMonth(songReleaseDate: String): String {
        val songReleaseDateMonth = songReleaseDate.substringAfter('-')
        val monthFormated = monthMapping(songReleaseDateMonth)
        return monthFormated + ", " + songReleaseDate.substringBefore('-')
    }

    private fun monthMapping(month: String): String {
        return when(month){
            "01" -> "January"
            "02" -> "February"
            "03" -> "March"
            "04" -> "April"
            "05" -> "May"
            "06" -> "June"
            "07" -> "July"
            "08" -> "August"
            "09" -> "September"
            "10" -> "October"
            "11" -> "November"
            else -> "December"
        }
    }

    override fun convertStringToDateForYear(songReleaseDate: String): String {
        var formatedDate = songReleaseDate

        if(isLeapYear(songReleaseDate))
            formatedDate += " (a leap year)"
        else
            formatedDate += " (not a leap year)"

        return formatedDate
    }
}