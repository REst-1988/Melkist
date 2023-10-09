package com.example.melkist.views.calculator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragCalculatorP1Binding
import com.example.melkist.interfaces.Interaction
import com.example.melkist.models.Roles
import com.example.melkist.utils.loginRequiredDialog
import com.example.melkist.utils.showDialogWithMessage

class CalculatorP1Frag : Fragment() {

    private lateinit var binding: FragCalculatorP1Binding
    private var interaction: Interaction? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragCalculatorP1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        interaction?.changBottomNavViewVisibility(View.VISIBLE)
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

    private fun initListeners() {
        binding.rl1.setOnClickListener {
            if ((activity as MainActivity).user?.id == null) {
                loginRequiredDialog(requireActivity())
                return@setOnClickListener
            }
            onRl1calculatorMortgageRentFragClicked()
        }
        binding.rl2.setOnClickListener {
            if ((activity as MainActivity).user?.id == null) {
                loginRequiredDialog(requireActivity())
                return@setOnClickListener
            }
            onRl2calculatorCommissionFragClicked()
        }
        binding.rl3.setOnClickListener {
            if ((activity as MainActivity).user?.id == null) {
                loginRequiredDialog(requireActivity())
                return@setOnClickListener
            }
            onRl3calculatorExpertsFragClicked()
        }
    }

    private fun onRl1calculatorMortgageRentFragClicked(){
        findNavController().navigate(
            R.id.action_navigation_calculator_to_calculatorMortgageRentFrag
        )
        interaction?.changBottomNavViewVisibility(View.GONE)
    }

    private fun onRl2calculatorCommissionFragClicked(){
        findNavController().navigate(
            R.id.action_navigation_calculator_to_calculatorCommissionFrag
        )
        interaction?.changBottomNavViewVisibility(View.GONE)
    }

    private fun onRl3calculatorExpertsFragClicked(){
        val roles = Roles()
        (activity as MainActivity).user?.apply {
            if (this.roleId == roles.manager.id || this.roleId == roles.supervisor.id || this.roleId == roles.consultant.id) {
                findNavController().navigate(
                    R.id.action_navigation_calculator_to_calculatorExpertsFrag
                )
                interaction?.changBottomNavViewVisibility(View.GONE)
            } else {
                showDialogWithMessage(
                    requireContext(), resources.getString(R.string.access_only_for_real_estates)
                ) { d, _ -> d.dismiss() }
            }
        }
    }
}