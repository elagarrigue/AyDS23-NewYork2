package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.home.model.entities.Song
import ayds.newyork.songinfo.home.model.repository.local.spotify.sqldb.CursorToSpotifySongMapper
import ayds.newyork.songinfo.moredetails.model.entities.ArtistData
import java.sql.SQLException

interface CursorToArtistDataMapper {

    fun map(cursor: Cursor): MutableList<String>
}

internal class CursorToArtistDataMapperImpl : CursorToArtistDataMapper {

    override fun map(cursor: Cursor): MutableList<String>{
        val items: MutableList<String> = ArrayList()
        while (cursor.moveToNext()) {
            val info = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_ARTIST_INFO)
            )
            items.add(info)
        }
        cursor.close()
        return items
    }
}