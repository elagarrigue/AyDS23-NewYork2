package ayds.newyork.songinfo.moredetails.model.repository.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "dictionary.db"
private const val ARTISTS_TABLE_NAME = "artists"
private const val COLUMN_ID = "id"
private const val COLUMN_ARTIST = "artist"
private const val COLUMN_ARTIST_INFO = "info"
private const val COLUMN_SOURCE = "source"
private const val SOURCE_VALUE = 1
private const val ARTIST_TABLE_CREATION_QUERY = "create table $ARTISTS_TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_ARTIST string, $COLUMN_ARTIST_INFO string, $COLUMN_SOURCE integer)"
private const val SELECTION = "$COLUMN_ARTIST  = ?"
private const val ORDER = "$COLUMN_ARTIST DESC"

class DataBase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ARTIST_TABLE_CREATION_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun saveArtist(artist: String?, info: String) = this.writableDatabase.insert(ARTISTS_TABLE_NAME, null, createArtistWithValues(artist, info))

    private fun createArtistWithValues(artist: String?, info: String): ContentValues {
        val values = ContentValues()

        with(values){
            put(COLUMN_ARTIST, artist)
            put(COLUMN_ARTIST_INFO, info)
            put(COLUMN_SOURCE, SOURCE_VALUE)
        }
        return values
    }

    fun getInfo(artist: String?): String? {
        val items = cursorIterator(createCursor(this, artist))
        return if (items.isEmpty()) null else items[0]
    }

    private fun createCursor(dbHelper: DataBase, artist: String?): Cursor {
        return dbHelper.readableDatabase.query(
            ARTISTS_TABLE_NAME,
            arrayOf(COLUMN_ID, COLUMN_ARTIST, COLUMN_ARTIST_INFO),
            SELECTION,
            arrayOf(artist),
            null,
            null,
            ORDER
        )
    }

    private fun cursorIterator(cursor: Cursor): MutableList<String>{
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