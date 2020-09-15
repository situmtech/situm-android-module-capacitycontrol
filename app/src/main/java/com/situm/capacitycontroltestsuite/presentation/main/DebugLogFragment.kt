package com.situm.capacitycontroltestsuite.presentation.main

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.situm.capacitycontroltestsuite.R
import com.situm.capacitycontroltestsuite.databinding.FragmentDebugLogBinding
import kotlinx.android.synthetic.main.fragment_debug_log.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class DebugLogFragment : Fragment() {

    private val mViewModel: DebugViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentDebugLogBinding>(
                inflater, R.layout.fragment_debug_log, container, false)

        binding.setLifecycleOwner(activity)
        binding.viewModel = mViewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvLogView.movementMethod = ScrollingMovementMethod()
    }
}