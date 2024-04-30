package com.example.adminblinkitclone.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.adminblinkitclone.databinding.FragmentOTPBinding
import com.example.adminblinkitclone.viewModels.AuthViewModel
import com.example.userblinkitclone.models.Admin
import kotlinx.coroutines.launch


class OTPFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentOTPBinding
    private lateinit var userNumber: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOTPBinding.inflate(layoutInflater)

        getUserNumber()

        customizeEnteringOTP()
        sendOTP()
        onLoginClick()
        onBackButtonClicked()
        return binding.root
    }

    private fun getUserNumber() {
        val bundle = arguments
        userNumber = bundle?.getString("number").toString()
        binding.tvUserNumber.text = userNumber
    }

    private fun customizeEnteringOTP() {
        val editText = arrayOf(
            binding.etOtp1,
            binding.etOtp2,
            binding.etOtp3,
            binding.etOtp4,
            binding.etOtp5,
            binding.etOtp6
        )
        for (i in editText.indices) {
            editText[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        if (i < editText.size - 1) {

                            editText[i + 1].requestFocus()

                        }

                    } else if (s?.length == 0) {
                        if (i > 0) {

                            editText[i - 1].requestFocus()
                        }
                    } else if (i == editText.size - 1 && s?.length ?: 0 > 1) {
                        s?.delete(1, s.length)
                        editText[i].clearFocus()
                    }

                }

            })
        }
    }

    private fun sendOTP() {


        Utils.showDialog(requireContext(), "Sending OTP...")

        viewModel.apply {
            viewModel.sendOTP(userNumber, requireActivity())//code in AuthViewModel
            lifecycleScope.launch {
                otpSent.collect {
                    if (it) {
                        Utils.hideDialog()
                        Utils.showToast(requireContext(), "OTP Sent")
                    }
                }
            }

        }

    }

    private fun verifyOTP(otp: String) {

        val user =
            Admin(uid = null, userPhoneNumber = userNumber, userAddress = null)
        viewModel.signInWithPhoneAuthCredential(otp, userNumber, user)
        lifecycleScope.launch {
            viewModel.isSignedInSuccessfully.collect {
                if (it) {
                    Utils.hideDialog()
                    Utils.showToast(requireContext(), "Logged In")
                    startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun onLoginClick() {
        binding.buttonLogin.setOnClickListener {
            Utils.showDialog(requireContext(), "Signing you in...")
            val editText = arrayOf(
                binding.etOtp1,
                binding.etOtp2,
                binding.etOtp3,
                binding.etOtp4,
                binding.etOtp5,
                binding.etOtp6
            )
            val otp = editText.joinToString("") { it.text.toString() }

            if (otp.length < editText.size) {
                Utils.showToast(requireContext(), "Please enter correct OTP")
            } else {
                editText.forEach {
                    it.text?.clear()
                    it.clearFocus()
                }
                verifyOTP(otp)
            }
        }
    }
    private fun onBackButtonClicked() {
        binding.toolbar.setNavigationOnClickListener {

            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment)
        }
    }


}