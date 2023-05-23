package ayds.newyork.songinfo.moredetails.data.repository.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.EmptyArtistData
import ayds.newyork.songinfo.moredetails.domain.entities.ArtistData.ArtistWithData

interface CursorToArtistDataMapper {

    fun map(cursor: Cursor): ArtistData
}

internal class CursorToArtistDataMapperImpl : CursorToArtistDataMapper  {

    override fun map(cursor: Cursor): ArtistData =
         with(cursor){
             if(cursor.moveToNext()){
                 ArtistWithData(
                     name = getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST)),
                     info = getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST_INFO)),
                     url = getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST_URL)),
                     isInDatabase = true
                 )
             }
             else
                 EmptyArtistData
         }

}