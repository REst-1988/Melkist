package com.example.melkist.views.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.melkist.databinding.FragAddP6DetailsSeekerBinding
import com.example.melkist.viewmodels.AddItemViewModel

class AddP6DetailsSeekerFrag : Fragment() {
    private lateinit var binding: FragAddP6DetailsSeekerBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragAddP6DetailsSeekerBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP6DetailsSeekerFrag
        }
        return binding.root
    }

    /************** binding commands **********************/
    fun back() {
        TODO()
    }

    fun cancel() {
        TODO()
    }

    fun onCommit() {

    }
}