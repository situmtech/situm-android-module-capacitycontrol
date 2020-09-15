package com.situm.capacitycontroltestsuite.presentation.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.situm.capacitycontroltestsuite.R
import com.situm.capacitycontroltestsuite.databinding.ActivityDebugBinding
import com.situm.capacitycontroltestsuite.presentation.login.LoginActivity
import es.situm.capacitycontrol.CapacityControlService
import kotlinx.android.synthetic.main.activity_debug.*
import org.koin.android.viewmodel.ext.android.viewModel

class DebugActivity : AppCompatActivity() {

    val mViewModel: DebugViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.elevation = 0f


        val binding =
            DataBindingUtil.setContentView<ActivityDebugBinding>(this, R.layout.activity_debug)
        binding.viewModel = mViewModel
        binding.setLifecycleOwner(this)

        initObservers()

        initTabs()
    }

    private fun initTabs() {
        val tabAdapter = DebugTabAdapter(supportFragmentManager)
        tabAdapter.addTab(0, DebugTabAdapter.Tab("Setup", DebugConfigFragment()))
        tabAdapter.addTab(1, DebugTabAdapter.Tab("Logs", DebugLogFragment()))

        vpTabs.adapter = tabAdapter

        tabContainer.setupWithViewPager(vpTabs)
    }

    private fun initObservers() {
        mViewModel.checkPermissionsEvent.observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let {
                if (hasLocationPermission()) mViewModel.startPositioning()
                else requestLocationPermission()
            }
        })

        mViewModel.capacityControlService.observe(this, Observer { event ->
            val start = event?.getContentIfNotHandled()

            start?.let {
                if (it) {
                    CapacityControlService.startSocialDistanceService(this)
                } else {
                    CapacityControlService.stopSocialDistanceService(this)

                }
            }
        })

        mViewModel.completeLogoutEvent.observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        })

    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this@DebugActivity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            0
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this@DebugActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


}