package com.example.mylibrary

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bandirma.entity.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.EventListener
import java.util.Objects

class AddMyLibrary : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_my_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val barcode = intent.getStringExtra("barcode")

        val edtBarcode = findViewById<EditText>(R.id.edtBarcode)
        val edtBookName = findViewById<EditText>(R.id.edtBookName)
        val edtAuthorName = findViewById<EditText>(R.id.edtAuthorName)
        val edtPublishYear = findViewById<EditText>(R.id.edtPublishedYear)
        val edtPage = findViewById<EditText>(R.id.edtPageCount)
        val swTransient = findViewById<Switch>(R.id.switch1)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val edtCoverPhoto = findViewById<EditText>(R.id.edtCoverPhotoUrl)

        edtBarcode.setText(barcode)

        val database = Firebase.database.reference
        database.child("books").child(barcode.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val books = snapshot.value
                    edtBookName.setText(snapshot.child("name").value.toString())
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        btnSave.setOnClickListener {
            val _book = Book(
                1, edtBookName.text.toString(),
                barcode.toString(), "2002", 23, null, "111",
                "", false, null, null
            )
            database.child("books")
                .child(barcode.toString())
                .setValue(_book)
            addMyBook(barcode.toString())

        }
    }
    fun addMyBook(id:String) {
        val database = Firebase.database.reference

        val liste = ArrayList<String> ()
        database.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("books")
            .addListenerForSingleValueEvent(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        liste.add(id)
        database.child("users")
            .child(auth.currentUser?.uid.toString())
            .child("books")
            .setValue(liste)

    }
}