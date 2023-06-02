package ayds.newyork.songinfo.moredetails.presentation

import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard

data class MoreDetailsUIState(
    val artistCards: List<ArtistCard>
)