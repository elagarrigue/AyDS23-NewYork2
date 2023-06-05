package ayds.newyork.songinfo.moredetails.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.presentation.MoreDetailsUIState
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import ayds.observer.Observer

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
        cardsAdapter = CardsAdapter()
        initRecycler()
    }

    private fun initRecycler() {
        cardsRecyclerView = findViewById(R.id.cardsRecyclerView)
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
            cardsAdapter.setCards(emptyList())
        }
    }


}
