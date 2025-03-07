package com.example.smarthome.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.Houses_Popup_Dialog
import com.example.smarthome.R

class ManageFragment : Fragment() {
    var HousePopupList: AppCompatButton? = null
    private var selectedItem: String? = null

    private var dataList: List<String>? = null // List to hold the data for GridView
    private var adapter_user: CustomAdapter_user? = null // Adapter for GridView
    private var adapter_hex: CustomAdapter_hex? = null // Adapter for GridView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_manage, container, false)
        HousePopupList = view.findViewById(R.id.ShowHouses)
        HousePopupList?.run { setOnClickListener({ openDialog() }) }

        val recyclerView_hex = view.findViewById<RecyclerView>(R.id.recyclerView_hex)

        // Initialize dataList with initial data from the database
        dataList = fetchDataFromDatabase()

        // Set the layout manager
        val layoutManager_hex = GridLayoutManager(activity, 1)
        recyclerView_hex.layoutManager = layoutManager_hex

        // Create the adapter with the dataList
        adapter_hex = CustomAdapter_hex(activity, R.layout.grid_item_layout_hex, dataList)
        recyclerView_hex.adapter = adapter_hex

        //********************************************************************
        val recyclerView_user = view.findViewById<RecyclerView>(R.id.recyclerView_user)

        //        // Initialize dataList with initial data from the database
        dataList = fetchDataFromDatabase()




        // Set the layout manager
        val layoutManager_user = GridLayoutManager(activity, 1)
        recyclerView_user.layoutManager = layoutManager_user

        // Create the adapter with the dataList
        adapter_user = CustomAdapter_user(activity, R.layout.grid_item_layout_user, dataList)
        recyclerView_user.adapter = adapter_user

//        //Update the values
//        selectedItem = arguments?.getString("selected_item")
//        HousePopupList?.text=selectedItem?:"No text selected"

        return view
    }

    fun openDialog() {
        val houses_popup_dialog = Houses_Popup_Dialog()
        fragmentManager?.let { houses_popup_dialog.show(it, "Example dialog") }
    }

    // Implement the OnRoomSelectedListener method to update button text
    fun onRoomSelected(roomName: String) {
        HousePopupList?.text = roomName
    }

    private fun fetchDataFromDatabase(): List<String> {
        // Retrieve data from the database and return as a List<String>
        // Example:
        val data: MutableList<String> = ArrayList()
        data.add("Atharv")
        data.add("Juhi")
        data.add("Vishesh")
        data.add("JK")
        return data
    }


    private inner class CustomAdapter_hex(
        context: Context?, // Your existing code...
        private val resourceId: Int, dataList: List<String?>?
    ) :
        RecyclerView.Adapter<CustomAdapter_hex.ViewHolder>() {
        private var selectedItem = -1

        fun setSelectedItem(position: Int) {
            selectedItem = position
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                resourceId, parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val dataItem = dataList!![position]
            holder.itemTextView.text = dataItem

            if (position == selectedItem) {
                holder.itemLayout.setBackgroundColor(Color.parseColor("#a4b6ce"))
            } else {
                holder.itemLayout.setBackgroundColor(Color.WHITE)
            }
        }

        override fun getItemCount(): Int {
            return dataList!!.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            var itemLayout: LinearLayout = itemView.findViewById(R.id.itemLayout)
            var itemTextView: TextView = itemView.findViewById(R.id.itemTextView)

            init {
                itemLayout.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                setSelectedItem(adapterPosition)
            }
        }
    }


    private inner class CustomAdapter_user(
        context: Context?, // Your existing code...
        private val resourceId: Int, dataList: List<String?>?
    ) :
        RecyclerView.Adapter<CustomAdapter_user.ViewHolder>() {
        private var selectedItem = -1

        fun setSelectedItem(position: Int) {
            selectedItem = position
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                resourceId, parent, false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val dataItem = dataList!![position]
            holder.itemTextView.text = dataItem

            if (position == selectedItem) {
                holder.itemLayout.setBackgroundColor(Color.parseColor("#a4b6ce"))
            } else {
                holder.itemLayout.setBackgroundColor(Color.WHITE)
            }
        }

        override fun getItemCount(): Int {
            return dataList!!.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            var itemLayout: LinearLayout = itemView.findViewById(R.id.itemLayout)
            var itemTextView: TextView = itemView.findViewById(R.id.itemTextView)

            init {
                itemLayout.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                setSelectedItem(adapterPosition)
            }
        }
    }
}