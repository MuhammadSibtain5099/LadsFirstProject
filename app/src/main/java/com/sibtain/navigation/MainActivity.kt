package com.sibtain.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sibtain.navigation.fragments.CallFragment
import com.sibtain.navigation.fragments.ChatFragment
import com.sibtain.navigation.fragments.DashboardFragment
import com.sibtain.navigation.fragments.RecentCallFragment
import com.sibtain.navigation.interfaces.Communicator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), Communicator {
    private val dashboardFragment = DashboardFragment()
    private val chatFragment = ChatFragment()
    private val callFragment = CallFragment()
    private val recentFragment = RecentCallFragment()
    private lateinit var btnDashboard: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        replaceFragment(dashboardFragment)
        val toggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        btnDashboard = findViewById(R.id.bottom_navigation)

        btnDashboard.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.icon_Dashboard -> replaceFragment(dashboardFragment)
                R.id.icon_Call -> replaceFragment(callFragment)
                R.id.icon_Chat -> replaceFragment(chatFragment)
                R.id.Recent_Call -> replaceFragment(recentFragment)
            }
            true
        }
        fab.setOnClickListener {
            val intent = Intent(this, AddContact::class.java)
            startActivity(intent)
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    override fun passDataCom(txt: String) {
        val bundle = Bundle()
        bundle.putString("message", txt)
        val trans = this.supportFragmentManager.beginTransaction()
        val callFragment = CallFragment()
        callFragment.arguments = bundle
        trans.replace(R.id.container, callFragment).commit()
    }
}
