package com.example.smarthome

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.smarthome.fragments.ManageFragment
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class Houses_Popup_Dialog : AppCompatDialogFragment() {
    private var dataList: List<String>? = null // List to hold the data for GridView
    private var adapter: CustomAdapter? = null // Adapter for GridView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity, R.style.MyDialogTheme)
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.activity_houses_popup_dialog, null)

        val gridView = view.findViewById<GridView>(R.id.gridView1)

        // Initialize dataList with initial data from the database
        dataList = fetchDataFromDatabase()

        // Create the adapter with the dataList
        adapter =
            activity?.let { CustomAdapter(it, android.R.layout.simple_list_item_1, dataList!!) }

        // Set the adapter to the GridView
        gridView.adapter = adapter

        builder.setView(view)
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Select") { _, _ ->
                // Handle the selection action
//                val selectedItemValue = dataList?.get(adapter?.selectedItemValue ?: -1)
//
//                // Send the selected item to ManageFragment using Bundle
//                val manageFragment = ManageFragment()
//                val bundle = Bundle()
//                bundle.putString("selected_item", selectedItemValue)
//                manageFragment.arguments = bundle
//
//                // Replace the current fragment with ManageFragment
//                val transaction = requireActivity().supportFragmentManager.beginTransaction()
//                transaction.replace(R.id.container_0, manageFragment) // Replace with your container ID
//                transaction.addToBackStack(null) // Optional, to allow back navigation
//                transaction.commit()
            }


        return builder.create()
    }


    private fun fetchDataFromDatabase(): List<String> {
        // Initialize an empty list to hold the data
        val data: MutableList<String> = ArrayList()

        // Get the Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Reference to the 'room_types' collection
        val roomTypesRef = db.collection("room_types")

        // Fetch the first document in the collection asynchronously
        roomTypesRef.limit(1).get()
            .addOnSuccessListener { result ->
                // Check if the query returned any document
                if (!result.isEmpty) {
                    // Get the first document in the query result
                    val document: DocumentSnapshot? = result.documents[0]

                    // Get the 'room' array field from the document
                    val roomList = document?.get("rooms") as? List<String> ?: emptyList()

                    // Add all items in the 'room' array to the data list
                    data.addAll(roomList)

                    // Notify the adapter to update the GridView after data is fetched
                    adapter?.notifyDataSetChanged()
                } else {
                    // Handle the case where no documents are found
                    Log.w("Firebase", "No documents found!")
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error, e.g. show a Toast or Log
                Log.w("Firebase", "Error getting documents.", exception)
            }

        // Return the empty list initially, as the fetch is asynchronous
        return data
    }


    private inner class CustomAdapter(
        context: Context,
        private val resourceId: Int,
        dataList: List<String?>
    ) : ArrayAdapter<String?>(context, resourceId, dataList) {
        var selectedItemValue = -1

        // Rename this method to avoid clash
        fun updateSelectedItem(position: Int) {
            selectedItemValue = position
            notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false)
            }

            val itemLayout = convertView!!.findViewById<LinearLayout>(R.id.itemLayout)
            val itemTextView = convertView.findViewById<TextView>(R.id.itemTextView)
            val imageView = convertView.findViewById<ImageView>(R.id.textdivider)

            val dataItem = getItem(position)
            itemTextView.text = dataItem
            itemLayout.isClickable = true

            if (position == selectedItemValue) {
                itemLayout.setBackgroundColor(Color.parseColor("#a4b6ce"))
            } else {
                itemLayout.setBackgroundColor(Color.WHITE)
            }

            itemLayout.setOnClickListener { updateSelectedItem(position) }

            return convertView
        }

        override fun isEnabled(position: Int): Boolean {
            // Enable selection on grid items
            return true
        }
    }
}