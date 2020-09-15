package com.situm.capacitycontroltestsuite.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.situm.capacitycontroltestsuite.R
import com.situm.capacitycontroltestsuite.databinding.ActivityLoginBinding
import com.situm.capacitycontroltestsuite.presentation.main.DebugActivity
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val mViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        binding.viewModel = mViewModel
        binding.setLifecycleOwner(this)

        initObservers()
    }

    private fun initObservers() {
        mViewModel.navigationEvent.observe(this, Observer { event ->
            event?.getContentIfNotHandled()?.let { navigate ->
                if (navigate) {
                    val intent = Intent(this@LoginActivity, DebugActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent);
                }
            }
        })
    }
}