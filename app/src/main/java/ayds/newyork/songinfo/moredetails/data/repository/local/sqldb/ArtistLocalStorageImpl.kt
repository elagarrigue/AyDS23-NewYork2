package ayds.newyork.songinfo.moredetails.data.repository.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1
private const val SELECTION = "$COLUMN_ARTIST  = ?"
private const val ORDER = "$COLUMN_ARTIST DESC"

interface ArtistLocalStorage {

    fun saveArtist(artist: ArtistCard)

    fun getArtist(artist: String?): Card
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

    override fun saveArtist(artist: ArtistCard) {
        this.writableDatabase.insert(ARTISTS_TABLE_NAME, null, createArtistWithValues(artist))
    }

    private fun createArtistWithValues(artist: ArtistCard): ContentValues {
        val values = ContentValues()

        with(values){
            put(COLUMN_ARTIST, artist.name)
            put(COLUMN_ARTIST_INFO, artist.description)
            put(COLUMN_ARTIST_URL, artist.infoUrl)
            put(COLUMN_SOURCE, SOURCE_VALUE)
        }
        return values
    }

    override fun getArtist(artist: String?): Card {
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