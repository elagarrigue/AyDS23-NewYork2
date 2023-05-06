package ayds.newyork.songinfo.moredetails.model.repository.local

interface ArtistLocalStorage {
    fun saveArtist(artist: String?, info: String)

    fun getInfo(artist: String?): String?
}