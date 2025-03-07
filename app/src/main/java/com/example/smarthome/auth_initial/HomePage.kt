package com.example.smarthome.auth_initial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthome.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.textfield.TextInputLayout

class HomePage : AppCompatActivity() {
    var UsernameTxt: TextInputLayout? = null
    var PasswordTxt: TextInputLayout? = null
    var PhoneNbr: TextInputLayout? = null
    var EmailTxt: TextView? = null
    var data_user: ArrayList<String> = ArrayList()
    var CreateHouse: Button? = null
    var firestore: FirebaseFirestore? = null
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // UI components
        UsernameTxt = findViewById(R.id.UsernameTxt)
        PhoneNbr = findViewById(R.id.PhoneNbr)
        PasswordTxt = findViewById(R.id.Password)
        EmailTxt = findViewById(R.id.EmailTxt)
        CreateHouse = findViewById(R.id.CreateHouse)

        // Fetch the current user from FirebaseAuth
        val currentUser = firebaseAuth?.currentUser

        if (currentUser != null) {
            val email = currentUser.email
            EmailTxt?.text = email // Set the email to the TextView
            EmailTxt?.isFocusable = false  // Make the email field non-editable
            EmailTxt?.isClickable = false  // Make the email field non-interactive
        }

        // Get the user data passed from the previous activity (registration or login)
        val bundle = intent.extras
        val user_data_list = bundle?.getStringArrayList("UserDataList")

        // Set Username if available
        if (user_data_list != null && user_data_list.isNotEmpty()) {
            UsernameTxt?.getEditText()!!.setText(user_data_list[0])
        }

        // Set the button's click listener to create the house
        CreateHouse?.setOnClickListener {
            // Ensure all necessary fields are populated
            if (UsernameTxt?.getEditText()!!.text.isNotEmpty() && PhoneNbr?.getEditText()!!.text.isNotEmpty() && PasswordTxt?.getEditText()!!.text.isNotEmpty()) {
                // Add data to the data_user list
                data_user.add(0, UsernameTxt?.getEditText()!!.text.toString())
                data_user.add(1, PhoneNbr?.getEditText()!!.text.toString())
                data_user.add(2, EmailTxt?.text.toString()) // Use the email fetched from FirebaseAuth
                data_user.add(3, PasswordTxt?.getEditText()!!.text.toString())

                // Now upload the updated data to Firestore under the same user ID
                val userId = firebaseAuth?.currentUser?.uid
                if (userId != null) {
                    val userData = hashMapOf(
                        "Username" to UsernameTxt?.getEditText()!!.text.toString(),
                        "Phone" to PhoneNbr?.getEditText()!!.text.toString(),
                        "Email" to EmailTxt?.text.toString(),
                        "Password" to PasswordTxt?.getEditText()!!.text.toString(),
                        "userId" to userId
                    )

                    // Upload data to Firestore in the 'user' collection, under the current user's UID
                    firestore?.collection("user")?.document(userId)
                        ?.set(userData)
                        ?.addOnSuccessListener {
                            Log.d("HomePage", "User data successfully uploaded to Firestore")
                            // Proceed to the Waiting_Room activity
                            val intent = Intent(applicationContext, Waiting_Room::class.java)
                            intent.putExtra("UserDataListChange", data_user)
                            finish()  // Finish the current activity
                            startActivity(intent)  // Start the new activity
                        }
                        ?.addOnFailureListener { e ->
                            Log.d("HomePage", "Error uploading data: ", e)
                            Toast.makeText(applicationContext, "Error saving data to Firestore", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(applicationContext, "User not authenticated", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Handle empty fields case (inform the user)
                Toast.makeText(applicationContext, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Handle any necessary cleanup tasks, if required
    }
}
