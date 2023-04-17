package ayds.newyork.songinfo.home.view

object ReleaseDateConverterInjector{
    fun getConverter(songReleaseDatePrecision: String) : ReleaseDateFormatStrategy{
        val releaseDateConverterFormat : ReleaseDateFormatStrategy = when(songReleaseDatePrecision){
            "day" ->  ReleaseDateFormatStrategyDay()
            "month" -> ReleaseDateFormatStrategyMonth()
            "year" ->  ReleaseDateFormatStrategyYear()
            else -> ReleaseDateFormatStrategyDefault()
        }
        return releaseDateConverterFormat
    }
}