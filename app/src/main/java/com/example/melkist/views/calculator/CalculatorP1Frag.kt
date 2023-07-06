package com.example.melkist.views.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragCalculatorP1Binding
import com.example.melkist.viewmodels.MainViewModel

class CalculatorP1Frag : Fragment() {

    private lateinit var binding: FragCalculatorP1Binding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragCalculatorP1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rl1.setOnClickListener{
            findNavController().navigate(
                R.id.action_navigation_calculator_to_calculatorMortgageRentFrag
            )
        }
        binding.rl2.setOnClickListener{
            // TODO
        }
        binding.rl3.setOnClickListener {
            // TODO
        }
    }
}