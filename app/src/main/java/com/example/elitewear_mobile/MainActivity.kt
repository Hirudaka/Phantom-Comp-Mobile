package com.example.elitewear_mobile


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the button click listener
        val Loginbutton = findViewById<Button>(R.id.btnLogin)
        val Registerbutton = findViewById<Button>(R.id.btnRegister)
        Loginbutton.setOnClickListener {
            // Start ProductListActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        Registerbutton.setOnClickListener {
            // Start ProductListActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
