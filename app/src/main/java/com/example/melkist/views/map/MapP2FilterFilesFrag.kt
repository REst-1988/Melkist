package com.example.melkist.views.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragMapP2FilterFilesBinding
import com.example.melkist.utils.*
import com.example.melkist.viewmodels.MapViewModel


class MapP2FilterFilesFrag : Fragment() {

    private var binding: FragMapP2FilterFilesBinding? = null
    private val viewModel: MapViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragMapP2FilterFilesBinding.inflate(inflater)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@MapP2FilterFilesFrag
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(ITEM_TYPE_KEY) { key, bundle ->
            val result = bundle.getInt(key)
            itemTypeHandling(result)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


    private fun itemTypeHandling(result: Int) {
        when (result) {
            OWNER_ITEM_TYPE -> viewModel.setItemType(MapViewModel.ItemType.SHOW_OWNER)
            SEEKER_ITEM_TYPE -> viewModel.setItemType(MapViewModel.ItemType.SHOW_SEEKER)
        }
        viewModel.resetCatSubCat()
    }

    /********************* binding methods **************************/
    fun back() {
        findNavController().popBackStack()
    }


    fun onChoosingtype() {
        findNavController().navigate(R.id.action_mapP2FilterFilesFrag_to_addP2ChooseTypeFrag2)
    }

    fun showTypeText(): String {
        return when (viewModel.getItemType()) {
            MapViewModel.ItemType.SHOW_SEEKER -> requireContext().resources.getText(R.string.choosing_seeker_header)
                .toString()
            MapViewModel.ItemType.SHOW_OWNER -> requireContext().resources.getText(R.string.choosing_owner_header)
                .toString()
            else -> requireContext().resources.getText(R.string.all).toString()
        }
    }

    fun onChoosingCategory() {
        val array = arrayListOf<Int?>(viewModel.getTypeId(viewModel.getItemType()), -1)
        val bundle = bundleOf(CAT_ID_KEY to array)
        findNavController().navigate(R.id.action_mapP2FilterFilesFrag_to_chooseCatSubcatFrag, bundle)
    }

    fun showCategoryText(): String {
        return if (viewModel.catId != 0 && viewModel.catTitle.isNotEmpty()) viewModel.catTitle
        else resources.getString(R.string.all)
    }

    fun onChoosingSubCategory() {
        val array = arrayListOf<Int?>(viewModel.getTypeId(viewModel.getItemType()), viewModel.catId)
        val bundle = bundleOf(CAT_ID_KEY to array)
        findNavController().navigate(R.id.action_mapP2FilterFilesFrag_to_chooseCatSubcatFrag, bundle)
    }

    fun showSubCategoryText(): String {
        return if (viewModel.subCatId != 0 && viewModel.subCatTitle.isNotEmpty()) viewModel.subCatTitle
        else resources.getString(R.string.all)
    }

    fun isShowLocationBtn(): Boolean {
        return viewModel.subCatId != 0 && viewModel.subCatTitle.isNotEmpty()
    }

    fun onProceed() {
        findNavController().navigate(
            R.id.action_addP1MainFrag_to_addP4LocationFrag
        )
    }

    fun onAgeFromClick() {
        Toast.makeText(requireContext(), "click achived", Toast.LENGTH_SHORT).show()
    }

    fun onAgeToClick() {
        Toast.makeText(requireContext(), "click achived", Toast.LENGTH_SHORT).show()
    }

    fun onSizeFromClick() {
        Toast.makeText(requireContext(), "click achived", Toast.LENGTH_SHORT).show()
    }

    fun onSizeToClick() {
        Toast.makeText(requireContext(), "click achived", Toast.LENGTH_SHORT).show()
    }

    fun onRoomNoFromClick() {
        Toast.makeText(requireContext(), "click achived", Toast.LENGTH_SHORT).show()
    }

    fun onRoomNoToClick() {
        Toast.makeText(requireContext(), "click achived", Toast.LENGTH_SHORT).show()
    }

    fun onPriceFromClick() {
        Toast.makeText(requireContext(), "click achived", Toast.LENGTH_SHORT).show()
    }

    fun onPriceToClick() {
        Toast.makeText(requireContext(), "click achived", Toast.LENGTH_SHORT).show()
    }

}