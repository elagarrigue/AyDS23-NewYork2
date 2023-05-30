package ayds.newyork.songinfo.moredetails.presentation

import ayds.newyork.songinfo.moredetails.domain.entities.Card

data class MoreDetailsUIState(
    val artistCards: List<Card>
)