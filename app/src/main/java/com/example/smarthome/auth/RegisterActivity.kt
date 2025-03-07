package com.example.smarthome.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthome.auth_initial.HomePage
import com.example.smarthome.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonReg: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initializeViews()
        setupFirebase()
        setClickListeners()
    }

    private fun initializeViews() {
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonReg = findViewById(R.id.btn_Register)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.loginNow)
    }

    private fun setupFirebase() {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    private fun setClickListeners() {
        textView.setOnClickListener {
            navigateToLogin()
        }

        buttonReg.setOnClickListener {
            registerUser()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun registerUser() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if (validateInput(email, password)) {
            showProgressBar()
            createAccount(email, password)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showToast("Enter email")
                false
            }

            TextUtils.isEmpty(password) -> {
                showToast("Enter password")
                false
            }

            else -> true
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                hideProgressBar()
                if (task.isSuccessful) {
                    onAccountCreationSuccess(email)
                } else {
                    onAccountCreationFailure(task.exception)
                }
            }
    }

    private fun onAccountCreationSuccess(email: String) {
        showToast("Account Created")
        val userId = auth.currentUser?.uid
        userId?.let {
            saveUserDataToFirestore(it, email)
        }
    }

    private fun saveUserDataToFirestore(userId: String, email: String) {
        val userData = hashMapOf(
            "Email" to email,
            "userId" to userId
        )

        firestore.collection("user").document(userId)
            .set(userData)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "User data added to Firestore")
                navigateToHomeRoom()
            }
            .addOnFailureListener { e ->
                Log.w("RegisterActivity", "Error adding user data to Firestore", e)
                showToast("Error storing user data.")
            }
    }

    private fun navigateToHomeRoom() {
        val intent = Intent(applicationContext, HomePage::class.java)
        startActivity(intent)
        finish()
    }

    private fun onAccountCreationFailure(exception: Exception?) {
        Log.e("RegisterActivity", "Authentication failed", exception)
        showToast(exception?.message ?: "Authentication failed.")
    }
}
