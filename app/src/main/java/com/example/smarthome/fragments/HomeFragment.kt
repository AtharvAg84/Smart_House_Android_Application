package com.example.smarthome.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
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
import android.widget.PopupMenu
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smarthome.GridSpacingItemDecoration
import com.example.smarthome.R
import com.example.smarthome.RecyclerViewAdapter
import com.example.smarthome.data.Devices_Data
import com.example.smarthome.data.Smart_Devices
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import java.io.IOException
import java.util.Locale


class HomeFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    var dataOfuser: ArrayList<String> = ArrayList()
    var user_houses: ArrayList<String>? = ArrayList()

    //    Menu menu;
    var firestore: FirebaseFirestore? = null
    var popup: PopupMenu? = null
    var userImage: AppCompatImageView? = null
    var Username: TextView? = null
    var cityName: String = "Chennai"
    var House: String = ""


    var kitchenButton: AppCompatRadioButton? = null
    var liviingroomButton: AppCompatRadioButton? = null
    var bathroomButton: AppCompatRadioButton? = null
    var bedroomButton: AppCompatRadioButton? = null
    var checkedButton: AppCompatRadioButton? = null

    var user_info: ArrayList<String>? = ArrayList()

    //Map<String,ArrayList<Smart_Devices>> smart_devicesMap = new HashMap<>();

    var bundle: Bundle? = Bundle()
    var StateWeather: TextView? = null
    var TempDegree: TextView? = null
    var WeatherDescribtion: TextView? = null
    var imageWeather: AppCompatImageView? = null
    var fusedLocationClient: FusedLocationProviderClient? = null

    var MyLong: Double = 6.3758
    var MyLat: Double = 32.3424

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bundle = this.arguments
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        try {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
            }
            fusedLocationClient!!.lastLocation
                .addOnSuccessListener(
                    requireActivity()
                ) { location ->
                    if (location != null) {
                        MyLat = location.latitude
                        MyLong = location.longitude
                    }
                }
        } catch (e: Exception) {
        }
        //
        recyclerView = view.findViewById(R.id.DeviceContainer)
        layoutManager = GridLayoutManager(requireContext().applicationContext, 2)
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setHasFixedSize(true)
        recyclerView.setPadding(6, 6, 6, 6)
//
        val density = resources.displayMetrics.density
        val spacingDp = 8 // Desired column margin in dp
        val spacingPx = (spacingDp * density).toInt() // Convert dp to pixels
//
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(spacingPx))

        // Initialize the adapter and set it on the RecyclerView
        recyclerViewAdapter =
            RecyclerViewAdapter(Devices_Data().getDevicesList() as ArrayList<Smart_Devices>)
        recyclerView.adapter = this.recyclerViewAdapter

        val geocoder = Geocoder(requireContext().applicationContext, Locale.getDefault())

        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1)
            cityName =
                addresses!![0].getAddressLine(0).split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0]
            Log.i("abderrazak", " $cityName")
            weather(cityName)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        val cityName = addresses[0].getAddressLine(0)

        user_info = bundle!!.getStringArrayList("user_info")
        radioGroup = view.findViewById(R.id.radioRoom)
        rooms.add("LivingRoom")
        rooms.add("Kitchen")
        rooms.add("BedRoom")
        rooms.add("BathRoom")
        images["Clear"] = R.drawable.clear
        images["Clouds"] = R.drawable.clouds
        images["Rain"] = R.drawable.rain
        images["thunderstroms"] = R.drawable.thunderstromspng
        StateWeather = view.findViewById(R.id.StateWeather)
        TempDegree = view.findViewById(R.id.TempDegree)
        WeatherDescribtion = view.findViewById(R.id.WeatherDescribtion)
        imageWeather = view.findViewById(R.id.imageWeather)
        liviingroomButton = view.findViewById(R.id.button_1)
        kitchenButton = view.findViewById(R.id.button_2)
        bedroomButton = view.findViewById(R.id.button_3)
        bathroomButton = view.findViewById(R.id.button_4)
        changeshousesbutton = view.findViewById(R.id.houses_button_menu)
        Username = view.findViewById(R.id.UsernameTxt)
        Username?.setText(user_info!![1])


        userImage = view.findViewById(R.id.appCompatImageView)

        /*//        firestore = FirebaseFirestore.getInstance()
        //        firestore!!.collection("user")
        //            .whereEqualTo("Email", user_info!![0]).get()
        //            .addOnCompleteListener { task: Task<QuerySnapshot> ->
        //                for (doc in task.result) {
        //                    if (doc.exists()) {
        //
        //                        user_houses = doc["Houses"] as ArrayList<String>?
        //                        if (user_houses != null) {
        //                            com.example.smarthome.fragments.HomeFragment.Companion.changeshousesbutton!!?.setText(
        //                                user_houses!![0]
        //                            )
        //                            House = user_houses!![0]
        //                            Refrech(checkRoom)
        //                        }
        //
        //                        dataOfuser.add(
        //                            0, Objects.requireNonNull(
        //                                Objects.requireNonNull(
        //                                    doc["Email"]
        //                                )
        //                            ).toString()
        //                        )
        //                        dataOfuser.add(
        //                            1, Objects.requireNonNull(
        //                                Objects.requireNonNull(
        //                                    doc["ID"]
        //                                )
        //                            ).toString()
        //                        )
        //                        dataOfuser.add(
        //                            2,
        //                            Objects.requireNonNull(doc["Password"])
        //                                .toString()
        //                        )
        //                        dataOfuser.add(
        //                            3,
        //                            Objects.requireNonNull(doc["Phone"]).toString()
        //                        )
        //                        dataOfuser.add(
        //                            4,
        //                            Objects.requireNonNull(doc["Username"])
        //                                .toString()
        //                        )
        //                    }

        //                    Username?.setText(dataOfuser[4])
        //                    if (user_info!!.size == 3) {
        //                        Picasso.get().load(user_info!![2]).into(userImage)
        //                    } else {
        //                        userImage?.setImageResource(R.drawable.user_image)
        //                    }
        //
        //                }
        //            }*/

        firestore = FirebaseFirestore.getInstance()
        firestore!!.collection("user")
            .whereEqualTo("Email", user_info!![0])
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    val documents =
                        task.result?.documents // Get all documents returned in the query
                    if (documents != null && documents.isNotEmpty()) {
                        // Loop through the documents and process
                        for (doc in documents) {
                            try {
                                // Ensure that the document exists and has the necessary fields
                                user_houses = doc.get("Houses") as? ArrayList<String>

                                if (user_houses != null && user_houses!!.isNotEmpty()) {
                                    changeshousesbutton?.text =
                                        user_houses!![0]
                                    House = user_houses!![0]
                                    Refrech(checkRoom) // Make sure the Refrech() method is correctly defined elsewhere
                                }

                                // Safe access to user data fields
                                dataOfuser.apply {
                                    add(0, doc.getString("Email") ?: "")
                                    add(1, doc.getString("ID") ?: "")
                                    add(2, doc.getString("Password") ?: "")
                                    add(3, doc.getString("Phone") ?: "")
                                    add(4, doc.getString("Username") ?: "")
                                }

                                // Update the UI with the user's data
                                Username?.setText(dataOfuser[4])
                                if (user_info!!.size == 3) {
                                    Picasso.get().load(user_info!![2]).into(userImage)
                                } else {
                                    userImage?.setImageResource(R.drawable.user_image)
                                }

                            } catch (e: Exception) {
                                Log.e("Firestore", "Error reading document: ${e.message}")
                            }
                        }
                    } else {
                        Log.d("Firestore", "No matching documents found.")
                    }
                } else {
                    Log.e("Firestore", "Error getting documents: ${task.exception?.message}")
                }
            }


        // ************************ Door Test ***************
        val Door = view.findViewById<GifImageView>(R.id.Door)
        (Door.drawable as GifDrawable).stop()
        Door.setOnClickListener { view1: View ->
            if (view1.isSelected) {
                view1.isSelected = false
                Door.setBackgroundResource(R.drawable.door_background_closed)
                (Door.drawable as GifDrawable).start()
                Door.setImageResource(R.drawable.close_door_v1)
            } else {
                view1.isSelected = true
                Door.setBackgroundResource(R.drawable.door_background_opend)
                (Door.drawable as GifDrawable).start()
                Door.setImageResource(R.drawable.open_door_v1)
            }
        }


        popup = PopupMenu(requireContext(), view, Gravity.END, 0, R.style.PopupMenuStyle)
        popup!!.inflate(R.menu.houses_popup_menu)
        changeshousesbutton?.setOnClickListener(View.OnClickListener { view2: View? ->
            popup = PopupMenu(requireContext(), view2, Gravity.END, 0, R.style.PopupMenuStyle)
            popup!!.inflate(R.menu.houses_popup_menu)
            for (i in user_houses!!.indices) {
                popup!!.menu.add(1, 1, 1, user_houses!![i])
            }

            val menu = popup!!.menu
            for (i in 0 until menu.size()) {
                val menuItem = menu.getItem(i)
                val spannableString = SpannableString(menuItem.title)
                spannableString.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blue_main
                        )
                    ), 0, spannableString.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                menuItem.setTitle(spannableString)
            }
            popup!!.show()
            popup!!.setOnMenuItemClickListener { item: MenuItem ->
                changeshousesbutton?.setText(item.title)
                Refrech(checkRoom)
                false
            }
        })

        popup!!.setOnMenuItemClickListener { item: MenuItem ->
            changeshousesbutton?.setText(item.title)
            Log.i("ionia", "in clicked")
            true
        }

        radioGroup?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            Toast.makeText(context, "you pressed on : $checkedId", Toast.LENGTH_SHORT).show()
            checkedButton = view.findViewById(checkedId)
            when (checkedId) {
                R.id.button_1 -> {
                    checkRoom = 0
                    Refrech(0)
                }

                R.id.button_2 -> {
                    checkRoom = 1
                    Refrech(1)
                }

                R.id.button_3 -> {
                    checkRoom = 2
                    Refrech(2)
                }

                R.id.button_4 -> {
                    checkRoom = 3
                    Refrech(3)
                }
            }
        })

        return view
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        changeshousesbutton!!.text = menuItem.title
        Log.i("ionia", "in clicked")
        return true
    }

    fun Refrech(checkRoom: Int) {
//
//        database = FirebaseDatabase.getInstance()
//        val myRef = database!!.getReference(changeshousesbutton!!.text.toString())
//        myRef.addValueEventListener(object : ValueEventListener {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onDataChange(snapshot: DataSnapshot) {
//                smart_devicesArrayLis.clear()  // Clear existing list before adding new data
//                if (snapshot.child(rooms[checkRoom]).exists()) {
//                    var i = 0
//                    for (dataSnapshot in snapshot.child(rooms[checkRoom]).children) {
//                        val smart_device = dataSnapshot.getValue(Smart_Devices::class.java)
//                        smart_devicesArrayLis.add(i, smart_device)
//                        i++
//                    }
//                }
//                recyclerViewAdapter?.notifyDataSetChanged()  // Notify adapter of data change
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle possible database error
//                Log.e("HomeFragment", "Database Error: ${error.message}")
//            }
//        })
    }


    fun weather(CityName: String) {
        Log.i("abderrazak2", CityName)
        val queue = Volley.newRequestQueue(requireContext().applicationContext)
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=" + "$CityName" + "&appid=31d49ebece13e9a409b91f382a1edaca"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    Log.i("MyLog", "---------")
                    Log.i("MyLog", response!!)
                    val jsonObject = JSONObject(response)
                    val main = jsonObject.getJSONObject("main")
                    val weatherArray = jsonObject.getJSONArray("weather")
                    val weather = weatherArray.getJSONObject(0)
                    val tempMin = (main.getDouble("temp_min") - 273.15).toInt().toString()
                    val tempMax = (main.getDouble("temp_max") - 273.15).toInt().toString()
                    val feels_like = (main.getDouble("feels_like") - 273.15).toInt().toString()
                    val stateWeather = (weather.getString("main")) as String
                    val weatherDesc = (weather.getString("description")) as String
                    val pression = main.getInt("pressure")
                    val humidity = main.getInt("humidity")
                    StateWeather!!.text = stateWeather
                    TempDegree!!.text = "$feels_like°C"
                    WeatherDescribtion!!.text =
                        "The temperature , below $tempMin°C out cost by $tempMax°C, Humidity $humidity, Pressure $pression"
                    imageWeather!!.setImageResource(images[stateWeather]!!)
                    val img = (weather.getString("main"))

                    //                    meteoItem.image = img;
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { Log.i("MyLog", "Connection Probelm !") })
        queue.add(stringRequest)
    }

    companion object {
        var rooms: ArrayList<String> = ArrayList()
        var checkRoom: Int = 0
        private lateinit var DeviceContainer: RecyclerView
        var smart_devicesArrayLis: ArrayList<Smart_Devices?> = ArrayList()
        var database: FirebaseDatabase? = FirebaseDatabase.getInstance() // Initialize database once
        var radioGroup: RadioGroup? = null
        var changeshousesbutton: AppCompatButton? = null

        var images: MutableMap<String, Int> = HashMap()

        @SuppressLint("NonConstantResourceId")
        fun addToFirebase(smart_device: Smart_Devices) {
            // Check for null radioGroup and changeshousesbutton
            val radioButtonId = radioGroup?.checkedRadioButtonId
            val houseName = changeshousesbutton?.text?.toString()

            if (radioButtonId != null && houseName != null) {
                val room = when (radioButtonId) {
                    R.id.button_1 -> "LivingRoom"
                    R.id.button_2 -> "Kitchen"
                    R.id.button_3 -> "BedRoom"
                    R.id.button_4 -> "BathRoom"
                    else -> return // No valid radio button selected
                }

                // Use Firebase database reference to set value for the smart device
                database?.getReference(houseName)
                    ?.child(room)
                    ?.child(
                        smart_device.getPort().toString()
                    ) // Using toString() instead of java.lang.String.valueOf
                    ?.setValue(smart_device)
                    ?.addOnFailureListener { e ->
                        Log.e("Firebase", "Error adding device: ${e.message}")
                    }
            }
        }

        fun removeFromFirebase(smart_device: Smart_Devices) {
            // Check for null house name and room
            val houseName = changeshousesbutton?.text?.toString()
            val room = rooms.getOrNull(checkRoom)

            if (houseName != null && room != null) {
                // Remove the smart device from Firebase
                database?.getReference(houseName)
                    ?.child(room)
                    ?.child(smart_device.getPort().toString())
                    ?.removeValue()
                    ?.addOnFailureListener { e ->
                        Log.e("Firebase", "Error removing device: ${e.message}")
                    }
            }
        }
    }
}