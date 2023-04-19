package ayds.newyork.songinfo.home.view

object ReleaseDateConverterInjector{
    fun getConverter(songReleaseDatePrecision: String) : ReleaseDateFormatStrategy{
        val releaseDateConverterFormat : ReleaseDateFormatStrategy = when(songReleaseDatePrecision){
            "day" ->  ReleaseDateFormatDayStrategy()
            "month" -> ReleaseDateFormatMonthStrategy()
            "year" ->  ReleaseDateFormatYearStrategy()
            else -> ReleaseDateFormatDefaultStrategy()
        }
        return releaseDateConverterFormat
    }
}