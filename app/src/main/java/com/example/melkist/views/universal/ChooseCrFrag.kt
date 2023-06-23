package com.example.melkist.views.universal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.melkist.R
import com.example.melkist.adapters.ChoosingPcrsAdapter
import com.example.melkist.adapters.ChoosingRegionAdapter
import com.example.melkist.databinding.FragChooseCrBinding
import com.example.melkist.models.PcrsData
import com.example.melkist.models.RegionResponseData
import com.example.melkist.utils.CITY
import com.example.melkist.utils.CR_KEY
import com.example.melkist.utils.DATA
import com.example.melkist.utils.PROVINCE
import com.example.melkist.utils.REGION_1
import com.example.melkist.utils.REGION_2
import com.example.melkist.viewmodels.ChooseCrViewModel

class ChooseCrFrag : Fragment() {

    /** this fragment receive to argument
     *     1. req source for knowing which item need to be done (Constants.PROVINCE,
     *          Constants.CITY, Constants.REGION_1, Constants.REGION_2, Constants.REGION_3)
     *     2. id which would be id of any req source needed
     */

    lateinit var binding: FragChooseCrBinding
    private val viewModel: ChooseCrViewModel by viewModels()
    lateinit var adapter: ChoosingPcrsAdapter
    lateinit var regionAdapter: ChoosingRegionAdapter
    private var arrayBundle: ArrayList<Int>? = arrayListOf()
    private var reqSource: Int? = null
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrayBundle = arguments?.getIntegerArrayList(CR_KEY)
        arrayBundle?.let {
            reqSource = it[0]
            id = it[1]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        adapter = ChoosingPcrsAdapter(viewModel, this@ChooseCrFrag)
        regionAdapter = ChoosingRegionAdapter(viewModel, this@ChooseCrFrag)
        binding = FragChooseCrBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ChooseCrFrag
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
                if (reqSource == PROVINCE || reqSource == CITY) adapter.filter.filter(p0)
                else regionAdapter.filter.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun listenToData() {
        if (reqSource == PROVINCE || reqSource == CITY) viewModel.pcrsList.observe(
            viewLifecycleOwner
        ) {
            binding.rvList.adapter = adapter
            adapter.submitList(viewModel.pcrsList.value)
        }
        else viewModel.regionList.observe(viewLifecycleOwner) {
            binding.rvList.adapter = regionAdapter
            regionAdapter.submitList(viewModel.regionList.value)
        }

    }

    override fun onResume() {
        super.onResume()
        Log.e("TAG", "onResume: $reqSource ", )
        when (reqSource) {
            PROVINCE -> {
                viewModel.getProvinces()
            }
            CITY -> {
                id?.let {
                    viewModel.getCity(it)
                }
            }
            else -> id?.let {
                viewModel.getRegion(it)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (reqSource == PROVINCE || reqSource == CITY) {
            viewModel.emptyPcList()
            adapter.submitList(viewModel.pcrsList.value)
        } else {
            viewModel.emptyRegionList()
            regionAdapter.submitList(viewModel.regionList.value)
        }
    }

    fun onChooseProvinceCityItems (pcrs: PcrsData){
        setFragmentResult(CR_KEY, bundleOf(DATA to arrayOf(pcrs.id.toString(), pcrs.title)))
        back()
    }

    fun onChooseItem(cr: RegionResponseData) {
        setFragmentResult(CR_KEY, bundleOf(DATA to arrayOf(cr.id.toString(), cr.title)))
        back()
    }

    /************** binding commands **********************/
    fun back() {
        findNavController().popBackStack()
    }

    fun chooseTitle(): String {
        return when (reqSource) {
            PROVINCE -> resources.getString(R.string.choose_province_title)
            CITY -> resources.getString(R.string.choose_city_title)
            REGION_1 -> resources.getString(R.string.choose_region_title)
            REGION_2 -> resources.getString(R.string.choose_region_2_title)
            else -> resources.getString(R.string.choose_region_3_title)
        }
    }
}