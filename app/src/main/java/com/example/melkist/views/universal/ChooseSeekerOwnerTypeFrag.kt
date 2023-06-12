package com.example.melkist.views.universal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.melkist.databinding.FragChooseSeekerOwnerTypeBinding
import com.example.melkist.utils.*

class ChooseSeekerOwnerTypeFrag : Fragment() {

    private lateinit var binding: FragChooseSeekerOwnerTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragChooseSeekerOwnerTypeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutSeeker.setOnClickListener{
            setFragmentResult(ITEM_TYPE_KEY, bundleOf(DATA to SEEKER_ITEM_TYPE))
            back()
        }

        binding.layoutOwner.setOnClickListener{
            setFragmentResult(ITEM_TYPE_KEY, bundleOf(DATA to OWNER_ITEM_TYPE))
            back()
        }

        binding.ibtnBack.setOnClickListener {
            back()
        }
    }

    /******************* binding commands **************************/
    fun back() {
        findNavController().popBackStack()
    }
}