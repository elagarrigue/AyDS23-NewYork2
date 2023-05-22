package ayds.newyork.songinfo.moredetails.data.repository.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Card.EmptyCard
import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard

interface CursorToArtistDataMapper {

    fun map(cursor: Cursor): Card
}

internal class CursorToArtistDataMapperImpl : CursorToArtistDataMapper  {

    override fun map(cursor: Cursor): Card =
         with(cursor){
             if(cursor.moveToNext()){
                 ArtistCard(
                     name = getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST)),
                     description = getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST_INFO)),
                     infoUrl = getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST_URL)),
                     isInDatabase = true
                 )
             }
             else
                 EmptyCard
         }

}