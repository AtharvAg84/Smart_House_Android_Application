package com.example.smarthome

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.smarthome.fragments.HomeFragment
import com.example.smarthome.fragments.ManageFragment
import com.example.smarthome.fragments.SettingsFragment
import com.example.smarthome.fragments.StatisticsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainDashBoard : AppCompatActivity() {
    private var user_info: ArrayList<String>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dash_board_active)
        user_info = intent.getStringArrayListExtra("FromMain")
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val floatingActionBar = findViewById<FloatingActionButton>(R.id.floatingactionbutton)
        floatingActionBar.setOnClickListener { openDialog() }

        bottomNavigationView.selectedItemId = R.id.home
        replaceFragment(HomeFragment(), true)

        bottomNavigationView.setOnItemSelectedListener { item ->
            val iteq = item.itemId
            Log.i("Syndra", "item is : $iteq")
            item.setChecked(true)
            val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
            when (item.itemId) {
                R.id.home -> {
                    Log.i("lemmetry", "tried")
                    floatingActionBar.visibility = View.VISIBLE
                    if (currentFragment !is HomeFragment) {
                        val myHomeFragment: HomeFragment = HomeFragment()
                        replaceFragment(HomeFragment(), false)
                    }
                }

                R.id.statistics -> {
                    val statisticsFragment: StatisticsFragment = StatisticsFragment()
                    floatingActionBar.visibility = View.INVISIBLE
                    if ((currentFragment is ManageFragment) || (currentFragment is SettingsFragment)) {
                        replaceFragment(StatisticsFragment(), false)
                    } else if (currentFragment !is StatisticsFragment) {
                        replaceFragment(StatisticsFragment(), true)
                    }
                }

                R.id.manage -> {
                    val manageFragment: ManageFragment = ManageFragment()
                    floatingActionBar.visibility = View.INVISIBLE
                    if ((currentFragment is SettingsFragment)) {
                        replaceFragment(ManageFragment(), false)
                    } else if (currentFragment !is ManageFragment) {
                        replaceFragment(ManageFragment(), true)
                    }
                }

                R.id.settings -> {
                    val settingsFragment: SettingsFragment = SettingsFragment()
                    floatingActionBar.visibility = View.INVISIBLE

                    if (currentFragment !is SettingsFragment) {
                        replaceFragment(SettingsFragment(), true)
                    }
                }

                else -> {
//                    val myHomeFragment: HomeFragment = HomeFragment()
//                    floatingActionBar.visibility = View.VISIBLE
//
//                    replaceFragment(HomeFragment(), false)
                }
            }
            true
        }
    }

    private fun openDialog() {
        val dialog_test = Dialog_Test()
        dialog_test.show(supportFragmentManager, "Example dialog")
    }

    private fun replaceFragment(fragment: Fragment, trans: Boolean) {
        // Get FragmentManager to manage the fragment transaction
        val fragmentManager = supportFragmentManager

        // Begin a new transaction
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Bundle to pass data to the fragment
        val bundle = Bundle()

        // Passing the user_info array list to the fragment
        bundle.putStringArrayList("user_info", user_info)
        fragment.arguments = bundle

        // Set custom animations for fragment transition if required
        if (trans) {
            fragmentTransaction.setCustomAnimations(
                R.anim.silde_in_right,   // animation when fragment enters
                R.anim.slide_out_left    // animation when fragment exits
            )
        } else {
            fragmentTransaction.setCustomAnimations(
                R.anim.slide_in_left,    // animation when fragment enters
                R.anim.slide_out_right   // animation when fragment exits
            )
        }

        // Replace the current fragment with the new one in the container (frame_layout)
        fragmentTransaction.replace(R.id.frame_layout, fragment)

        // Optionally, add the transaction to the back stack to allow the user to go back
        fragmentTransaction.addToBackStack(null)  // Pass null to use the default back stack name

        // Commit the transaction
        fragmentTransaction.commit()
    }
}