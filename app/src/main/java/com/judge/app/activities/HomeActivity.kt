package com.judge.app.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.airbnb.mvrx.BaseMvRxActivity
import com.judge.R

class HomeActivity : BaseMvRxActivity() {
    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
    }
    private val bottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.nav_view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottomNavigationView.setupWithNavController(navHostFragment.navController)
    }
}
