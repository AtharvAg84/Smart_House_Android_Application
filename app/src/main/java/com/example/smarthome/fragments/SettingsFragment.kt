package com.example.smarthome.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.example.smarthome.MainActivity
import com.example.smarthome.R
import com.example.smarthome.notification.NotificationActivity

class SettingsFragment : Fragment() {

    private lateinit var signOutButton: Button
    private lateinit var notifyButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Find the Signout button in the layout
        signOutButton = rootView.findViewById(R.id.signOut)
          notifyButton = rootView.findViewById(R.id.notify_button)

        // Set an OnClickListener on the Signout button
        signOutButton.setOnClickListener {
            signOut()
        }

        // Set an OnClickListener on the Signout button
        notifyButton.setOnClickListener {
            // Correct way to create the intent and start the activity from a Fragment
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
        }



        return rootView
    }

    private fun signOut() {
        // Sign out from Firebase Auth
        firebaseAuth.signOut()

        // Optionally show a message to the user that they have been signed out
        Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()

        // You may want to redirect the user to a login screen or handle navigation after sign out.
        // For example:
         val intent = Intent(activity, MainActivity::class.java)
         startActivity(intent)
         activity?.finish()  // Close the current activity after sign out
    }
}
