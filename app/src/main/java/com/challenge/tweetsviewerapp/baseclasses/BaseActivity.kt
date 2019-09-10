package com.challenge.tweetsviewerapp.baseclasses

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity<B : ViewDataBinding, V : AndroidViewModel> : AppCompatActivity() {

    var binding: B? = null
    var viewModel: V? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): Class<V>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        supportActionBar?.hide()
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(getViewModel())
    }
}