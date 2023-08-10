package com.example.melkist.views.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.melkist.LoginActivity
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.data.Ds
import com.example.melkist.databinding.DialogContactUsBinding
import com.example.melkist.databinding.FragProfileP1Binding
import com.example.melkist.interfaces.Interaction
import com.example.melkist.models.Roles
import com.example.melkist.utils.showDialogWith2Actions
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel
import kotlinx.coroutines.launch


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

    private fun extiConfirm() {
        startActivity(
            Intent(requireActivity(), LoginActivity::class.java)
        )
        lifecycleScope.launch {
            Ds.getDataStore(requireContext()).emptyPreferences()
        }
        requireActivity().finish()
    }

    /******************** binding methods ***************************/

    fun getUserImage(): String {
        var url = ""
        (activity as MainActivity).user?.apply {
            url = profilePic ?: ""
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
        findNavController().navigate(R.id.action_navigation_profle_to_profilePicFrag2)
        interaction?.changBottomNavViewVisibility(View.GONE)
    }

    fun onAlertsClick() {
        findNavController()
            .navigate(R.id.action_navigation_profle_to_profileAlertsFrag)
        interaction?.changBottomNavViewVisibility(View.GONE)
    }

    fun onMyFilesClick() {
        findNavController()
            .navigate(R.id.action_navigation_profle_to_profileMyFilesFrag)
        interaction?.changBottomNavViewVisibility(View.GONE)
    }

    fun onCooperationClick() {
        findNavController()
            .navigate(R.id.action_navigation_profle_to_profileCooperationFrag)
        interaction?.changBottomNavViewVisibility(View.GONE)
    }

    fun onManageTeamClick() {
        (activity as MainActivity).user?.apply {
            if (roleId == Roles().manager.id || roleId == Roles().supervisor.id) {
                findNavController().navigate(
                    R.id.action_navigation_profle_to_profileManageTeamFrag
                )
                interaction?.changBottomNavViewVisibility(View.GONE)
            } else {
                showToast(
                    requireContext(),
                    resources.getString(R.string.no_access_for_this)
                )
            }
        }
    }

    fun onAiClick() {
        showDialogWithMessage(
            requireContext(),
            resources.getString(R.string.invalid_permission)
        ) { d, _ ->
            d.dismiss()
        }
    }

    fun onStatisticsClick() {
        showDialogWithMessage(
            requireContext(),
            resources.getString(R.string.invalid_permission)
        ) { d, _ ->
            d.dismiss()
        }
    }

    fun onOptionsClick() {
        findNavController().navigate(
            R.id.action_navigation_profle_to_profileOptionsFrag
        )
        interaction?.changBottomNavViewVisibility(View.GONE)
    }

    fun onContactUsClick() {
        val binding =
            DialogContactUsBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog
            .Builder(context)
            .create()
        dialog.setView(binding.root)
        dialog.setCancelable(true)
        dialog.show()
        binding.btnCallBackup.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:09398208899")
            startActivity(intent)
        }
        binding.btnOurSite.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(resources.getString(R.string.site_url) + "/main") //TODO: change this to last site address
            )
            startActivity(browserIntent)
        }
    }

    fun onExitClick() {
        showDialogWith2Actions(
            requireContext(),
            resources.getString(R.string.are_you_sure),
            { _, _ -> extiConfirm() },
            { d, _ -> d.dismiss() }
        )
    }
}