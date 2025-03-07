package com.example.smarthome

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.smarthome.data.Devices_Data
import com.example.smarthome.data.Smart_Devices
import com.example.smarthome.fragments.HomeFragment
import com.google.android.material.textfield.TextInputLayout

class Dialog_Test : AppCompatDialogFragment() {
    var autoCompleteTextView: AutoCompleteTextView? = null
    var icon: ImageView? = null
    var adapterItems: ArrayAdapter<String?>? = null
    var data_devices: List<Smart_Devices> = Devices_Data().getDevicesList()
    var items: Array<String?> = arrayOfNulls(data_devices.size)
    var Description: TextInputLayout? = null
    var Port: TextInputLayout? = null
    var item_id: Int = 0
    var radioGroup: RadioGroup? = null
    var radioButton1: RadioButton? = null
    var radioButton2: RadioButton? = null
    var radioButton12: RadioButton? = null
    var radioButton22: RadioButton? = null
    var radioGroup2: RadioGroup? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity, R.style.MyDialogTheme)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.activity_dialog_test, null)
        radioButton1 = view.findViewById(R.id.radio_button_1)
        radioButton2 = view.findViewById(R.id.radio_button_2)
        radioButton12 = view.findViewById(R.id.radio_button_12)
        radioButton22 = view.findViewById(R.id.radio_button_22)
        radioGroup = view.findViewById(R.id.radioGroup)
        radioGroup2 = view.findViewById(R.id.radioGroup2)
        Description = view.findViewById(R.id.Description)
        Port = view.findViewById(R.id.Port)
        autoCompleteTextView = view.findViewById(R.id.auto_complet)
        icon = view.findViewById(R.id.ima)
        autoCompleteTextView?.setDropDownBackgroundDrawable(ColorDrawable(Color.parseColor("#d8d8d8")))

        // mouad 's part
        var i = 0
        while (i < data_devices.size) {
            items[i] = data_devices[i].name
            i++
        }
        adapterItems =
            ArrayAdapter(requireActivity().applicationContext, R.layout.list_items, items)

        autoCompleteTextView?.setAdapter(adapterItems)
        autoCompleteTextView?.setOnItemClickListener({ parent, view, position, id ->
            Log.i("mouad", "checkedbutton :" + radioGroup?.checkedRadioButtonId)

            if (data_devices[position].type != 3) {
                radioGroup?.check(if ((data_devices[position].type == 1)) R.id.radio_button_1 else R.id.radio_button_2)
                radioGroup2?.check(if ((data_devices[position].signal == 1)) R.id.radio_button_12 else R.id.radio_button_22)
                radioButton1?.isEnabled = false
                radioButton2?.isEnabled = false
                radioButton12?.isEnabled = false
                radioButton22?.isEnabled = false
                item_id = position
            } else {
                autoCompleteTextView?.setText("")
                autoCompleteTextView?.setHint("Name Of your Device")
                radioButton1?.isEnabled = true
                radioButton2?.isEnabled = true
                radioButton12?.isEnabled = true
                radioButton22?.isEnabled = true
                item_id = 5
            }
            icon?.setImageResource(data_devices[position].iconOff)
        })

        builder.setView(view)
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Add") { dialog, _ ->
                val smart_device = if (item_id != 5) {
                    data_devices[item_id]
                } else {
                    Smart_Devices(
                        autoCompleteTextView?.text.toString(),
                        item_id,
                        if (radioButton12?.isChecked == true) 1 else 0,
                        if (radioButton1?.isChecked == true) 1 else 0
                    )
                }
                smart_device.port = Port?.editText?.text.toString().toIntOrNull() ?: 0
                smart_device.description = Description?.editText?.text.toString()
                smart_device.iconOff = data_devices[item_id].iconOff
                try {
                    smart_device.iconOn = data_devices[item_id].iconOn
                } catch (e: Exception) {
                    // handle the exception as needed
                }
                HomeFragment.addToFirebase(smart_device)
            }
        return builder.create()
    }
}
