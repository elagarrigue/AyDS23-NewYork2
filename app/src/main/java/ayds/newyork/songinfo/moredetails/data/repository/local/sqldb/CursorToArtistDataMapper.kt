package ayds.newyork.songinfo.moredetails.data.repository.local.sqldb

import android.database.Cursor

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