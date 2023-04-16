package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.utils.view.isLeapYear
import java.text.SimpleDateFormat

interface ReleaseDateConverterFormat{
    fun convertStringToDate(songReleaseDate: String): String
}

internal class ReleaseDateConverterFormatDay: ReleaseDateConverterFormat{
    override fun convertStringToDate(songReleaseDate: String): String {
        val inputDate = SimpleDateFormat("yyyy-MM-dd")
        val outputDate = SimpleDateFormat("dd/MM/yyyy")
        val oldDate = inputDate.parse(songReleaseDate)
        return outputDate.format(oldDate)
    }
}

internal class ReleaseDateConverterFormatMonth: ReleaseDateConverterFormat{
    override fun convertStringToDate(songReleaseDate: String): String {
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
}

internal class ReleaseDateConverterFormatYear: ReleaseDateConverterFormat{
    override fun convertStringToDate(songReleaseDate: String): String {
        var formatedDate = songReleaseDate

        if(isLeapYear(songReleaseDate))
            formatedDate += " (a leap year)"
        else
            formatedDate += " (not a leap year)"
        return formatedDate
    }
}

internal class ReleaseDateConverterFormatDefault(): ReleaseDateConverterFormat{
    override fun convertStringToDate(songReleaseDate: String): String {
        return "Incorrect ReleaseDatePrecision"
    }
}