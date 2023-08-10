package com.example.melkist.views.universal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.melkist.AddActivity
import com.example.melkist.R
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
        setTexts()
        setListener()
    }
    private fun back() {
        findNavController().popBackStack()
    }

    private fun setTexts() {
        if (requireActivity() is AddActivity) {
            binding.apply {
                txtSeekerHeader2.text = resources.getString(R.string.choosing_seeker_header2)
                txtSeekerBody.text = resources.getString(R.string.choosing_seeker_body)
                txtOwnerHeader2.text = resources.getString(R.string.choosing_owner_header2)
                txtOwnerBody.text = resources.getString(R.string.choosing_owner_body)
            }
        } else {
            binding.apply {
                txtSeekerHeader2.text = resources.getString(R.string.choosing_seeker_header2_filer)
                txtSeekerBody.text = resources.getString(R.string.choosing_seeker_body_filter)
                txtOwnerHeader2.text = resources.getString(R.string.choosing_owner_header2_filter)
                txtOwnerBody.text = resources.getString(R.string.choosing_owner_body_filter)
            }
        }
    }
    private fun setListener() {
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
}