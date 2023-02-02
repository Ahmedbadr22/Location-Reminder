package com.example.location.reminder.authentication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.location.reminder.databinding.FragmentMainAuthBinding

class MainAuthFragment : Fragment() {
    private lateinit var binding: FragmentMainAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainAuthBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            fragment = this@MainAuthFragment
        }
    }

    fun navigateToLoginMethodFragment() {
        val navAction = MainAuthFragmentDirections.actionMainAuthFragmentToLoginMethodFragment()
        findNavController().navigate(navAction)
    }
}