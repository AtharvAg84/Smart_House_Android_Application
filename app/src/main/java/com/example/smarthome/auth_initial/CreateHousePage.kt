@file:Suppress("DEPRECATION")

package com.example.smarthome.auth_initial

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.smarthome.MainDashBoard
import com.example.smarthome.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class CreateHousePage : AppCompatActivity() {
    private var HName: TextInputLayout? = null
    private var Password: TextInputLayout? = null
    private var HexCode: TextInputLayout? = null
    private var firestore: FirebaseFirestore? = null
    private var database: DatabaseReference? = null
    private var CancelCreateHouse: AppCompatButton? = null
    private var HouseCreation: AppCompatButton? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_house_page)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Getting data passed from the previous screen
        val bundle = intent.extras
        val user_data_list = bundle!!.getStringArrayList("UserDataListChange")

        HName = findViewById(R.id.HouseTxt)
        Password = findViewById(R.id.KeypadPassword)
        HexCode = findViewById(R.id.CardHexNbr)
        HouseCreation = findViewById(R.id.HouseCreation)  // Initialize button
        CancelCreateHouse = findViewById(R.id.CancelCreateHouse)

        // Ensure HouseCreation is cast to AppCompatButton before use
        val houseCreationButton = HouseCreation as AppCompatButton

        houseCreationButton.setOnClickListener {
            // Validate fields before making a Firebase call
            val houseName = HName?.editText?.text.toString().trim()
            val password = Password?.editText?.text.toString().trim()
            val hexCode = HexCode?.editText?.text.toString().trim()

            if (houseName.isEmpty() || password.isEmpty() || hexCode.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Getting user data from Firebase Auth
            val currentUser = auth.currentUser
            if (currentUser != null) {
                firestore = FirebaseFirestore.getInstance()

                // Creating new house data map
                val houseData: MutableMap<String, Any> = HashMap()
                houseData["Name"] = houseName
                houseData["Password"] = password
                houseData["HexCode"] = listOf(hexCode)

                // Adding house data to Firestore
                firestore!!.collection("House").add(houseData).addOnSuccessListener { houseID ->
                    // Update user with house ID
                    firestore!!.collection("user").document(currentUser.uid)
                        .update("Houses", listOf(houseID.id))

                    // Set up the Firebase Realtime Database reference for the house
                    database = FirebaseDatabase.getInstance().getReference(houseID.id)
                    val usersRefPass = database!!.child("Password")
                    usersRefPass.setValue(password)


                    Log.d("HouseValue", "Yes Done")
                    // Update UI
                    houseCreationButton.setBackgroundColor(Color.GRAY)
                    houseCreationButton.isClickable = false

                    // Navigate to Main Dashboard
                    val intent = Intent(applicationContext, MainDashBoard::class.java)
                    intent.putExtra("FromMain", user_data_list)
                    finish()
                    startActivity(intent)
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this, "Error creating house: ${e.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            }
        }

        // Cancel action
        CancelCreateHouse?.setOnClickListener {
            val intent = Intent(applicationContext, Waiting_Room::class.java)
            intent.putExtra("UserDataListChange", user_data_list)
            finish()
            startActivity(intent)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
