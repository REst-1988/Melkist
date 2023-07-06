package com.example.melkist.views.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragProfileP1Binding
import com.example.melkist.interfaces.Interaction
import com.example.melkist.viewmodels.MainViewModel

class ProfileP1Frag : Fragment() {

    private var _binding: FragProfileP1Binding? = null
    private val binding get() = _binding!!
    private var interaction: Interaction? = null
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragProfileP1Binding.inflate(inflater, container, false)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfileP1Frag
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Interaction) {
            interaction = context as Interaction
        } else {
            throw RuntimeException(
                context.toString() + " must implement OnFragmentInteractionListener"
            )
        }
    }
    // This is for hide and unhiding bottom nav bar
    override fun onDetach() {
        super.onDetach()
        interaction = null
    }

    override fun onResume() {
        super.onResume()
        interaction?.changBottomNavViewVisibility(View.VISIBLE)
    }

    /******************** binding methods ***************************/

    fun getUserImage(): String {
        var url = ""
        (activity as MainActivity).user?.apply {
            url = profilePic?:""
        }
        return url
    }
    fun userNameText(): String {
        var user = ""
        (activity as MainActivity).user?.apply {
            user = String.format("%s %s", firstName, lastName)
        }
        return user
    }

    fun onEditUserClick() {
        // TODO
    }

    fun onAlertsClick() {
        findNavController()
            .navigate(R.id.action_navigation_profle_to_profileAlertsFrag)
        interaction?.changBottomNavViewVisibility(View.GONE)
    }

    fun onMyFilesClick() {
        //TODO()
    }

    fun onManageTeamClick() {
        //TODO()
    }

    fun onAiClick() {
        //TODO("show dialog says soon will run")
    }

    fun onStatisticsClick() {
        //TODO("show a dialog says cooming soon")
    }

    fun onOptionsClick() {
        //TODO()
    }

    fun onContactUsClick() {
        // TODO
    }

    fun onExitClick() {
        //TODO("sign out of system")
    }
}