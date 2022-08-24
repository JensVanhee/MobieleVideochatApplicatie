package com.example.mobielevideochatapplicatie

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity(), ContactAdapter.ClickListener {

    // Constante STT (= Speech To Text)
    companion object {
        private const val REQUEST_CODE_STT = 1
    }

    // Maakt instantie van TextToSpeech object met de naam textToSpeech
    // Lazy neemt een lambda of functie en returned een Lazy<T> object
    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeechEngine.language = Locale.ENGLISH
            }
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var searchView: SearchView
    private lateinit var speechButton: Button
    private var contactList = mutableListOf<String>()
    private var displayList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fillContactList()

        recyclerView = findViewById(R.id.rv_contact_list)
        contactAdapter = ContactAdapter(displayList, this)
        recyclerView.adapter = contactAdapter

        searchView = findViewById(R.id.action_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    displayList.clear()
                    val search = newText.lowercase(Locale.getDefault())

                    for (contact in contactList) {
                        if (contact.lowercase(Locale.getDefault()).contains(search)) {
                            displayList.add(contact)
                        }
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                } else {
                    displayList.clear()
                    displayList.addAll(contactList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return true
            }
        })

        speechButton = findViewById(R.id.speech_btn)

        // Wanneer een gebruiker de knop "spraakherkenning" indrukt opent de Google Spraakherkenner
        // De Taal van het systeem wordt gebruikt als herkenningstaal voor de spraakherkenner
        speechButton.setOnClickListener{
            val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            sttIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault())
            sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Met wie wil u een gesprek starten?")

            // Controleert of het systeemspraakherkenning ondersteunt
            try{
                startActivityForResult(sttIntent, REQUEST_CODE_STT)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, "Dit toestel ondersteunt spraakherkenning niet.", Toast.LENGTH_LONG).show()
            }
        }

    }

    // Succesvol ontvangen spraak wordt omgezet naar tekst in de searchview
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_STT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    result?.let {
                        val recognizedText = it[0]
                        searchView.setQuery(recognizedText, false)
                    }
                }
            }
        }
    }

    // Wanneer de applicatie geen prioriteit meer geeft op het toestel stopt de spraakherkenner
    override fun onPause() {
        textToSpeechEngine.stop()
        super.onPause()
    }

    // Wanneer de applicatie vernietigd wordt legt het systeem de spraakherkenner stil
    override fun onDestroy() {
        textToSpeechEngine.shutdown()
        super.onDestroy()
    }

    override fun clickedItem(contact: String) {
        startActivity(Intent(this, VideochatActivity::class.java).putExtra("contact", contact))
    }

    private fun fillContactList(){
        contactList.add("Bert de Jong")
        contactList.add("Celine de Bakker")
        contactList.add("Cindy de Vries")
        contactList.add("Cindy Visser")
        contactList.add("Ellen Vos")
        contactList.add("Fien de Vries")
        contactList.add("Fleur Winters")
        contactList.add("Frank Vos")
        contactList.add("Jan Jansen")
        contactList.add("Jens Vanhee")
        contactList.add("Koen Willems")
        contactList.add("Linde Winters")
        contactList.add("Luca Blom")
        contactList.add("Nico van der Wal")
        contactList.add("Pieter Zomers")
        contactList.add("Sam Boom")
        contactList.add("Sara Vermeulen")
        contactList.add("Sofie de Bruin")
        contactList.add("Tomas van Geel")
        contactList.add("Willem Kuipers")

        displayList.addAll(contactList)
    }
}