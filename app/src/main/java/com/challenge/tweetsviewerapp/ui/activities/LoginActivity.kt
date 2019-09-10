package com.challenge.tweetsviewerapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.challenge.tweetsviewerapp.R
import com.challenge.tweetsviewerapp.baseclasses.BaseActivity
import com.challenge.tweetsviewerapp.databinding.ActivityLoginBinding
import com.challenge.tweetsviewerapp.viewModel.MainViewModel
import com.twitter.sdk.android.core.*

class LoginActivity : BaseActivity<ActivityLoginBinding, MainViewModel>() {
    override fun getLayoutId(): Int = R.layout.activity_login

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (TwitterCore.getInstance().sessionManager.activeSession == null) {
            twitterLogin()
        } else {
            navigateToHome()
        }
    }

    fun twitterLogin() {
        binding?.btnLogin?.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                binding?.btnLogin?.text = "LoggedIn"
                viewModel?.getTwitterSuccess(result)
                navigateToHome()
            }

            override fun failure(exception: TwitterException?) {
                Log.e("TWITTER EXCEPTION", exception?.stackTrace.toString())
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding?.btnLogin?.onActivityResult(requestCode, resultCode, data)
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}