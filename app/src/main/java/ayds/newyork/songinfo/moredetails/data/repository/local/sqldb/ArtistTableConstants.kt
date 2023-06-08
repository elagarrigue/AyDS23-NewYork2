package ayds.newyork.songinfo.moredetails.data.repository.local.sqldb

const val ARTISTS_TABLE_NAME = "artists"
const val COLUMN_ID = "id"
const val COLUMN_ARTIST = "artist"
const val COLUMN_ARTIST_INFO = "info"
const val COLUMN_SOURCE = "source"
const val COLUMN_LOGO_URL = "logo"
const val COLUMN_ARTIST_URL = "url"


const val ARTIST_TABLE_CREATION_QUERY =
    "create table $ARTISTS_TABLE_NAME " +
            "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$COLUMN_ARTIST string, $COLUMN_ARTIST_INFO string, " +
            "$COLUMN_ARTIST_URL string, " +
            "$COLUMN_LOGO_URL string, " +
            "$COLUMN_SOURCE integer)"