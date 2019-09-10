package com.challenge.tweetsviewerapp.ui.activities

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.baseclasses.BaseActivity
import com.challenge.tweetsviewerapp.databinding.ActivityHomeBinding
import com.challenge.tweetsviewerapp.viewModel.MainViewModel

class HomeActivity : BaseActivity<ActivityHomeBinding, MainViewModel>() {
    override fun getLayoutId(): Int = R.layout.activity_home

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding?.toolBar)
        val host = NavHostFragment.create(R.navigation.nav_graph)
        supportFragmentManager.beginTransaction().replace(R.id.container, host)
            .setPrimaryNavigationFragment(host).commit()
    }
}