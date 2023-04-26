package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBase(context: Context) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun saveArtist(artist: String?, info: String) =
        this.writableDatabase.insert("artists", null, createArtistWithValues(artist, info))


    private fun createArtistWithValues(artist: String?, info: String): ContentValues {
        val values = ContentValues()

        with(values){
            put("artist", artist)
            put("info", info)
            put("source", 1)
        }
        return values
    }

    fun getInfo(artist: String?): String? {
        val items = cursorIterator(createCursor(this, artist))
        return if (items.isEmpty()) null else items[0]
    }

    private fun createCursor(dbHelper: DataBase, artist: String?): Cursor {
        return dbHelper.readableDatabase.query(
            "artists",
            constructProjection(),
            "artist  = ?",
            arrayOf(artist),
            null,
            null,
            "artist DESC"
        )
    }

    private fun constructProjection() = arrayOf("id", "artist", "info")

    private fun cursorIterator(cursor: Cursor): MutableList<String>{
        val items: MutableList<String> = ArrayList()

        while (cursor.moveToNext()) {
            val info = cursor.getString(
                cursor.getColumnIndexOrThrow("info")
            )
            items.add(info)
        }

        cursor.close()

        return items
    }
}