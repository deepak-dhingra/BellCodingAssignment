package com.challenge.tweetsviewerapp.baseclasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<B : ViewDataBinding, V : AndroidViewModel> : Fragment() {

    var fragmentBinding: B? = null
    var mainViewModel: V? = null

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): Class<V>

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        mainViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
                .create(getViewModel())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return fragmentBinding?.root
    }
}