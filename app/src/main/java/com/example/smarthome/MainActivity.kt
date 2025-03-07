@file:Suppress("DEPRECATION")

package com.example.smarthome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthome.auth_initial.Waiting_Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

class MainActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null
    private var user_info = ArrayList<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth
        firestore = FirebaseFirestore.getInstance() // Initialize Firestore

        setContentView(R.layout.activity_main)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val gif = findViewById<GifImageView>(R.id.LogoGif)
        (gif.drawable as GifDrawable).stop()

        android.os.Handler().postDelayed({
            (gif.drawable as GifDrawable).start()
            android.os.Handler().postDelayed({
                val currentUser = auth?.currentUser // Get the current authenticated user

                //FirebaseAuth.getInstance().signOut()

                if (currentUser != null) {
                    Log.i("ireliawashere", "User exists: ${currentUser.email}")

                    //FirebaseAuth.getInstance().signOut()
                    val email = currentUser.email // Get the email of the authenticated user

                    firestore!!.collection("user")
                        .whereEqualTo("Email", email)
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful && !task.result.isEmpty) {
                                val document = task.result.documents[0]
                                if (!document.contains("Houses")) {
                                    Log.i("ireliawashere", "No Houses -> Waiting_Room")
                                    val intent = Intent(this@MainActivity, Waiting_Room::class.java)
                                    startActivity(intent)
                                } else {
                                    Log.i("ireliawashere", "Houses data found -> MainDashBoard")

                                    val intent =
                                        Intent(this@MainActivity, MainDashBoard::class.java)
                                    user_info.add(0, currentUser.email)
                                    user_info.add(1, document["Username"].toString())

                                    // Add user profile picture if exists
                                    currentUser.photoUrl?.let {
                                        user_info.add(2, it.toString())
                                    }

                                    intent.putExtra("FromMain", user_info)
                                    startActivity(intent)
                                }
                            } else {
                                Log.i("ireliawashere", "User not found in Firestore")
                            }
                        }
                } else {
                    Log.i("MOKAI", "No user logged in")
                    // No user is logged in, so redirect to HelloTherePage for login
                    val intent = Intent(this@MainActivity, HelloTherePage::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 500)
        }, 2500)
    }
}
