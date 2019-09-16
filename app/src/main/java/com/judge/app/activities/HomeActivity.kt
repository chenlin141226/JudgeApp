package com.judge.app.activities

import android.os.Bundle
import androidx.core.view.get
import com.judge.R
import com.judge.app.core.BaseActivity
import com.judge.extensions.setSelectItem
import com.judge.extensions.setupWithNavController
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        signImageView.setOnClickListener {
//            Log.e("TAG","success")
//            startActivity(Intent(this,SignActivity::class.java))
//        }
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()

    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        bottom_nav.apply {
            itemIconTintList = null
            setSelectItem(bottom_nav.menu[0])
        }
        val navGraphIds = listOf(R.navigation.home, R.navigation.judge,R.navigation.topic, R.navigation.market, R.navigation.mine)

        // Setup the bottom navigation view with a list of navigation graphs
        bottom_nav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        /*controller.observe(this, Observer { navController ->
            //setupActionBarWithNavController(navController)
        })*/
        //currentNavController = controller
    }

}
