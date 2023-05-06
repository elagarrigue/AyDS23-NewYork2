package ayds.newyork.songinfo.moredetails.model.repository.local.sqldb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ayds.newyork.songinfo.moredetails.model.repository.local.ArtistLocalStorage

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1
private const val SELECTION = "$COLUMN_ARTIST  = ?"
private const val ORDER = "$COLUMN_ARTIST DESC"

internal class ArtistLocalStorageImpl(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    ArtistLocalStorage {

    private val projection = arrayOf(
        COLUMN_ID,
        COLUMN_ARTIST,
        COLUMN_ARTIST_INFO
    )

    //Esto deberia crearse y pasarse con la creacion de la database en el inyector siguiendo analogamente lo que se hace en home
    private val cursorToArtistDataMapper: CursorToArtistDataMapper = CursorToArtistDataMapperImpl()

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