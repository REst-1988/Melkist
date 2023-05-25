package com.example.melkist.views.add

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.melkist.R
import com.example.melkist.adapters.ChoosingPcrsAdapter
import com.example.melkist.adapters.ChoosingRegionAdapter
import com.example.melkist.databinding.FragAddP5CrBinding
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.AddItemViewModel

class AddP5CrFrag : Fragment() {

    lateinit var binding: FragAddP5CrBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    lateinit var adapter: ChoosingPcrsAdapter
    lateinit var regionAdapter: ChoosingRegionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = ChoosingPcrsAdapter(viewModel, this@AddP5CrFrag)
        regionAdapter = ChoosingRegionAdapter(viewModel, this@AddP5CrFrag)
        binding = FragAddP5CrBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP5CrFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToSearchViewChanges()
        listenToData()
        binding.rvList.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
    }



    private fun listenToSearchViewChanges() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (viewModel.getLocReqSource() == AddItemViewModel.Cr.PROVINCE || viewModel.getLocReqSource() == AddItemViewModel.Cr.CITY)
                    adapter.filter.filter(p0)
                else
                    regionAdapter.filter.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun listenToData() {
        if (viewModel.getLocReqSource() == AddItemViewModel.Cr.PROVINCE || viewModel.getLocReqSource() == AddItemViewModel.Cr.CITY)
            viewModel.pcrsList.observe(viewLifecycleOwner) {
                binding.rvList.adapter = adapter
                adapter.submitList(viewModel.pcrsList.value)
            }
        else
            viewModel.regionList.observe(viewLifecycleOwner) {
                binding.rvList.adapter = regionAdapter
                regionAdapter.submitList(viewModel.regionList.value)
            }

    }

    override fun onResume() {
        super.onResume()

        when (viewModel.getLocReqSource()) {
            AddItemViewModel.Cr.PROVINCE -> {
                viewModel.getProvinces()
            }
            AddItemViewModel.Cr.CITY -> {
                viewModel.getCity()
            }
            else -> viewModel.getRegion()
        }
    }

    override fun onStop() {
        super.onStop()
        if (viewModel.getLocReqSource() == AddItemViewModel.Cr.PROVINCE || viewModel.getLocReqSource() == AddItemViewModel.Cr.CITY) {
            viewModel.emptyPcList()
            adapter.submitList(viewModel.pcrsList.value)
        } else {
            viewModel.emptyRegionList()
            regionAdapter.submitList(viewModel.regionList.value)
        }
    }

    /************** binding commands **********************/
    fun back() {
        findNavController().navigate(R.id.action_addP5CrFrag_to_addP4LocationFrag)
    }

    fun chooseTitle(): String {
        return when (viewModel.getLocReqSource()) {
            AddItemViewModel.Cr.PROVINCE -> resources.getString(R.string.choose_province_title)
            AddItemViewModel.Cr.CITY -> resources.getString(R.string.choose_city_title)
            AddItemViewModel.Cr.REGION_1 -> resources.getString(R.string.choose_region_title)
            AddItemViewModel.Cr.REGION_2 -> resources.getString(R.string.choose_region_2_title)
            AddItemViewModel.Cr.REGION_3 -> resources.getString(R.string.choose_region_3_title)
        }
    }


}