package com.example.melkist.views.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.melkist.R
import com.example.melkist.databinding.FragProfilePicBinding
import com.example.melkist.viewmodels.ProfilePicViewModel


class ProfilePicFrag : Fragment() {

    private val viewModel: ProfilePicViewModel by viewModels()
    lateinit var binding: FragProfilePicBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfilePicBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfilePicFrag
        }
        return binding.root
    }

    /*************************** binding **********************************/
    fun back() {
        // TODO
    }

    fun isShowBackBtn(): Boolean{
        return true // TODO
    }

    fun onSend () {
        // TODO
    }

    fun onProfilePicClick() {
        // TODO
    }
}