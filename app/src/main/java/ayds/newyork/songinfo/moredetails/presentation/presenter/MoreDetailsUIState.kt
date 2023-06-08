package ayds.newyork.songinfo.moredetails.presentation.presenter

import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard

data class MoreDetailsUIState (
    var artistCards: List<ArtistCard> = mutableListOf()
)