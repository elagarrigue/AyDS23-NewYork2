package ayds.newyork.songinfo.moredetails.data.repository.local.sqldb

import android.database.Cursor
import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.newyork.songinfo.moredetails.domain.entities.Source

interface CursorToArtistDataMapper {

    fun map(cursor: Cursor): List<ArtistCard>
}

internal class CursorToArtistDataMapperImpl : CursorToArtistDataMapper  {

    override fun map(cursor: Cursor): List<ArtistCard>  {
        var cards = mutableListOf<ArtistCard>()
        with(cursor){
            while(moveToNext()){
                cards.add(ArtistCard(
                    description = getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST_INFO)),
                    infoUrl = getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST_URL)),
                    isInDatabase = true,
                    source = Source.values()[getInt(cursor.getColumnIndexOrThrow(COLUMN_SOURCE))],
                    sourceLogoUrl = getString(cursor.getColumnIndexOrThrow(COLUMN_LOGO_URL))
                ))
            }
        }
        cursor.close()
        return cards
    }
}