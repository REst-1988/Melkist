package com.example.melkist.views.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.melkist.databinding.FragAddP7DetailsOwnerBinding
import com.example.melkist.viewmodels.AddItemViewModel


class AddP7DetailsOwnerFrag : Fragment() {
    private lateinit var binding: FragAddP7DetailsOwnerBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragAddP7DetailsOwnerBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP7DetailsOwnerFrag
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
        TODO()
    }

    fun onChoosingMeasure() {
        TODO("Show dialog for getting measurements")
    }

    fun showMeasureText(): String {
        TODO("Show measurement text")
    }

    fun onChoosingRoomCount(){
        TODO("Show dialog for getting room count")
    }

    fun showRoomCoText(): String {
        TODO("Show RoomCo text")
    }

    fun onChoosingPrice(){
        TODO("Show dialog for getting price")
    }

    fun showPriceText(): String {
        TODO("Show Price text")
    }
}