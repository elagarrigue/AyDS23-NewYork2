package ayds.newyork.songinfo.moredetails.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.newyork.songinfo.R
import ayds.newyork.songinfo.moredetails.MoreDetailsInjector
import ayds.newyork.songinfo.moredetails.presentation.MoreDetailsUIState
import ayds.newyork.songinfo.moredetails.presentation.presenter.MoreDetailsPresenter
import com.squareup.picasso.Picasso
import ayds.observer.Observer

const val ARTIST_NAME = "artistName"

interface MoreDetailsView {
    fun setPresenter(presenter: MoreDetailsPresenter)
}

class MoreDetailsViewImpl : MoreDetailsView, AppCompatActivity() {

    private lateinit var artistDescriptionTextView: TextView
    private lateinit var openUrlButton: Button
    private lateinit var logoImageView: ImageView
    private val observer: Observer<MoreDetailsUIState> =
        Observer { value ->
            updateUIComponents(value)
        }
    private lateinit var presenter: MoreDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        initProperties()
        initObservers()
        val artistName = obtainArtistName()
        openArtistInfoWindow(artistName)
    }

    override fun setPresenter(presenter: MoreDetailsPresenter){
        this.presenter = presenter
    }

    private fun initInjector(){
        MoreDetailsInjector.init(this)
    }

    private fun initProperties() {
        setContentView(R.layout.activity_other_info)
        artistDescriptionTextView = findViewById(R.id.textInfo)
        logoImageView = findViewById(R.id.imageView)
        openUrlButton = findViewById(R.id.openUrlButton)
    }

    private fun initObservers() {
        presenter.uiStateObservable.subscribe(observer)
    }

    private fun obtainArtistName() = intent.getStringExtra(ARTIST_NAME)!!.toString()

    private fun updateUIComponents(uiState: MoreDetailsUIState) {
        setImage(uiState.urlImagen)
        updateArtistDescription(uiState.info)
        setButtonUrl(uiState.url)
    }
    
    private fun setImage(imageUrl: String?) {
        runOnUiThread {
            Picasso.get().load(imageUrl).into(logoImageView)
        }
    }

    private fun updateArtistDescription(finalText: String?) {
        runOnUiThread {
            artistDescriptionTextView.text = Html.fromHtml(finalText)
        }
    }

    private fun setButtonUrl(url:String?) {
        openUrlButton.setOnClickListener {
            openExternalUrl(url)
        }
    }

    private fun openExternalUrl(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun openArtistInfoWindow(artistName:String) {
        Thread {
            presenter.loadArtistInfo(artistName)
        }.start()
    }
}