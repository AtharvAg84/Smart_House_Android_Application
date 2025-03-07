package com.example.smarthome.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.smarthome.R
import com.example.smarthome.data.Smart_Devices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LivingRoomFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    var Devcontrainer: GridLayout? = null
    var optiondots: AppCompatButton? = null
    var textView: TextView? = null
    var Descreption: TextView? = null
    var State: SwitchCompat? = null
    var icon: ImageView? = null
    var ListOutputs: ArrayList<Int> = ArrayList()
    var HouseID: String = ""

    var database: FirebaseDatabase? = null
    var devicesList: ArrayList<Smart_Devices> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_living_room, container, false)
        val bundle = this.arguments

        //HouseID=bundle.getString("HouseID");
        //HouseID=getArguments().getString("HouseID");
        //Log.i("house_id","House is out : "+HouseID);
        database = FirebaseDatabase.getInstance()

        //        Devcontrainer = view.findViewById(R.id.DeviceContainer);
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.leftMargin = 8
        layoutParams.topMargin = 8
        layoutParams.bottomMargin = 8
        val myArrayAdapter = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_list_item_1,
            devicesList
        )

        // Apply the layoutParams to the child view
//        Devcontrainer.setLayoutParams(layoutParams);
        Devcontrainer!!.setOnClickListener { v ->
            for (port in ListOutputs) {
                val ViewOut = v.findViewById<View>(port)
                ViewOut.setOnClickListener { vo ->
                    val switchCompat = vo.findViewById<SwitchCompat>(R.id.State)
                    switchCompat.isChecked = !switchCompat.isChecked
                    Log.i("seraphine", " ID : --" + switchCompat.isChecked)
                    database!!.getReference("hjhjh").child("LivingRoom").child(port.toString())
                        .child("state").setValue(if ((switchCompat.isChecked == true)) 1 else 0)
                }
            }
        }
        Refresh()
        return view
    }

    fun addToFirebase(smart_device: Smart_Devices) {
        val myRef = database!!.getReference("hjhjh")
        myRef.child("LivingRoom").child(java.lang.String.valueOf(smart_device.getPort()))
            .setValue(smart_device)
        Refresh()
    }

    val viewSelected: Unit
        get() {
            //TODO
        }

    fun AddDevice(smart_device: Smart_Devices) {
        val viewdevice = layoutInflater.inflate(R.layout.device, null)

        //Log.i("house_id","House is in : "+HouseID);

        //Log.i("house_id","House is in here  : "+HouseID);
        viewdevice.setId(smart_device.getPort() as Int)
        textView = viewdevice.findViewById(R.id.textView)
        Descreption = viewdevice.findViewById(R.id.Descreption)
        textView?.setText(smart_device.name)
        icon = viewdevice.findViewById(R.id.imageView)
        State = viewdevice.findViewById(R.id.State)
        Descreption?.setText(smart_device.description)
        if (smart_device.state == 0) {
            viewdevice.setBackgroundColor(Color.parseColor("#a84e32"))
        } else {
            viewdevice.setBackgroundColor(Color.parseColor("#4dab4b"))
        }
        //if(smart_device.getIcon()!=0){
        icon?.setImageResource(smart_device.iconOff)
        if (smart_device.type == 0) {
            State?.setVisibility(View.INVISIBLE)
        }

        State = viewdevice.findViewById(R.id.State)
        optiondots = viewdevice.findViewById(R.id.threedots)
        optiondots?.setOnClickListener(View.OnClickListener { view ->
            val popup = PopupMenu(requireContext(), view, Gravity.END, 0, R.style.PopupMenuStyle)
            popup.inflate(R.menu.houses_threedots)

            // Get the MenuItems from the PopupMenu
            val menu = popup.menu
            for (i in 0 until menu.size()) {
                val menuItem = menu.getItem(i)
                // Set the desired text color for the MenuItem
                val spannableString = SpannableString(menuItem.title)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor("#ff0000")),
                    0,
                    spannableString.length,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                menuItem.setTitle(spannableString)
            }
            popup.show()
        })

        Devcontrainer!!.addView(viewdevice)
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        return true
    }

    fun Refresh() {
        val myRef = database!!.getReference("hjhjh").child("LivingRoom")

        // Read from the database
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Devcontrainer!!.removeAllViews()
                var i = 0
                for (dataSnapshot in snapshot.children) {
                    val smart_device = dataSnapshot.getValue(
                        Smart_Devices::class.java
                    )
                    AddDevice(smart_device!!)
                    if (smart_device.type == 1) {
                        ListOutputs.add(i, smart_device.getPort() as Int)
                        i++
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    companion object {
        fun newInstance(): LivingRoomFragment {
            return LivingRoomFragment()
        }
    }
}