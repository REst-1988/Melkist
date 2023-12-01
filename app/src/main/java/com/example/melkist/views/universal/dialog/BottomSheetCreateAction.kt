package com.example.melkist.views.universal.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.melkist.R
import com.example.melkist.databinding.LayoutBottomSheetCreateActionBinding
import com.example.melkist.models.Action
import com.example.melkist.models.Actions
import com.example.melkist.utils.getPersianDate
import com.example.melkist.utils.getPersianTimestamp
import com.example.melkist.utils.getStringDateByTimestamp
import com.example.melkist.utils.isPhoneNo
import com.example.melkist.utils.isText
import com.example.melkist.utils.pickDateDialogForResult
import com.example.melkist.utils.showToast
import com.example.melkist.views.profile.ProfileMyFilesFrag
import com.example.melkist.views.universal.FileDetailFrag
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCreateAction(
    val fragment: Fragment,
    val fileId: Int
) : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutBottomSheetCreateActionBinding
    private var date: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LayoutBottomSheetCreateActionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btns = arrayOf(
            binding.choiceVisit,
            binding.choiceMeeting,
            binding.choiceContract,
            binding.choiceConclusion
        )
        binding.txtDate.text = getPersianDate()
        date = getPersianTimestamp()
        onActionsClicked(btns)

        binding.txtDate.setOnClickListener {
            pickDateDialogForResult(requireContext()).observe(viewLifecycleOwner) {
                binding.txtDate.text = getStringDateByTimestamp(it)
                date = it
            }
        }

        binding.btnCommitAction.setOnClickListener {
            onCommitClicked(btns)
        }
    }

    private lateinit var action: Action

    @SuppressLint("ClickableViewAccessibility")
    private fun onActionsClicked(btns: Array<TextView>) {
        btns.forEach { view ->
            view.setOnTouchListener { v, event ->
                btns.forEach { it.isPressed = false }
                if (event.action == MotionEvent.ACTION_UP) {
                    v.isPressed = true
                }
                setAction(v.id)
                return@setOnTouchListener true
            }
        }
    }

    private fun setAction(id: Int) {
        when (id) {
            binding.choiceVisit.id -> action = Actions().actionVisit
            binding.choiceMeeting.id -> action = Actions().actionMeeting
            binding.choiceContract.id -> action = Actions().actionContract
            binding.choiceConclusion.id -> action = Actions().actionConsult
        }
    }

    private fun onCommitClicked(btns: Array<TextView>) {
        var a = false
        btns.forEach {
            if (it.isPressed) {
                a = true
                return@forEach
            }
        }
        if (!isText(requireContext(), binding.etActionOwnerName)) return
        else binding.etActionOwnerName.error = null

        if (!isPhoneNo(requireContext(), binding.etActionOwnerMobile)) return
        else binding.etActionOwnerMobile.error = null
        if (a) {
            action.actionOwnerName = binding.etActionOwnerNameChild.text.toString()
            action.actionOwnerMobile = binding.etActionOwnerMobileChild.text.toString()
            action.actionDate = date

            when (fragment){
                is FileDetailFrag -> fragment.saveAction(action, fileId)
                is ProfileMyFilesFrag -> fragment.saveAction(action, fileId)
            }

            this.dismiss()
        } else {
            showToast(
                requireContext(),
                resources.getString(R.string.choose_a_field)
            )
        }
    }
}
