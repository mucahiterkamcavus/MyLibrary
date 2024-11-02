package com.example.mylibrary

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bandirma.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.checkerframework.common.returnsreceiver.qual.This

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        val email = findViewById<EditText>(R.id.txtEmail)
        val password = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener{

            val _email = email.text.toString()
            val _password = password.text.toString()

            auth.signInWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext,"Giriş Başarılı", Toast.LENGTH_SHORT).show()

                        val user = User(auth.currentUser?.uid.toString(),
                            auth.currentUser?.displayName.toString(),
                        "",
                            _password,
                            _email
                        )
                        val database = Firebase.database.reference
                        database.child("users").child(user.id).setValue(user)

                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(applicationContext,"Hatalı Giriş", Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }
}