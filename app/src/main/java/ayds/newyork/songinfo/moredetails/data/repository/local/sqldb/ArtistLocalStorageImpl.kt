package ayds.newyork.songinfo.moredetails.data.repository.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1
private const val SELECTION = "$COLUMN_ARTIST  = ?"
private const val ORDER = "$COLUMN_ARTIST DESC"

interface ArtistLocalStorage {
    fun saveArtist(artist: String?, info: String)

    fun getInfo(artist: String?): String?
}

internal class ArtistLocalStorageImpl(context: Context, private val cursorToArtistDataMapper: CursorToArtistDataMapper) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    ArtistLocalStorage {

    private val projection = arrayOf(
        COLUMN_ID,
        COLUMN_ARTIST,
        COLUMN_ARTIST_INFO
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ARTIST_TABLE_CREATION_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtist(artist: String?, info: String) {
        this.writableDatabase.insert(ARTISTS_TABLE_NAME, null, createArtistWithValues(artist, info))
    }

    private fun createArtistWithValues(artist: String?, info: String): ContentValues {
        val values = ContentValues()

        with(values){
            put(COLUMN_ARTIST, artist)
            put(COLUMN_ARTIST_INFO, info)
            put(COLUMN_SOURCE, SOURCE_VALUE)
        }
        return values
    }

    override fun getInfo(artist: String?): String? {
        val items = cursorToArtistDataMapper.map(createCursor(this, artist))
        return if (items.isEmpty()) null else items[0]
    }

    private fun createCursor(dbHelper: ArtistLocalStorageImpl, artist: String?): Cursor {
        return dbHelper.readableDatabase.query(
            ARTISTS_TABLE_NAME,
            projection,
            SELECTION,
            arrayOf(artist),
            null,
            null,
            ORDER
        )
    }
}