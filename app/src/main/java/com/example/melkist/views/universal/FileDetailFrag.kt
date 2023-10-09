package com.example.melkist.views.universal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.melkist.AddActivity
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.ImagePagerAdapter
import com.example.melkist.databinding.FragFileDetailBinding
import com.example.melkist.models.FileSave
import com.example.melkist.models.FileTypes
import com.example.melkist.models.Location
import com.example.melkist.utils.EXTRA_FILE_DETAIL
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.calculatePricePerMeter
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.getPropertyPeriodsPriceText
import com.example.melkist.utils.getPropertyPeriodsText
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.isShowAdminDeedField
import com.example.melkist.utils.isShowAgeField
import com.example.melkist.utils.isShowBalconyField
import com.example.melkist.utils.isShowDeedTypeField
import com.example.melkist.utils.isShowElevatorField
import com.example.melkist.utils.isShowFloorField
import com.example.melkist.utils.isShowMortgageField
import com.example.melkist.utils.isShowParkingField
import com.example.melkist.utils.isShowRentField
import com.example.melkist.utils.isShowRoomsField
import com.example.melkist.utils.isShowSizeField
import com.example.melkist.utils.isShowStoreRoomField
import com.example.melkist.utils.isShowSuitableForField
import com.example.melkist.utils.isShowTotalPriceField
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.utils.showDialogWith2Actions
import com.example.melkist.utils.showFav
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel

class FileDetailFrag : Fragment() {

    private lateinit var binding: FragFileDetailBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragFileDetailBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@FileDetailFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val a = layoutInflater.inflate(R.layout.layout_items_time_line, null)
        binding.llTimeLine.addView(a)
        val b = layoutInflater.inflate(R.layout.layout_items_time_line, null)
        binding.llTimeLine.addView(b)
        val v = layoutInflater.inflate(R.layout.layout_items_time_line, null)
        binding.llTimeLine.addView(v)
        val c = layoutInflater.inflate(R.layout.layout_items_time_line, null)
        binding.llTimeLine.addView(c)
        val x = layoutInflater.inflate(R.layout.layout_items_time_line, null)
        binding.llTimeLine.addView(x)
        val z = layoutInflater.inflate(R.layout.layout_items_time_line, null)
        binding.llTimeLine.addView(z)
        val q = layoutInflater.inflate(R.layout.layout_items_time_line, null)
        binding.llTimeLine.addView(q)
        val w = layoutInflater.inflate(R.layout.layout_items_time_line, null)
        binding.llTimeLine.addView(w)


    }

    override fun onResume() {
        super.onResume()
        initRequireViews()
        initListener()
        initObservers()
    }

    private fun initRequireViews() {
        viewModel.fileAllData.value?.data?.apply {
            typeInfo?.fileType?.let { idTitle ->
                if (idTitle.id == FileTypes().owner.id) {
                    binding.viewPager.visibility = View.VISIBLE
                    binding.indicator.visibility = View.VISIBLE
                    viewModel.fileAllData.value?.data?.images?.apply {
                        binding.viewPager.adapter =
                            ImagePagerAdapter(null, null, this)
                        binding.indicator.setViewPager(binding.viewPager)
                    }
                } else {
                    binding.viewPager.visibility = View.GONE
                    binding.indicator.visibility = View.GONE
                }
            }
            (activity as MainActivity).user?.let { user ->
                this.user?.let { fileCreatorUser ->
                    if (fileCreatorUser.id == user.id) {
                        binding.apply {
                            layoutBtnCooperationRequest.visibility = View.GONE
                            layoutBtnDeleteFile.visibility = View.VISIBLE
                        }
                    } else {
                        binding.apply {
                            layoutBtnCooperationRequest.visibility = View.VISIBLE
                            layoutBtnDeleteFile.visibility = View.GONE
                        }
                    }
                }
            }
            isFav?.apply {
                binding.ibtnBookmark.showFav(this)
            }
        }
    }

    private fun initListener() {
        binding.ibtnBookmark.setOnClickListener {
            viewModel.fileAllData.value?.data?.isFav?.apply {
                if (this) {
                    (activity as MainActivity).user.apply {
                        viewModel.deleteFavFile(
                            requireActivity(),
                            this?.token,
                            this?.id,
                            viewModel.fileAllData.value!!.data!!.id
                        )
                    }
                } else {
                    (activity as MainActivity).user.apply {
                        viewModel.saveFavFile(
                            requireActivity(),
                            this?.token,
                            this?.id,
                            viewModel.fileAllData.value!!.data!!.id
                        )
                    }
                }
            }
        }
        binding.ibtnShare.setOnClickListener {
            showToast(requireContext(), resources.getString(R.string.next_phase))
        }
    }

    private fun initObservers() {
        viewModel.cooperationResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    response.message?.let {
                        showToast(
                            requireContext(), it
                        )
                    }
                    viewModel.resetCooperationResponse()
                    back()
                }

                false -> {
                    onRequestFalseResult(
                        requireActivity(),
                        response.errors ?: UNKNOWN_ERRORS_LIST
                    ) {}
                    viewModel.resetCooperationResponse()
                }

                else -> {}
            }
        }

        viewModel.saveFavResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    response.message?.let {
                        showToast(
                            requireContext(), it
                        )
                        binding.ibtnBookmark.setImageResource(R.drawable.baseline_bookmark_added_24)
                        viewModel.resetSaveResponse()
                        viewModel.fileAllData.value?.data?.isFav = true
                    }
                }

                false -> {
                    onRequestFalseResult(
                        requireActivity(),
                        response.errors ?: UNKNOWN_ERRORS_LIST
                    ) {}
                    viewModel.resetSaveResponse()
                }

                else -> Log.e(
                    "TAG",
                    "saveFavResponse: ${resources.getString(R.string.null_value)}"
                )
            }
        }

        viewModel.deleteFavResponse.observe(viewLifecycleOwner) { response ->
            Log.e("TAG", "deleteFavResponse: test $response")
            when (response.result) {
                true -> {
                    showToast(
                        requireContext(), response.message!!
                    )
                    binding.ibtnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                    viewModel.resetDeleteFavResponse()
                    viewModel.fileAllData.value?.data?.isFav = false
                }

                false -> {
                    onRequestFalseResult(
                        requireActivity(),
                        response.errors ?: UNKNOWN_ERRORS_LIST
                    ) {}
                    viewModel.resetDeleteFavResponse()
                }

                else -> Log.e(
                    "TAG",
                    "deleteFavResponse: ${resources.getString(R.string.null_value)}"
                )
            }
        }

        viewModel.deleteFileResponse.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    viewModel.resetDeleteFileResponse()
                    back()
                }

                false -> {
                    viewModel.resetDeleteFileResponse()
                    onRequestFalseResult(
                        requireActivity(),
                        response.errors ?: UNKNOWN_ERRORS_LIST
                    ) {}
                }

                null -> Log.e(
                    "TAG",
                    "initObservers: ${resources.getString(R.string.null_value)}"
                )
            }
        }
    }

    private fun showOtherRegions(locations: List<Location>?): String {
        var text = ""
        locations?.apply {
            if (size > 1) {
                binding.txtRegionOther.visibility = View.VISIBLE
                binding.txtRegionOtherTitle.visibility = View.VISIBLE
                val a = mutableListOf<String>()
                for (i in 1 until size)
                    a.add(this[i].region.title)
                text = concatenateText(a)
            } else {
                binding.txtRegionOther.visibility = View.GONE
                binding.txtRegionOtherTitle.visibility = View.GONE
            }
        }
        return text
    }

    /************** binding methods ************************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onSendCooperationRequest() {
        viewModel.fileAllData.value?.data?.id?.let { fileId ->
            (activity as MainActivity).user.apply {
                viewModel.sendCooperationRequest(
                    requireActivity(),
                    this?.token,
                    this?.id,
                    fileId
                )
            }
        }
    }

    fun onEditFileClick() {

        viewModel.fileAllData.value?.data?.apply {
            val intent = Intent(requireActivity(), AddActivity::class.java)
            intent.putExtra(EXTRA_FILE_DETAIL, this)
            startActivity(intent)
        }
    }

    fun onDeleteFileClick() {
        showDialogWith2Actions(
            requireContext(),
            resources.getString(R.string.are_you_sure_about_delete_file),
            { _, _ ->
                (activity as MainActivity).user?.token?.apply {
                    viewModel.fileAllData.value?.data?.id?.let { fileId ->
                        viewModel.deleteFile(requireActivity(), this, fileId)
                    }
                }
            },
            { d, _ -> d.dismiss() }
        )
    }

    fun getUserImage(): String? {
        return viewModel.fileAllData.value?.data?.user?.profilePic
    }

    fun advertiserText(): String {
        val a = viewModel.fileAllData.value?.data?.let { fileDate ->
            fileDate.user?.let { user ->
                String.format("%s %s", user.firstName, user.lastName)
            }
        }
        return a ?: ""
    }

    fun realEstateText(): String {
        return viewModel.fileAllData.value?.data?.user?.realEstate
            ?: resources.getString(R.string.freelancer)
    }

    fun createAtText(): String {
        return viewModel.fileAllData.value?.data?.created_at
            ?: ""
    }

    fun typeText(): String {
        val a = viewModel.fileAllData.value?.data?.typeInfo?.let {
            try {
                resources.getString(
                    R.string.type_value,
                    it.category!!.title,
                    it.subCategory!!.title,
                    it.fileType!!.title
                )
            } catch (e: Exception) {
                handleSystemException(
                    lifecycleScope,
                    "${(requireActivity() as MainActivity).user?.id}, ${this.javaClass.name}, typeText, ",
                    e
                )
                null
            }
        }
        return a ?: ""
    }

    fun reginText(): String {
        val a = viewModel.fileAllData.value?.data?.let {
            it.locations[0].region.title
        }
        return a ?: ""
    }

    fun reginTagsText(): String {
        val a = viewModel.fileAllData.value?.data?.let {
            concatenateText(it.locations[0].region.tags)
        }
        return a ?: ""
    }

    fun otherRegionText(): String {
        return showOtherRegions(viewModel.fileAllData.value?.data?.locations)
    }

    ////////////////////////////////////////
    fun isShowAge(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowAgeField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun ageText(): String {
        val a = viewModel.fileAllData.value?.data?.let {
            return getPropertyPeriodsText(
                requireContext(),
                it.age,
                R.string.empty,
                R.string.empty
            )
        }
        return a ?: ""
    }

    fun isShowSize(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowSizeField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun sizeText(): String {
        val a = viewModel.fileAllData.value?.data?.let {
            getPropertyPeriodsText(
                requireContext(),
                it.size,
                R.string.empty,
                R.string.squere_meter
            )
        }
        return a ?: ""
    }

    fun isShowRooms(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowRoomsField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun roomNoText(): String {
        val a = viewModel.fileAllData.value?.data?.let {
            getPropertyPeriodsText(
                requireContext(),
                it.roomNo,
                R.string.empty,
                R.string.room
            )
        }
        return a ?: ""
    }

    fun isShowPrice(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowTotalPriceField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun priceText(): String {
        val a = viewModel.fileAllData.value?.data?.let {
            return getPropertyPeriodsPriceText(
                requireContext(),
                it.price,
                R.string.empty,
                R.string.tooman
            )
        }
        return a ?: ""
    }

    fun pricePerMeterText(): String {
        return (viewModel.fileAllData.value?.data?.let {
            if (isShowPrice() == View.VISIBLE)
                String.format(
                    "%s %s",
                    calculatePricePerMeter(
                        requireContext(),
                        viewModel.fileAllData.value!!.data!!.price,
                        viewModel.fileAllData.value!!.data!!.size
                    ),
                    resources.getString(R.string.tooman)
                )
            else
                ""
        }) ?: ""
    }

    fun isShowMortgage(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowMortgageField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun mortgageText(): String {
        return "جهت تکمیل backend"  // TODO
    }

    fun isShowRent(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowRentField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun rentText(): String {
        return "جهت تکمیل backend"  // TODO
    }

    fun isShowSuitableFor(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowSuitableForField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun suitableForText(): String {
        return "جهت تکمیل backend"  // TODO
    }

    fun isShowFloor(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowFloorField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun floorText(): String {
        return "جهت تکمیل backend"  // TODO
    }

    fun isShowParking(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowParkingField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun parkingText(): String {
        return "جهت تکمیل backend"  // TODO
    }

    fun isShowStoreRoom(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowStoreRoomField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun storeRoomText(): String {
        return "جهت تکمیل backend"  // TODO
    }

    fun isShowBalcony(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowBalconyField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun balconyText(): String {
        return "جهت تکمیل backend"  // TODO
    }

    fun isShowElevator(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowElevatorField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun elevatorText(): String {
        return "جهت تکمیل backend"  // TODO
    }

    fun isShowAdminDeed(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowAdminDeedField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun adminDeedText(): String {
        return "جهت تکمیل backend"  // TODO
    }

    fun isShowDeedType(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowDeedTypeField(data.category!!.id!!, data.subCategory!!.id!!))
                View.VISIBLE
            else
                View.GONE
        } ?: View.GONE
    }

    fun deedTypeText(): String {
        return "جهت تکمیل backend"  // TODO
    }

    fun isShowDescriptions(): Boolean {
        return !viewModel.fileAllData.value?.data?.description.isNullOrEmpty()
    }

    fun descriptionsText(): String {
        return viewModel.fileAllData.value?.data?.description ?: ""
    }
}