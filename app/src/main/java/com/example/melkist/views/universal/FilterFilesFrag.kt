package com.example.melkist.views.universal

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragFilterFilesBinding
import com.example.melkist.models.FilterFileData
import com.example.melkist.models.Period
import com.example.melkist.utils.AGE_FROM_TAG
import com.example.melkist.utils.AGE_TO_TAG
import com.example.melkist.utils.CAT_ID_KEY
import com.example.melkist.utils.CAT_RESULT_KEY
import com.example.melkist.utils.CR_KEY
import com.example.melkist.utils.DATA
import com.example.melkist.utils.EMPTY_CATEGORY_ID
import com.example.melkist.utils.IS_IN_FAV_LIST_KEY
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
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.getPersianYear
import com.example.melkist.utils.hasFilterData
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel
import com.example.melkist.viewmodels.MainViewModel.ItemType
import com.example.melkist.views.universal.dialog.BottomSheetUniversalList
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.math.BigDecimal

private const val TAG = "FilterFilesFrag"

class FilterFilesFrag : Fragment() {

    private lateinit var binding: FragFilterFilesBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var isInFavList = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arrayBundle = arguments?.getBoolean(IS_IN_FAV_LIST_KEY)
        arrayBundle?.let {
            isInFavList = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragFilterFilesBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@FilterFilesFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        assignFilterDataToFields()
        setResultListeners()
    }

    private fun setResultListeners() {
        setFragmentResultListener(ITEM_TYPE_KEY) { _, bundle ->
            when (bundle.getInt(DATA)) {
                OWNER_ITEM_TYPE -> viewModel.setItemType(MainViewModel.ItemType.SHOW_OWNER)
                SEEKER_ITEM_TYPE -> viewModel.setItemType(MainViewModel.ItemType.SHOW_SEEKER)
                else -> viewModel.setItemType(ItemType.SHOW_ALL)
            }
            binding.txtChooseType.text = showTypeText()
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

    private fun assignFilterDataToFields() {
        binding.apply {
            if (isInFavList)
                viewModel.filterFavData?.let { data ->
                    if (data.age.from != 0L) etAgeFromChild.setText(data.age.from.toString())
                    if (data.age.to != 0L) etAgeToChild.setText(data.age.to.toString())
                    if (data.size.from != 0L) etSizeFromChild.setText(data.size.from.toString())
                    if (data.size.to != 0L) etSizeToChild.setText(data.size.to.toString())
                    if (data.rooms.from != 0L) etRoomNoFromChild.setText(data.rooms.from.toString())
                    if (data.rooms.to != 0L) etRoomNoToChild.setText(data.rooms.to.toString())
                    if (data.price.from != 0L) etPriceFromChild.setText(formatNumber(data.price.from!!))
                    if (data.price.to != 0L) etPriceToChild.setText(formatNumber(data.price.to!!))
                }
            else
                viewModel.filterFileData?.let { data ->
                    if (data.age.from != 0L) etAgeFromChild.setText(data.age.from.toString())
                    if (data.age.to != 0L) etAgeToChild.setText(data.age.to.toString())
                    if (data.size.from != 0L) etSizeFromChild.setText(data.size.from.toString())
                    if (data.size.to != 0L) etSizeToChild.setText(data.size.to.toString())
                    if (data.rooms.from != 0L) etRoomNoFromChild.setText(data.rooms.from.toString())
                    if (data.rooms.to != 0L) etRoomNoToChild.setText(data.rooms.to.toString())
                    if (data.price.from != 0L) etPriceFromChild.setText(formatNumber(data.price.from!!))
                    if (data.price.to != 0L) etPriceToChild.setText(formatNumber(data.price.to!!))
                }
        }
    }

    private fun checkFieldsForNullability(view: TextInputLayout): Long {
        if (view.editText!!.text.toString().isNotEmpty() && view.editText!!.text.toString() != "") {
            if (view.editText!!.text.toString()
                    .contains(",")
            ) return BigDecimal(
                view.editText!!.text.toString().replace(",", "").replace("٬", "")
            ).toLong()
            return view.editText!!.text.toString().toLong()
        }
        return 0  // Amir needs 0 not null
    }

    private fun gatheringData(): FilterFileData {
        binding.apply {
            val rooms = Period(
                from = checkFieldsForNullability(etRoomNoFrom),
                to = checkFieldsForNullability(etRoomNoTo)
            )
            val price = Period(
                from = checkFieldsForNullability(etPriceFrom),
                to = checkFieldsForNullability(etPriceTo)
            )
            val age = Period(
                from = checkFieldsForNullability(etAgeFrom),
                to = checkFieldsForNullability(etAgeTo)
            )
            val size = Period(
                from = checkFieldsForNullability(etSizeFrom),
                to = checkFieldsForNullability(etSizeTo)
            )
            return FilterFileData(
                null,
                rooms,
                price,
                age,
                size,
                if (viewModel.getItemType() == MainViewModel.ItemType.SHOW_ALL)
                    null
                else
                    viewModel.getTypeId(viewModel.getItemType()),
                viewModel.catId,
                viewModel.subCatId,
                viewModel.regionId
            )
        }
    }

    private fun addLiveSeparator(et: TextInputEditText) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                et.removeTextChangedListener(this)
                val text: String = p0.toString()
                if (!TextUtils.isEmpty(text)) {
                    val format: String =
                        formatNumber(BigDecimal(text.replace(",", "").replace("٬", "")).toLong())
                    et.setText(format)
                    et.setSelection(format.length)
                }
                et.addTextChangedListener(this)
            }
        })
    }

    /********************* binding methods **************************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onFilterClick() {
        if (hasFilterData(gatheringData())) {
            if (isInFavList)
                viewModel.filterFavData = gatheringData()
            else
                viewModel.filterFileData = gatheringData()
            back()
        } else {
            showToast(
                requireContext(),
                resources.getString(R.string.empty_filter_items)
            )
        }
    }

    fun onUnFilterClick() {
        viewModel.setItemType(ItemType.SHOW_ALL)
        if (isInFavList)
            viewModel.filterFavData = null
        else
            viewModel.filterFileData = null
        viewModel.catId = null
        viewModel.subCatId = null
        viewModel.regionId = null
        viewModel.catTitle = null
        viewModel.subCatTitle = null
        viewModel.regionTitle = null
        showTypeText()
        showCategoryText()
        showSubCategoryText()
        showRegionText()
        binding.apply {
            etAgeToChild.setText("")
            etAgeFromChild.setText("")
            etPriceToChild.setText("")
            etPriceFromChild.setText("")
            etSizeFromChild.setText("")
            etSizeToChild.setText("")
            etRoomNoFromChild.setText("")
            etRoomNoToChild.setText("")
        }
        //setFragmentResult(FILTER_RESULT_KEY, bundleOf(DATA to gatheringData()))
        back()
    }

    fun onChoosingType() {
        findNavController().navigate(R.id.action_filterFilesFrag_to_chooseTypeFrag)
    }

    fun showTypeText(): String {
        return when (viewModel.getItemType()) {
            ItemType.SHOW_SEEKER -> requireContext().resources.getText(R.string.choosing_seeker_header)
                .toString()

            ItemType.SHOW_OWNER -> requireContext().resources.getText(R.string.choosing_owner_header)
                .toString()

            else -> requireContext().resources.getText(R.string.all).toString()
        }
    }

    fun onChoosingCategory() {
        if (viewModel.getItemType() != ItemType.SHOW_ALL) {
            val array = arrayListOf(
                (activity as MainActivity).user!!.token,
                viewModel.getTypeId(viewModel.getItemType()).toString(),
                EMPTY_CATEGORY_ID.toString()
            )
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
        return if (viewModel.catId != null && viewModel.catTitle!!.isNotEmpty()) viewModel.catTitle!!
        else resources.getString(R.string.all)
    }

    fun onChoosingSubCategory() {
        if (viewModel.catId != null && viewModel.catTitle!!.isNotEmpty()) {
            val array =
                arrayListOf(
                    (activity as MainActivity).user!!.token,
                    viewModel.getTypeId(viewModel.getItemType()).toString(),
                    viewModel.catId.toString()
                )
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
        return if (viewModel.subCatId != null && viewModel.subCatTitle!!.isNotEmpty()) viewModel.subCatTitle!!
        else resources.getString(R.string.all)
    }

    fun showRegionText(): String {
        return if (viewModel.regionTitle != null && viewModel.regionTitle != "") viewModel.regionTitle!!
        else resources.getString(R.string.all)
    }

    fun onChoosingRegionClick() {
        val bundle = bundleOf(
            CR_KEY to arrayListOf(
                REGION_1, 733 // Note that in the future city id might change (could be variable)
            )
        )
        findNavController().navigate(R.id.action_FilterFilesFrag_to_ChooseCrFrag2, bundle)
    }

    fun onAgeFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.age_list).toList())
        bottomFrag.show(childFragmentManager, AGE_FROM_TAG)
        bottomFrag.setFragmentResultListener(AGE_FROM_TAG) { _, positionBundle ->
            val year = getPersianYear() - resources.getStringArray(
                R.array.int_age_list
            )[positionBundle.getInt(DATA)].toInt()
            binding.etAgeFromChild.setText(year.toString())
        }
    }

    fun onAgeToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.age_list).toList())
        bottomFrag.show(childFragmentManager, AGE_TO_TAG)
        bottomFrag.setFragmentResultListener(AGE_TO_TAG) { _, positionBundle ->
            val year = getPersianYear() - resources.getStringArray(
                R.array.int_age_list
            )[positionBundle.getInt(DATA)].toInt()
            binding.etAgeToChild.setText(year.toString())
        }
    }

    fun onSizeFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.size_list).toList())
        bottomFrag.show(childFragmentManager, SIZE_FROM_TAG)
        bottomFrag.setFragmentResultListener(SIZE_FROM_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainSizeFrom.visibility = View.GONE
                binding.etSizeFromChild.requestFocus()
            } else {
                binding.etSizeFromChild.setText(resources.getStringArray(R.array.int_size_list)[position])
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
                binding.curtainSizeTo.visibility = View.GONE
                binding.etSizeToChild.requestFocus()
            } else {
                binding.etSizeToChild.setText(resources.getStringArray(R.array.int_size_list)[position])
            }
        }
    }

    fun onRoomNoFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.room_no_list).toList())
        bottomFrag.show(childFragmentManager, ROOM_FROM_TAG)
        bottomFrag.setFragmentResultListener(ROOM_FROM_TAG) { _, positionBundle ->
            binding.etRoomNoFromChild.setText(
                resources.getStringArray(R.array.int_room_no_list)[positionBundle.getInt(DATA)]
            )
        }
    }

    fun onRoomNoToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.room_no_list).toList())
        bottomFrag.show(childFragmentManager, ROOM_TO_TAG)
        bottomFrag.setFragmentResultListener(ROOM_TO_TAG) { _, positionBundle ->
            binding.etRoomNoToChild.setText(
                resources.getStringArray(R.array.int_room_no_list)[positionBundle.getInt(DATA)]
            )
        }
    }

    fun onPriceFromClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.price_list).toList())
        bottomFrag.show(childFragmentManager, PRICE_FROM_TAG)
        bottomFrag.setFragmentResultListener(PRICE_FROM_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainPriceFrom.visibility = View.GONE
                binding.etPriceFromChild.requestFocus()
                addLiveSeparator(binding.etPriceFromChild)
            } else {
                binding.etPriceFromChild.setText(
                    formatNumber(
                        resources.getStringArray(R.array.int_price_list)[position].toLong()
                    )
                )
            }
        }
    }

    fun onPriceToClick() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.price_list).toList())
        bottomFrag.show(childFragmentManager, PRICE_TO_TAG)
        bottomFrag.setFragmentResultListener(PRICE_TO_TAG) { _, positionBundle ->
            val position = positionBundle.getInt(DATA)
            if (position == 0) {
                binding.curtainPriceTo.visibility = View.GONE
                binding.etPriceToChild.requestFocus()
                addLiveSeparator(binding.etPriceToChild)
            } else {
                binding.etPriceToChild.setText(
                    formatNumber(
                        resources.getStringArray(R.array.int_price_list)[position].toLong()
                    )
                )
            }
        }
    }
}