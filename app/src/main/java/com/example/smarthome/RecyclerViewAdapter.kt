package com.example.smarthome

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.data.Smart_Devices
import com.example.smarthome.fragments.HomeFragment


class RecyclerViewAdapter(var smartDevices: ArrayList<Smart_Devices>) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.device, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val device = smartDevices[position]  // No need for null checks
        holder.textView.text = device.name
        holder.Descreption.text = device.description

        // Set the icon based on the state
        if (device.state != 0) {
            holder.icon.setImageResource(device.iconOn)
        } else {
            holder.icon.setImageResource(device.iconOff)
        }

        // Toggle visibility of the switch based on device type
        if (device.type == 0) {
            holder.State.visibility = View.INVISIBLE
        } else {
            holder.State.visibility = View.VISIBLE
            holder.State.isChecked = device.state == 1
        }

        // Apply animation to the item
        val animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_house_cards)
        holder.itemView.startAnimation(animation)
    }

    override fun getItemCount(): Int = smartDevices.size

    @Suppress("DEPRECATION")
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Descreption: TextView = itemView.findViewById(R.id.Descreption)
        var textView: TextView = itemView.findViewById(R.id.textView)
        var icon: ImageView = itemView.findViewById(R.id.imageView)
        var State: SwitchCompat = itemView.findViewById(R.id.State)
        var optiondots: AppCompatButton = itemView.findViewById(R.id.threedots)
        var constraintLayout: ConstraintLayout = itemView.findViewById(R.id.devicelayout)

        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                val device = smartDevices[pos]
                if (device.type != 0) {  // Only toggle if the device type allows
                    device.state = if (device.state == 1) 0 else 1
                    HomeFragment.addToFirebase(device)  // Update device in Firebase
                    notifyItemChanged(pos)  // Update the item in the RecyclerView
                }
            }
            constraintLayout.setOnClickListener { view ->
                view.isSelected = !view.isSelected
                if (view.isSelected) {
                    // Apply a color filter using PorterDuffColorFilter
                    view.background.colorFilter =
                        PorterDuffColorFilter(Color.parseColor("#70bf73"), PorterDuff.Mode.SRC_ATOP)
                } else {
                    // Clear the color filter
                    view.background.colorFilter = null
                }
            }


            optiondots.setOnClickListener {
                val popup = PopupMenu(
                    itemView.context,
                    itemView.findViewById(R.id.threedots),
                    Gravity.END,
                    0,
                    R.style.PopupMenuStyle
                )
                popup.inflate(R.menu.houses_threedots)

                // Modify the PopupMenu items if needed
                val menu = popup.menu
                for (i in 0 until menu.size()) {
                    val menuItem = menu.getItem(i)
                    val spannableString = SpannableString(menuItem.title)
                    spannableString.setSpan(
                        ForegroundColorSpan(Color.parseColor("#ff0000")),
                        0,
                        spannableString.length,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                    )
                    menuItem.title = spannableString
                }

                popup.show()
                popup.setOnMenuItemClickListener {
                    val device = smartDevices[adapterPosition]
                    HomeFragment.removeFromFirebase(device)  // Remove device from Firebase
                    notifyItemRemoved(adapterPosition)  // Remove the item from the RecyclerView
                    false
                }
            }
        }
    }
}

