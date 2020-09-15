package com.situm.capacitycontroltestsuite.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.situm.capacitycontroltestsuite.R
import com.situm.capacitycontroltestsuite.databinding.FragmentDebugConfigBinding
import es.situm.sdk.location.OutdoorLocationOptions
import kotlinx.android.synthetic.main.fragment_debug_config.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DebugConfigFragment : Fragment() {

    private val mViewModel: DebugViewModel by sharedViewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentDebugConfigBinding>(
                inflater, R.layout.fragment_debug_config, container, false)

        binding.viewModel = mViewModel
        binding.setLifecycleOwner(activity)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initObservers()
    }


    private fun initObservers() {
        mViewModel.buildingDetectorModes.observe(this, Observer { modes ->
            modes?.let {
                //TODO: This could be done better
                ArrayAdapter<OutdoorLocationOptions.BuildingDetector>(
                        activity!!,
                        android.R.layout.simple_spinner_item,
                        it
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spBuildingDetectorMode.adapter = adapter
                }
            }
        })

        spBuildingDetectorMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModel.updateSelectedBulidingDetector(position)
            }
        }
    }
}