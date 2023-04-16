package ayds.newyork.songinfo.home.view

object ReleaseDateConverterInjector{
    fun getConverter(songReleaseDatePrecision: String) : ReleaseDateConverterFormat{
        val releaseDateConverterFormat : ReleaseDateConverterFormat = when(songReleaseDatePrecision){
            "day" ->  ReleaseDateConverterFormatDay()
            "month" -> ReleaseDateConverterFormatMonth()
            "year" ->  ReleaseDateConverterFormatYear()
            else -> ReleaseDateConverterFormatDefault()
        }
        return releaseDateConverterFormat
    }
}