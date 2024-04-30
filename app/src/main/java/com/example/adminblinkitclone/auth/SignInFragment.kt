package com.example.adminblinkitclone.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.adminblinkitclone.R
import com.example.adminblinkitclone.Utils
import com.example.adminblinkitclone.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(layoutInflater)

        getUserNumber()
        onContinueButtonClick()

        return binding.root
    }


    private fun getUserNumber() {

        binding.etUserNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                number: CharSequence?, start: Int, count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                number: CharSequence?, start: Int, before: Int, count: Int
            ) {

                val length = number?.length
                if (length == 10) {
                    binding.buttonContinue.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.green
                        )
                    )
                } else {
                    binding.buttonContinue.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), R.color.grey
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length > 10) {
                        val trimmedText = it.toString().substring(0, 10)
                        binding.etUserNumber.setText(trimmedText)
                        binding.etUserNumber.setSelection(trimmedText.length)
                    }
                }
            }


        })
    }

    private fun onContinueButtonClick() {

        binding.buttonContinue.setOnClickListener {
            val number = binding.etUserNumber.text.toString()

            if (number.isEmpty() || number.length != 10) {

                Utils.showToast(requireContext(), "Please enter valid phone number")
            } else {
                val bundle = Bundle()
                bundle.putString("number", number)
                findNavController().navigate(R.id.action_signInFragment_to_OTPFragment, bundle)
            }
        }
    }


}