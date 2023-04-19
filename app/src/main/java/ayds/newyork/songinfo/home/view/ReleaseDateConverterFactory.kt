package ayds.newyork.songinfo.home.view

private const val DAY_PRECISION = "day"
private const val MONTH_PRECISION = "month"
private const val YEAR_PRECISION = "year"

object ReleaseDateConverterFactory{
    fun getConverter(songReleaseDatePrecision: String): ReleaseDateFormatStrategy =
        when (songReleaseDatePrecision) {
            DAY_PRECISION -> ReleaseDateFormatDayStrategy()
            MONTH_PRECISION -> ReleaseDateFormatMonthStrategy()
            YEAR_PRECISION -> ReleaseDateFormatYearStrategy()
            else -> ReleaseDateFormatDefaultStrategy()
        }
}
