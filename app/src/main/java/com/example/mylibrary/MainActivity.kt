package com.example.mylibrary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val lblInfo = findViewById<TextView>(R.id.lblInfo)
        auth = Firebase.auth
        val database = Firebase.database.reference
        database.child("userbooks")
            .child(auth.currentUser?.uid.toString())
            .get().addOnSuccessListener {

                if (it.exists()) {
                    Toast.makeText(applicationContext, "Kayıt var", Toast.LENGTH_SHORT).show()
                    lblInfo.visibility = View.GONE
                } else {
                    Toast.makeText(applicationContext, "Kayıt yok", Toast.LENGTH_SHORT).show()
                }
            }
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        val btnScan = findViewById<FloatingActionButton>(R.id.btnScan)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        scannerView.visibility = View.GONE
        btnScan.setOnClickListener{
            if(scannerView.visibility==View.GONE) {
                scannerView.visibility = View.VISIBLE
                codeScanner.startPreview()
                val intent = Intent(this,AddMyLibrary::class.java)
                intent.putExtra("barcode","12345")
                startActivity(intent)
                this.finish()
            }
            else {
                scannerView.visibility = View.GONE
                codeScanner.releaseResources()
            }
        }
    }
}