package ayds.newyork.songinfo.utils.view

class LeapYear {
    companion object {
        fun isLeapYear(yearString: String): Boolean {
            val year = yearString.toIntOrNull()
                ?: throw IllegalArgumentException("El valor de entrada no es un número válido: $yearString")
            return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
        }
    }
}