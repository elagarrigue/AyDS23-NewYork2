package ayds.newyork.songinfo.home.view

object ReleaseDateConverterInjector{
    private const val DAY_PRECISION = "day"
    private const val MONTH_PRECISION = "month"
    private const val YEAR_PRECISION = "year"

    fun getConverter(songReleaseDatePrecision: String) : ReleaseDateFormatStrategy{
        val releaseDateConverterFormat : ReleaseDateFormatStrategy = when(songReleaseDatePrecision){
            DAY_PRECISION -> ReleaseDateFormatDayStrategy()
            MONTH_PRECISION -> ReleaseDateFormatMonthStrategy()
            YEAR_PRECISION -> ReleaseDateFormatYearStrategy()
            else -> ReleaseDateFormatDefaultStrategy()
        }
        return releaseDateConverterFormat
    }
}

