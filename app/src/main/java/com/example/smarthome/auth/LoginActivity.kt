package com.example.smarthome.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthome.MainDashBoard
import com.example.smarthome.R
import com.example.smarthome.auth_initial.Waiting_Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private var firestore: FirebaseFirestore? = null

    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance() // Initialize Firestore

        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.registerNow)

        textView.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val email: String = editTextEmail.text.toString().trim()
            val password: String = editTextPassword.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this@LoginActivity, "Enter email", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this@LoginActivity, "Enter password", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    user?.let {
                        val mail = it.email
                        firestore?.collection("user")?.whereEqualTo("Email", mail)?.get()
                            ?.addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful && !firestoreTask.result.isEmpty) {
                                    val document = firestoreTask.result.documents[0]
                                    if (!document.contains("Houses")) {
                                        Log.i("ireliawashere", "WaitingRoom")
                                        val intent = Intent(
                                            this@LoginActivity, Waiting_Room::class.java
                                        )
                                        startActivity(intent)
                                    } else {
                                        Log.i("ireliawashere", "MainDash")
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "Login Successful. Redirect to MainDash.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        val intent = Intent(
                                            this@LoginActivity, MainDashBoard::class.java
                                        )
                                        val userInfo = arrayListOf(
                                            it.email,
                                            document["Username"].toString(),
                                            it.photoUrl.toString()
                                        )
                                        intent.putExtra("FromMain", userInfo)
                                        startActivity(intent)
                                    }
                                } else {
                                    Log.i("ireliawashere", "User not found in Firestore")
                                    val intent =
                                        Intent(this@LoginActivity, RegisterActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Authentication failed. Please check your credentials.",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}
