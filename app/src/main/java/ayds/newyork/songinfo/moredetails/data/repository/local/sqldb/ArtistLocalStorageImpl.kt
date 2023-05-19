package ayds.newyork.songinfo.moredetails.data.repository.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.ArtistWithData

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1
private const val SELECTION = "$COLUMN_ARTIST  = ?"
private const val ORDER = "$COLUMN_ARTIST DESC"

interface ArtistLocalStorage {

    fun saveArtist(artist: ArtistWithData)

    fun getArtist(artist: String?): ArtistData
}

internal class ArtistLocalStorageImpl(context: Context, private val cursorToArtistDataMapper: CursorToArtistDataMapper) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    ArtistLocalStorage {

    private val projection = arrayOf(
        COLUMN_ID,
        COLUMN_ARTIST,
        COLUMN_ARTIST_INFO,
        COLUMN_ARTIST_URL
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ARTIST_TABLE_CREATION_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtist(artist: ArtistWithData) {
        this.writableDatabase.insert(ARTISTS_TABLE_NAME, null, createArtistWithValues(artist))
    }

    private fun createArtistWithValues(artist: ArtistWithData): ContentValues {
        val values = ContentValues()

        with(values){
            put(COLUMN_ARTIST, artist.name)
            put(COLUMN_ARTIST_INFO, artist.info)
            put(COLUMN_ARTIST_URL, artist.url)
            put(COLUMN_SOURCE, SOURCE_VALUE)
        }
        return values
    }

    override fun getArtist(artist: String?): ArtistData {
        return cursorToArtistDataMapper.map(createCursor(this, artist))
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