package ayds.newyork.songinfo.moredetails.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.domain.entities.Card
import ayds.newyork.songinfo.moredetails.domain.entities.Card.ArtistCard
import ayds.newyork.songinfo.moredetails.presentation.MoreDetailsUIState
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.observer.Observer
import com.squareup.picasso.Picasso

const val ARTIST_NAME = "artistName"

interface MoreDetailsView {
    fun setPresenter(presenter: MoreDetailsPresenter)
}

class MoreDetailsViewImpl : MoreDetailsView, AppCompatActivity() {

    private lateinit var cardsRecyclerView: RecyclerView
    private lateinit var cardsAdapter: CardsAdapter
    private val observer: Observer<MoreDetailsUIState> = Observer { value ->
        updateUIComponents(value)
    }
    private lateinit var presenter: MoreDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        initProperties()
        initObservers()
        val artistName = obtainArtistName()
        presenter.openArtistInfoWindow(artistName)
    }

    override fun setPresenter(presenter: MoreDetailsPresenter) {
        this.presenter = presenter
    }

    private fun initInjector() {
        MoreDetailsInjector.init(this)
    }

    private fun initProperties() {
        setContentView(R.layout.activity_other_info)
        cardsRecyclerView = findViewById(R.id.cardsRecyclerView)
        cardsAdapter = CardsAdapter()
        cardsRecyclerView.layoutManager = LinearLayoutManager(this)
        cardsRecyclerView.adapter = cardsAdapter
    }

    private fun initObservers() {
        presenter.uiStateObservable.subscribe(observer)
    }

    private fun obtainArtistName(): String {
        return intent.getStringExtra(ARTIST_NAME) ?: ""
    }

    private fun updateUIComponents(uiState: MoreDetailsUIState) {
        runOnUiThread {
            cardsAdapter.setCards(uiState.artistCards)
        }
    }

    private class CardsAdapter : RecyclerView.Adapter<CardViewHolder>() {

        private var cards: List<Card> = emptyList()

        fun setCards(cards: List<Card>) {
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
            if(card is ArtistCard)
                holder.bind(card)
        }

        override fun getItemCount(): Int {
            return cards.size
        }
    }

    private class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val sourceLabelTextView: TextView = itemView.findViewById(R.id.sourceLabelTextView)
        private val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
        private val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val openUrlButton: Button = itemView.findViewById(R.id.openUrlButton)

        fun bind(card: ArtistCard) {
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
}
