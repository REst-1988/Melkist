package com.example.melkist.views.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.melkist.databinding.FragAddP2DetailsForSeekerBinding
import com.example.melkist.viewmodels.AddItemViewModel

class AddP4Location : Fragment() {

    private lateinit var binding: FragAddP2DetailsForSeekerBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragAddP2DetailsForSeekerBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP4Location
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


    fun onChoosingLocation() {
        TODO()
    }

    fun showLocationText(): String {
        TODO("Show location text   &    hide choose location text")
    }
}