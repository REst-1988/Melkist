package com.example.melkist.views.login.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.melkist.R
import com.example.melkist.databinding.FragmentChoosingRealEstateOrUserBinding


class ChoosingRealEstateOrUserFragment : Fragment() {

    lateinit var binding: FragmentChoosingRealEstateOrUserBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoosingRealEstateOrUserBinding.inflate(inflater)
        return binding.root
    }


}