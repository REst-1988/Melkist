package com.example.melkist.views.universal.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.melkist.R
import com.example.melkist.adapters.BottomSheetUniversalAdapter
import com.example.melkist.databinding.LayoutBottomSheetUniversalListBinding
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
        TODO()
    }

}