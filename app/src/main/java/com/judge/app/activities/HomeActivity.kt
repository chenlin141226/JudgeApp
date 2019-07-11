package com.judge.app.activities

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.mvrx.BaseMvRxActivity
import com.judge.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseMvRxActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        nav_view.setupWithNavController(nav_host.findNavController())
    }
}
