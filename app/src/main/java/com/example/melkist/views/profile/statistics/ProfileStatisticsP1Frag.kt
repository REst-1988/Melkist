package com.example.melkist.views.profile.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragProfileStatisticsP1Binding
import com.example.melkist.utils.showDialogWithMessage

class ProfileStatisticsP1Frag : Fragment() {

    private lateinit var binding: FragProfileStatisticsP1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileStatisticsP1Binding.inflate(layoutInflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@ProfileStatisticsP1Frag
        }
        return binding.root
    }

    /***************** binding methods ********************/
    fun back() {
        findNavController().popBackStack()
    }

    fun statStaff() {
        findNavController().navigate(
            R.id.action_profileStatisticsP1Frag_to_profileStatisticsStaffFrag
        )
    }

    fun staffImprovement() {
        findNavController().navigate(
            R.id.action_profileStatisticsP1Frag_to_profileStatMemberImprovementFrag
        )
    }

    fun workPeakClicked() {
        findNavController().navigate(
            R.id.action_profileStatisticsP1Frag_to_profileStatWorkPeaksFragm
        )
    }

    fun compareRealEstatesClicked() {
        showDialogWithMessage(
            requireContext(),
            resources.getString(R.string.invalid_permission)
        ) { d, _ ->
            d.dismiss()
        }
    }
}