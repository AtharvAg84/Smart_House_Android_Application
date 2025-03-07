@file:Suppress("DEPRECATION")

package com.example.smarthome

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.smarthome.auth_initial.Waiting_Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.AggregateQuerySnapshot
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore

class SignUpPage : AppCompatActivity() {
    private var gso: GoogleSignInOptions? = null
    private var gsc: GoogleSignInClient? = null
    private var firestore: FirebaseFirestore? = null
    private var data_user_list: ArrayList<String?> = ArrayList()
    private lateinit var returnbackbutton: Button

    // Declare the launcher for ActivityResult
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                handleSignInResult(data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        returnbackbutton = findViewById(R.id.buttonback)
        returnbackbutton.setOnClickListener { finish() }

        val creataccfirebase = findViewById<Button>(R.id.buttoncreatfirebaseacc)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this, gso!!)

        creataccfirebase.setOnClickListener {
            Toast.makeText(this@SignUpPage, "Redirecting You To Firebase", Toast.LENGTH_SHORT)
                .show()
            signIn()
        }

        //*******************************************************

        // Create the container layout styled as a button
        val containerLayout = ConstraintLayout(this)
        containerLayout.id = View.generateViewId()
        containerLayout.background = ContextCompat.getDrawable(this, R.drawable.rounded_white_bg)
        containerLayout.isClickable = true

        // Create the elements
        val imageView = ImageView(this)
        imageView.id = View.generateViewId()
        imageView.layoutParams = ConstraintLayout.LayoutParams(58, 58)
        imageView.setImageResource(R.drawable.icon)
        imageView.setColorFilter(ContextCompat.getColor(this, R.color.gray_null))

        val textView = TextView(this)
        textView.id = View.generateViewId()
        textView.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.typeface = ResourcesCompat.getFont(this, R.font.outfit_semibold)
        textView.text = "SmartLamp"
        textView.setTextColor(ContextCompat.getColor(this, R.color.gray_null))
        textView.textSize = 28f

        val descriptionTextView = TextView(this)
        descriptionTextView.id = View.generateViewId()
        descriptionTextView.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        descriptionTextView.setTextColor(Color.parseColor("#c1c1c1"))
        descriptionTextView.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.baseline_circle_24,
            0,
            0,
            0
        )
        descriptionTextView.compoundDrawablePadding = 6
        descriptionTextView.typeface = ResourcesCompat.getFont(this, R.font.outfit_medium)
        descriptionTextView.text = "Esp32 colorful lamp"
        descriptionTextView.textSize = 12f
        descriptionTextView.compoundDrawableTintList =
            ColorStateList.valueOf(Color.parseColor("#c1c1c1"))

        val switchCompat = androidx.appcompat.widget.SwitchCompat(this)
        switchCompat.id = View.generateViewId()
        switchCompat.layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        switchCompat.typeface = ResourcesCompat.getFont(this, R.font.outfit_medium)
        switchCompat.text = "Turned Off  "
        switchCompat.setTextColor(ContextCompat.getColor(this, R.color.gray_null))

        // Add the elements to the container layout
        containerLayout.addView(imageView)
        containerLayout.addView(textView)
        containerLayout.addView(descriptionTextView)
        containerLayout.addView(switchCompat)

        // Create the parent container layout
        val parentContainer = findViewById<ConstraintLayout>(R.id.parent_devicecard)

        // Add the container layout to the parent container
        parentContainer.addView(containerLayout)

        // Set constraints between the elements
        val constraintSet = ConstraintSet()
        constraintSet.clone(containerLayout)

        constraintSet.connect(
            imageView.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            16
        )
        constraintSet.connect(
            imageView.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            16
        )

        constraintSet.connect(textView.id, ConstraintSet.START, imageView.id, ConstraintSet.END, 16)
        constraintSet.connect(textView.id, ConstraintSet.TOP, imageView.id, ConstraintSet.TOP)

        constraintSet.connect(
            descriptionTextView.id,
            ConstraintSet.START,
            textView.id,
            ConstraintSet.START
        )
        constraintSet.connect(
            descriptionTextView.id,
            ConstraintSet.TOP,
            textView.id,
            ConstraintSet.BOTTOM,
            8
        )

        constraintSet.connect(
            switchCompat.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START,
            16
        )
        constraintSet.connect(
            switchCompat.id,
            ConstraintSet.TOP,
            imageView.id,
            ConstraintSet.BOTTOM,
            16
        )

        constraintSet.applyTo(containerLayout)
    }

    private fun signIn() {
        val intent = gsc!!.signInIntent
        signInLauncher.launch(intent) // Launch the Google Sign-In Intent
    }

    private fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            task.getResult(ApiException::class.java)
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) {
                val username = account.displayName
                val mail = account.email
                firestore = FirebaseFirestore.getInstance()

                val countQuery = firestore!!.collection("user").whereEqualTo("Email", mail).count()
                countQuery[AggregateSource.SERVER].addOnCompleteListener { tasks: Task<AggregateQuerySnapshot> ->
                    val snapshot = tasks.result
                    if (snapshot.count != 0L) {
                        Toast.makeText(this@SignUpPage, "This account exists", Toast.LENGTH_SHORT)
                            .show()
                        firestore!!.collection("user")
                            .whereEqualTo("Email", mail).get().addOnCompleteListener { task ->
                                for (doc in task.result) {
                                    if (!doc.contains("Houses")) {
                                        val intent =
                                            Intent(applicationContext, Waiting_Room::class.java)
                                        finish()
                                        startActivity(intent)
                                    } else {
                                        val intent =
                                            Intent(applicationContext, MainDashBoard::class.java)
                                        finish()
                                        startActivity(intent)
                                    }
                                }
                            }
                    } else {
                        data_user_list.add(0, username)
                        data_user_list.add(1, mail)
                        Toast.makeText(this, "Email: $mail Username: $username", Toast.LENGTH_SHORT)
                            .show()
                        homeActivity()
                    }
                }
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun homeActivity() {
        finish()
//        val intent = Intent(applicationContext, HomePage::class.java)
//        intent.putExtra("UserDataList", data_user_list)
//        startActivity(intent)
    }
}
