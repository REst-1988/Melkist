package com.example.melkist.views.profile.ai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragProfileAiP1Binding


class ProfileAiP1 : Fragment() {

    private lateinit var binding: FragProfileAiP1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileAiP1Binding.inflate(inflater)
        binding.fragment = this@ProfileAiP1
        return binding.root
    }

    /***************** binding methods **********************/
    fun back() {
        findNavController().popBackStack()
    }

    fun aiIntro() {
        findNavController().navigate(
            R.id.action_profileAiP1_to_profileAiSuggestionsFrag
        )
    }

    fun aiSimolar() {
        // TODO
    }

    fun aiCoworker () {
        // TODO
    }
}