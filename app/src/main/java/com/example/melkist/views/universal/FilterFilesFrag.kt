package com.example.melkist.views.universal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragFilterFilesBinding
import com.example.melkist.utils.AGE_FROM_TAG
import com.example.melkist.utils.AGE_TO_TAG
import com.example.melkist.utils.CAT_ID_KEY
import com.example.melkist.utils.CAT_RESULT_KEY
import com.example.melkist.utils.CR_KEY
import com.example.melkist.utils.DATA
import com.example.melkist.utils.ITEM_TYPE_KEY
import com.example.melkist.utils.OWNER_ITEM_TYPE
import com.example.melkist.utils.PRICE_FROM_TAG
import com.example.melkist.utils.PRICE_TO_TAG
import com.example.melkist.utils.REGION_1
import com.example.melkist.utils.ROOM_FROM_TAG
import com.example.melkist.utils.ROOM_TO_TAG
import com.example.melkist.utils.SEEKER_ITEM_TYPE
import com.example.melkist.utils.SIZE_FROM_TAG
import com.example.melkist.utils.SIZE_TO_TAG
import com.example.melkist.utils.SUB_CAT_RESULT_KEY
import com.example.melkist.utils.User
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.viewmodels.FilterFileViewModel
import com.example.melkist.viewmodels.MapViewModel
import com.example.melkist.views.universal.dialog.BottomSheetUniversalList


private const val TAG = "FilterFilesFrag"

class FilterFilesFrag : Fragment() {

    private var binding: FragFilterFilesBinding? = null
    private val viewModel: FilterFileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragFilterFilesBinding.inflate(inflater)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@FilterFilesFrag
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(ITEM_TYPE_KEY) { _, bundle ->
            when (bundle.getInt(DATA)) {
                OWNER_ITEM_TYPE -> viewModel.setItemType(MapViewModel.ItemType.SHOW_OWNER)
                SEEKER_ITEM_TYPE -> viewModel.setItemType(MapViewModel.ItemType.SHOW_SEEKER)
            }
            viewModel.resetCatSubCat()
        }
        setFragmentResultListener(CAT_RESULT_KEY) { _, bundle ->
            bundle.getStringArray(DATA)?.apply {
                viewModel.catId = this[0].toInt()
                viewModel.catTitle = this[1]
                if (this[0].toInt() != 0) {
                    viewModel.resetSubCat()
                }
            }
        }
        setFragmentResultListener(SUB_CAT_RESULT_KEY) { _, bundle ->
            bundle.getStringArray(DATA)?.apply {
                viewModel.subCatId = this[0].toInt()
                viewModel.subCatTitle = this[1]
            }
        }
        setFragmentResultListener(CR_KEY) { _, bundle ->
            bundle.getStringArray(DATA)?.apply {
                viewModel.regionId = this[0].toInt()
                viewModel.regionTitle = this[1]
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    /********************* binding methods **************************/
    fun back() {
        findNavController().popBackStack()
    }
    fun onProceed() {
        TODO()
    }
    fun onChoosingtype() {
        findNavController().navigate(R.id.action_filterFilesFrag_to_chooseTypeFrag)
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
        if (viewModel.getItemType() != MapViewModel.ItemType.SHOW_ALL) {
            val array = arrayListOf<Int?>(viewModel.getTypeId(viewModel.getItemType()), -1)
            val bundle = bundleOf(CAT_ID_KEY to array)
            findNavController().navigate(
                R.id.action_mapP2FilterFilesFrag_to_chooseCatSubcatFrag, bundle
            )
        } else {
            showDialogWithMessage(
                requireContext(), resources.getString(R.string.choose_type_first)
            ) { d, _ ->
                d.dismiss()
            }
        }
    }

    fun showCategoryText(): String {
        return if (viewModel.catId != 0 && viewModel.catTitle.isNotEmpty()) viewModel.catTitle
        else resources.getString(R.string.all)
    }

    fun onChoosingSubCategory() {
        if (viewModel.catId != 0 && viewModel.catTitle.isNotEmpty()) {
            val array =
                arrayListOf<Int?>(viewModel.getTypeId(viewModel.getItemType()), viewModel.catId)
            val bundle = bundleOf(CAT_ID_KEY to array)
            findNavController().navigate(
                R.id.action_mapP2FilterFilesFrag_to_chooseCatSubcatFrag, bundle
            )
        } else {
            showDialogWithMessage(
                requireContext(), resources.getString(R.string.choose_type_cat_first)
            ) { d, _ ->
                d.dismiss()
            }
        }
    }

    fun showSubCategoryText(): String {
        return if (viewModel.subCatId != 0 && viewModel.subCatTitle.isNotEmpty()) viewModel.subCatTitle
        else resources.getString(R.string.all)
    }

    fun showRegionText(): String {
        return if (viewModel.regionTitle != "")
            viewModel.regionTitle
        else
            resources.getString(R.string.all)
    }

    fun onChoosingRegionClick() {
        val bundle = bundleOf(CR_KEY to arrayListOf(REGION_1, 733)) // Note that in the future this might change
        findNavController().navigate(R.id.action_FilterFilesFrag_to_ChooseCrFrag2, bundle)
    }

    fun onAgeFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.age_list).toList())
        bottomFrag.show(childFragmentManager, AGE_FROM_TAG)
        bottomFrag.setFragmentResultListener(AGE_FROM_TAG) { _, positionBundle ->
            binding!!.etAgeFromChild.setText(resources.getStringArray(
                R.array.age_list)[positionBundle.getInt(DATA)]
            )
        }
    }

    fun onAgeToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.age_list).toList())
        bottomFrag.show(childFragmentManager, AGE_TO_TAG)
        bottomFrag.setFragmentResultListener(AGE_TO_TAG) { _, positionBundle ->
            binding!!.etAgeToChild.setText(
                resources.getStringArray(R.array.age_list)[positionBundle.getInt(DATA)]
            )
        }
    }

    fun onSizeFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.size_list).toList())
        bottomFrag.show(childFragmentManager, SIZE_FROM_TAG)
        bottomFrag.setFragmentResultListener(SIZE_FROM_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding!!.curtainSizeFrom.visibility = View.GONE
                binding!!.etSizeFromChild.requestFocus()
            } else {
                binding!!.etSizeFromChild.setText(resources.getStringArray(R.array.size_list)[position])
            }
        }
    }

    fun onSizeToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.size_list).toList())
        bottomFrag.show(childFragmentManager, SIZE_TO_TAG)
        bottomFrag.setFragmentResultListener(SIZE_TO_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding!!.curtainSizeTo.visibility = View.GONE
                binding!!.etSizeToChild.requestFocus()
            } else {
                binding!!.etSizeToChild.setText(resources.getStringArray(R.array.size_list)[position])
            }
        }
    }

    fun onRoomNoFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.room_no_list).toList())
        bottomFrag.show(childFragmentManager, ROOM_FROM_TAG)
        bottomFrag.setFragmentResultListener(ROOM_FROM_TAG) { _, positionBundle ->
            binding!!.etRoomNoFromChild.setText(
                resources.getStringArray(R.array.room_no_list)[positionBundle.getInt(DATA)]
            )
        }
    }

    fun onRoomNoToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.room_no_list).toList())
        bottomFrag.show(childFragmentManager, ROOM_TO_TAG)
        bottomFrag.setFragmentResultListener(ROOM_TO_TAG) { _, positionBundle ->
            binding!!.etRoomNoToChild.setText(
                resources.getStringArray(R.array.room_no_list)[positionBundle.getInt(DATA)]
            )
        }
    }

    fun onPriceFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.price_list).toList())
        bottomFrag.show(childFragmentManager, PRICE_FROM_TAG)
        bottomFrag.setFragmentResultListener(PRICE_FROM_TAG) { _, positionBundle ->
            binding!!.etPriceFromChild.setText(
                resources.getStringArray(R.array.price_list)[positionBundle.getInt(DATA)]
            )
        }
    }

    fun onPriceToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.price_list).toList())
        bottomFrag.show(childFragmentManager, PRICE_TO_TAG)
        bottomFrag.setFragmentResultListener(PRICE_TO_TAG) { _, positionBundle ->
            binding!!.etPriceToChild.setText(
                resources.getStringArray(R.array.price_list)[positionBundle.getInt(DATA)]
            )
        }
    }
}