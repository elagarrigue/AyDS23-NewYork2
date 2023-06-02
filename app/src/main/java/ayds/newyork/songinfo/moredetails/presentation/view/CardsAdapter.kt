package ayds.newyork.songinfo.moredetails.presentation.view

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import com.squareup.picasso.Picasso

class CardsAdapter : RecyclerView.Adapter<CardViewHolder>() {

    private var cards: List<Card.ArtistCard> = emptyList()

    fun setCards(cards: List<Card.ArtistCard>) {
        this.cards = cards
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int {
        return cards.size
    }
}

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val sourceLabelTextView: TextView = itemView.findViewById(R.id.sourceLabelTextView)
    private val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
    private val description: TextView = itemView.findViewById(R.id.descriptionTextView)
    private val openUrlButton: Button = itemView.findViewById(R.id.openUrlButton)

    fun bind(card: Card.ArtistCard) {
        Picasso.get().load(card.sourceLogoUrl).into(imageView)
        sourceLabelTextView.text = "Source: "
        description.text = HtmlCompat.fromHtml(card.description!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
        sourceTextView.text = card.source.toString()
        openUrlButton.setOnClickListener {
            openExternalUrl(card.infoUrl)
        }
    }

    private fun openExternalUrl(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        itemView.context.startActivity(intent)
    }
}