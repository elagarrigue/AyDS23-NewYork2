package ayds.newyork.songinfo.moredetails.presentation

private const val IMAGE_URL =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

data class MoreDetailsUIState(
    val info: String? = "",
    val url: String? = null,
    val urlImagen: String = IMAGE_URL
)