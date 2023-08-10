package com.example.melkist.views.universal.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.melkist.adapters.BottomSheetUniversalAdapter
import com.example.melkist.databinding.LayoutBottomSheetUniversalListBinding
import com.example.melkist.utils.DATA
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetUniversalList (val list: List<String>) : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutBottomSheetUniversalListBinding
    private lateinit var adapter: BottomSheetUniversalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = BottomSheetUniversalAdapter(list, this)
        binding = LayoutBottomSheetUniversalListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvBottomSheetList.adapter = adapter
    }

    fun setOnItemClickListener(position: Int) {
        this.tag?.let{ tag ->
            setFragmentResult(tag, bundleOf(DATA to position))
            this.dismiss()
        }
    }

}