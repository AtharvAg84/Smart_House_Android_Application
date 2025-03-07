package com.example.smarthome.auth_initial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.smarthome.HelloTherePage
import com.example.smarthome.MainDashBoard
import com.example.smarthome.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class Waiting_Room : AppCompatActivity() {
    private var EmailTxt: TextView? = null
    private var buttonSignOut: AppCompatButton? = null
    private var CreateHouse: AppCompatButton? = null
    private var back: AppCompatButton? = null
    private var userImage: ImageView? = null
    private var firestore: FirebaseFirestore? = null
    private var auth: FirebaseAuth? = null
    private var data_user_list: ArrayList<String?> = ArrayList()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_house_exist_page)

        // Initialize Firebase Authentication and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // UI components
        EmailTxt = findViewById(R.id.EmailTxt)
        buttonSignOut = findViewById(R.id.buttonSignOut)
        CreateHouse = findViewById(R.id.CreateHouse)
        back = findViewById(R.id.buttonback)
        userImage = findViewById(R.id.ImageAcc)

        // Get the current user from FirebaseAuth
        val currentUser = auth?.currentUser

        if (currentUser != null) {
            val email = currentUser.email
            EmailTxt?.text = email

            // Load user image if available
            if (currentUser.photoUrl != null) {
                Picasso.get().load(currentUser.photoUrl).into(userImage)
            } else {
                userImage?.setImageResource(R.drawable.user_image)
            }

            // Fetch user data from Firestore
            firestore!!.collection("user")
                .whereEqualTo("Email", email)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (doc in task.result) {
                            Log.i("MO3MO3AZEAZE", "inside complete")
                            data_user_list.add(0, currentUser.email)
                            data_user_list.add(1, doc["Username"].toString())
                            if (currentUser.photoUrl != null) {
                                data_user_list.add(2, currentUser.photoUrl.toString())
                            }
                            // Check if "Houses" field exists in Firestore document
                            if (doc.contains("Houses")) {
                                Log.i("MO3MO3AZEAZE", "inside no house")
                                val intent = Intent(applicationContext, MainDashBoard::class.java)
                                intent.putExtra("FromMain", data_user_list)
                                finish()
                                startActivity(intent)
                            }
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "Error fetching data from Firestore",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // Sign out button
        buttonSignOut?.setOnClickListener {
            auth?.signOut() // Firebase sign out
            val intent = Intent(applicationContext, HelloTherePage::class.java)
            startActivity(intent)
            finish()
        }

        // Create House button
        CreateHouse?.setOnClickListener {
            Log.d("CreateHouse", "Checking for it")
            val intent = Intent(applicationContext, CreateHousePage::class.java)
            intent.putExtra("UserDataListChange", data_user_list)
            startActivity(intent)
            finish()
        }

        // Back button
        back?.setOnClickListener {
        }
    }
}
