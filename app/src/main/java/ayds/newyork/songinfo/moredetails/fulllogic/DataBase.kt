package ayds.newyork.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBase(context: Context) : SQLiteOpenHelper(context, "dictionary.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table artists (id INTEGER PRIMARY KEY AUTOINCREMENT, artist string, info string, source integer)"
        )
        Log.i("DB", "DB created")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun DataBase.saveArtist(artist: String, info: String) =
        this.writableDatabase.insert("artists", null, createArtistWithValues(artist, info))


    private fun createArtistWithValues(artist: String, info: String): ContentValues{
        val values = ContentValues()

        values.put("artist", artist)
        values.put("info", info)
        values.put("source", 1)

        return values
    }

    @JvmStatic
    fun getInfo(dbHelper: DataBase, artist: String): String? {
        val db = dbHelper.readableDatabase

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        val projection = arrayOf(
            "id",
            "artist",
            "info"
        )

// Filter results WHERE "title" = 'My Title'
        val selection = "artist  = ?"
        val selectionArgs = arrayOf(artist)

// How you want the results sorted in the resulting Cursor
        val sortOrder = "artist DESC"
        val cursor = db.query(
            "artists",  // The table to query
            projection,  // The array of columns to return (pass null to get all)
            selection,  // The columns for the WHERE clause
            selectionArgs,  // The values for the WHERE clause
            null,  // don't group the rows
            null,  // don't filter by row groups
            sortOrder // The sort order
        )
        val items: MutableList<String> = ArrayList()
        while (cursor.moveToNext()) {
            val info = cursor.getString(
                cursor.getColumnIndexOrThrow("info")
            )
            items.add(info)
        }
        cursor.close()
        return if (items.isEmpty()) null else items[0]
    }

}