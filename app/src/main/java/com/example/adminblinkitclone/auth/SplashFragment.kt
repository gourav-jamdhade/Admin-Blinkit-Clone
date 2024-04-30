package com.example.adminblinkitclone.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.Utils
import com.example.adminblinkitclone.activity.AdminMainActivity
import com.example.adminblinkitclone.databinding.FragmentSplashBinding
import com.example.adminblinkitclone.viewModels.AuthViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()

    private  val binding: FragmentSplashBinding by lazy{
        FragmentSplashBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler(Looper.getMainLooper()).postDelayed({

            lifecycleScope.launch {
                fetchRemoteConfigValues()
            }

        }, 3000)
        return binding.root
    }

    private fun fetchRemoteConfigValues() {
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600) // Set minimum fetch interval
            .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val forceLoginRequired =
                        firebaseRemoteConfig.getBoolean("force_login_required")
                    if (forceLoginRequired) {
                        // Prompt user to log in again
                        findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                    } else {
                        // Proceed with your existing logic
                        checkCurrentUser()
                    }
                } else {
                    // Handle error
                    Log.e("TAG", "Error fetching Remote Config values: ${task.exception}")
                    // Proceed with your existing logic even if Remote Config fetch fails
                    checkCurrentUser()
                }
            }
    }

    private fun checkCurrentUser() {
        lifecycleScope.launch {
            viewModel.isACurrentUser.collect { isCurrentUser ->
                if (isCurrentUser) {
                    startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                    requireActivity().finish()
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                }
            }
        }
    }


}