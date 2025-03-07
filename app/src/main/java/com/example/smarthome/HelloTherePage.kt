package com.example.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.smarthome.auth.LoginActivity
import com.example.smarthome.auth.RegisterActivity

class HelloTherePage : AppCompatActivity() {
    private lateinit var buttonSignUp: AppCompatButton
    private lateinit var buttonLogin: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_there_page)  // Make sure the XML is named correctly

        // Initialize buttons
        buttonSignUp = findViewById(R.id.button_signup)
        buttonLogin = findViewById(R.id.button_login)

        // Set OnClickListener for Sign Up button
        buttonSignUp.setOnClickListener {
            // Navigate to SignUpActivity when the Sign Up button is clicked
            val intent = Intent(this@HelloTherePage, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Set OnClickListener for Login button
        buttonLogin.setOnClickListener {
            // Navigate to LoginActivity when the Login button is clicked
            val intent = Intent(this@HelloTherePage, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
