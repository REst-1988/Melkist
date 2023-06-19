package com.example.melkist.views.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.melkist.MainActivity
import com.example.melkist.databinding.FragProfileP1Binding
import com.example.melkist.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragProfileP1Binding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragProfileP1Binding.inflate(inflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfileFragment
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /******************** binding methods ***************************/
    fun userNameText(): String {
        val user = (activity as MainActivity).user
        return String.format("%s %s", user.firstName, user.lastName)
    }

    fun onAlertsClick() {
        TODO("this is next part")
    }

    fun onMyFilesClick() {
        TODO()
    }

    fun onManageTeamClick() {
        TODO()
    }
    fun onAiClick() {
        TODO("show dialog says soon will run")
    }

    fun onStatisticsClick () {
        TODO("show a dialog says cooming soon")
    }

    fun onOptionsClick() {
        TODO()
    }

    fun onExitClick () {
        TODO("sign out of system")
    }
}