package com.example.melkist.views.universal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.melkist.AddActivity
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.ImagePagerAdapter
import com.example.melkist.databinding.FragFileDetailBinding
import com.example.melkist.databinding.LayoutItemsTimeLineBinding
import com.example.melkist.models.Action
import com.example.melkist.models.FileActions
import com.example.melkist.models.FileActionsData
import com.example.melkist.models.FileData
import com.example.melkist.models.FileTypes
import com.example.melkist.models.Location
import com.example.melkist.models.Period
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.models.User
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.EXTRA_FILE_DETAIL
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.calculatePricePerMeter
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.getPropertyPeriodsPriceText
import com.example.melkist.utils.getPropertyPeriodsText
import com.example.melkist.utils.getStringDateByTimestamp
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
import com.example.melkist.views.universal.dialog.BottomSheetCreateAction

class FileDetailFrag : Fragment() {

    private lateinit var binding: FragFileDetailBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragFileDetailBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@FileDetailFrag
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initRequireComponnets()
        initListener()
        initObservers()

        // TODO: delete this
        Log.e("TAG", "onResume: " +
                "${viewModel.fileAllData.value?.data?.id}\n" +
                "${viewModel.fileAllData.value?.data?.locations?.get(0)?.region?.title}", )
    }

    private fun initRequireComponnets() {
        viewModel.fileAllData.value?.data?.apply {
            showProperViews(this)
            isFav?.apply {
                binding.ibtnBookmark.showFav(this)
            }
        }
    }

    private fun showProperViews(fileData: FileData) {
        fileData.typeInfo?.fileType?.let { idTitle ->
            if (idTitle.id == FileTypes().owner.id) {
                binding.viewPager.visibility = View.VISIBLE
                binding.indicator.visibility = View.VISIBLE
                showImagesForOwnerFile()
                viewModel.getActionsOfFile(requireActivity(), fileData.id)
            } else {
                binding.viewPager.visibility = View.GONE
                binding.indicator.visibility = View.GONE
            }
        }
    }

    private fun showProperActionButtons(fileUser: User?) {
        (activity as MainActivity).user?.let { user ->
            fileUser?.let { fileCreatorUser ->
                if (fileCreatorUser.id == user.id) {
                    binding.apply {
                        btnSendCooperationRequest.visibility = View.GONE
                        layoutBtnDeleteFile.visibility = View.VISIBLE
                        ibtnCreateAction.visibility = View.VISIBLE
                    }
                } else {
                    binding.apply {
                        btnSendCooperationRequest.visibility = View.VISIBLE
                        layoutBtnDeleteFile.visibility = View.GONE
                        ibtnCreateAction.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun showImagesForOwnerFile() {
        viewModel.fileAllData.value?.data?.images?.apply {
            binding.viewPager.adapter = ImagePagerAdapter(null, null, this)
            binding.indicator.setViewPager(binding.viewPager)
        }
    }

    private fun showTimeLine(fileActionsData: List<FileActionsData>?) {
        binding.llTimeLine.removeAllViewsInLayout()
        fileActionsData?.forEach { fileActions ->
            val actionLayout =
                LayoutItemsTimeLineBinding.inflate(LayoutInflater.from(requireContext()))
            actionLayout.txtActionTimeLine.text = fileActions.action?.actionTitle ?: ""
            actionLayout.txtDateTimeLine.text =
                getStringDateByTimestamp((fileActions.action?.actionDate ?: 0) * 10)
            actionLayout.txtAgentTimeLine.text =
                resources.getString(
                    R.string.applicant,
                    fileActions.performerUser?.firstName ?: "",
                    fileActions.performerUser?.lastName ?: "",
                    fileActions.performerUser?.realEstate ?: ""
                )
            binding.llTimeLine.addView(actionLayout.root)
        }
    }

    private fun initListener() {
        binding.ibtnBookmark.setOnClickListener {
            onBtnBookmarkClicked()
        }
        binding.ibtnShare.setOnClickListener {
            showToast(requireContext(), resources.getString(R.string.next_phase))
        }
    }

    private fun onBtnBookmarkClicked() {
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


    private fun initObservers() {
        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                ApiStatus.DONE -> showProperActionButtons(viewModel.fileAllData.value?.data?.user)
                else -> {
                    binding.apply {
                        layoutBtnDeleteFile.visibility = View.GONE
                        btnSendCooperationRequest.visibility = View.GONE
                    }
                }
            }
        }
        viewModel.cooperationResponse.observe(viewLifecycleOwner) { response ->
            handleCooperationResponse(response)
        }

        viewModel.saveFavResponse.observe(viewLifecycleOwner) { response ->
            handleSaveResponse(response)
        }

        viewModel.deleteFavResponse.observe(viewLifecycleOwner) { response ->
            handleDeleteFavResponse(response)
        }

        viewModel.deleteFileResponse.observe(viewLifecycleOwner) { response ->
            handleDeleteFileResponse(response)
        }

        viewModel.saveActionResponse.observe(viewLifecycleOwner) { response ->
            handleSaveActionResponse(response)
        }

        viewModel.fileActionsResponse.observe(viewLifecycleOwner) { response ->
            handleFileActionsResponse(response)
        }
    }

    private fun handleCooperationResponse(response: PublicResponseModel) {
        when (response.result) {
            true -> {
                response.message?.let {
                    showToast(
                        requireContext(), it
                    )
                }
                back()
                viewModel.resetCooperationResponse()
            }

            false -> {
                onRequestFalseResult(
                    requireActivity(), response.errors ?: UNKNOWN_ERRORS_LIST
                ) {}
                viewModel.resetCooperationResponse()
            }

            else -> {}
        }
    }

    private fun handleSaveResponse(response: PublicResponseModel) {
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
                    requireActivity(), response.errors ?: UNKNOWN_ERRORS_LIST
                ) {}
                viewModel.resetSaveResponse()
            }

            else -> Log.e(
                "TAG", "saveFavResponse: ${resources.getString(R.string.null_value)}"
            )
        }
    }

    private fun handleDeleteFavResponse(response: PublicResponseModel) {
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
                    requireActivity(), response.errors ?: UNKNOWN_ERRORS_LIST
                ) {}
                viewModel.resetDeleteFavResponse()
            }

            else -> {}
        }
    }

    private fun handleDeleteFileResponse(response: PublicResponseModel) {
        when (response.result) {
            true -> {
                back()
                viewModel.resetDeleteFileResponse()
            }

            false -> {
                onRequestFalseResult(
                    requireActivity(), response.errors ?: UNKNOWN_ERRORS_LIST
                ) {}
                viewModel.resetDeleteFileResponse()
            }

            null -> Log.e(
                "TAG", "initObservers: ${resources.getString(R.string.null_value)}"
            )
        }
    }

    private fun handleSaveActionResponse(response: PublicResponseModel) {
        when (response.result) {
            true -> {
                showToast(requireContext(), response.message?:"")
                viewModel.getActionsOfFile(
                    requireActivity(),
                    viewModel.fileAllData.value?.data?.id ?: 0
                )
                viewModel.resetSaveActionResponse()
            }

            false -> {
                onRequestFalseResult(
                    requireActivity(), response.errors ?: UNKNOWN_ERRORS_LIST
                ) {}
                viewModel.resetSaveActionResponse()
            }

            null -> Log.e(
                "TAG", "initObservers: ${resources.getString(R.string.null_value)}"
            )
        }
    }

    private fun handleFileActionsResponse(response: FileActions) {
        when (response.result) {
            true -> {
                showTimeLine(response.data)
                binding.scrollView.smoothScrollTo(0, binding.scrollView.bottom)
                // viewModel.resetFileActionsResponse() TODO: uncomment this
            }
            false -> {
                onRequestFalseResult(
                    requireActivity(), response.errors ?: UNKNOWN_ERRORS_LIST
                ) {}
                viewModel.resetFileActionsResponse()
            }

            else -> {}
        }
    }

    private fun showOtherRegions(locations: List<Location>?): String {
        var text = ""
        locations?.apply {
            if (size > 1) {
                binding.txtRegionOther.visibility = View.VISIBLE
                binding.txtRegionOtherTitle.visibility = View.VISIBLE
                val a = mutableListOf<String>()
                for (i in 1 until size) a.add(this[i].region.title)
                text = concatenateText(a)
            } else {
                binding.txtRegionOther.visibility = View.GONE
                binding.txtRegionOtherTitle.visibility = View.GONE
            }
        }
        return text
    }

    fun saveAction(action: Action, fileId: Int) {
        val user = (requireActivity() as MainActivity).user
        viewModel.saveAction(
            requireActivity(),
            user?.token,
            fileId,
            user?.id,
            action.id,
            action.actionDate,
            action.actionOwnerName,
            action.actionOwnerMobile
        )
    }

    /************** binding methods ************************/
    fun back() {
        findNavController().popBackStack()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun onCreateActionClick() {
        viewModel.fileAllData.value?.data?.id?.apply {
            val bottomSheetDialog = BottomSheetCreateAction(this@FileDetailFrag, this)
            bottomSheetDialog.show(childFragmentManager, bottomSheetDialog.tag)
        }
    }

    fun onSendCooperationRequest() {
        viewModel.fileAllData.value?.data?.id?.let { fileId ->
            (activity as MainActivity).user.apply {
                viewModel.sendCooperationRequest(
                    requireActivity(), this?.token, this?.id, fileId
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
        showDialogWith2Actions(requireContext(),
            resources.getString(R.string.are_you_sure_about_delete_file),
            { _, _ ->
                (activity as MainActivity).user?.token?.apply {
                    viewModel.fileAllData.value?.data?.id?.let { fileId ->
                        viewModel.deleteFile(requireActivity(), this, fileId)
                    }
                }
            },
            { d, _ -> d.dismiss() })
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
        return viewModel.fileAllData.value?.data?.created_at ?: ""
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
            if (isShowAgeField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun ageText(): String {
        val a = viewModel.fileAllData.value?.data?.age?.let {
            return getPropertyPeriodsText(
                requireContext(), it, R.string.empty, R.string.empty
            )
        }
        return a ?: ""
    }

    fun isShowSize(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowSizeField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun sizeText(): String {
        val a = viewModel.fileAllData.value?.data?.size?.let {
            getPropertyPeriodsText(
                requireContext(), it, R.string.empty, R.string.squere_meter
            )
        }
        return a ?: ""
    }

    fun isShowRooms(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowRoomsField(data.category?.id ?: 0, data.subCategory?.id ?: 0)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun roomNoText(): String {
        val a = viewModel.fileAllData.value?.data?.roomNo?.let {
            getPropertyPeriodsText(
                requireContext(), it, R.string.empty, R.string.room
            )
        }
        return a ?: ""
    }

    fun isShowPrice(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowTotalPriceField(
                    data.category?.id ?: 0,
                    data.subCategory?.id ?: 0
                )
            ) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun priceText(): String {
        val a = viewModel.fileAllData.value?.data?.let {
            getPropertyPeriodsPriceText(
                requireContext(), it.price ?: Period(0, 0), R.string.empty, R.string.tooman
            )
        }
        return a ?: ""
    }

    fun pricePerMeterText(): String {
        if (isShowPrice() == View.VISIBLE) {
            binding.txtPricePerMeter.visibility = View.VISIBLE
            binding.txtPricePerMeterTitle.visibility = View.VISIBLE
            return viewModel.fileAllData.value?.data?.let { data ->
                String.format(
                    "%s %s", calculatePricePerMeter(
                        requireContext(),
                        data.price ?: Period(0, 0),
                        viewModel.fileAllData.value?.data?.size ?: Period(0, 0)
                    ), resources.getString(R.string.tooman)
                )
            } ?: ""
        } else {
            binding.txtPricePerMeter.visibility = View.GONE
            binding.txtPricePerMeterTitle.visibility = View.GONE
            return ""
        }
    }

    fun isShowMortgage(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowMortgageField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun mortgageText(): String {
        return viewModel.fileAllData.value?.data?.mortgage?.let { data ->
            getPropertyPeriodsPriceText(
                requireContext(),
                data,
                R.string.empty,
                R.string.tooman
            )
        } ?: ""
    }

    fun isShowRent(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowRentField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun rentText(): String {
        return viewModel.fileAllData.value?.data?.rent?.let { data ->
            getPropertyPeriodsPriceText(
                requireContext(),
                data,
                R.string.empty,
                R.string.tooman
            )
        } ?: ""
    }

    fun isShowSuitableFor(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowSuitableForField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun suitableForText(): String {
        return resources
            .getStringArray(
                R.array.suitable_for_options
            )[viewModel.fileAllData.value?.data?.suitablefor ?: 0]
    }

    fun isShowFloor(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowFloorField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun floorText(): String {
        return resources
            .getStringArray(
                R.array.floor_list
            )[viewModel.fileAllData.value?.data?.floor ?: 0]
    }

    fun isShowParking(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowParkingField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun parkingText(): String {
        return viewModel.fileAllData.value?.data?.parking?.let {
            when (it) {
                true -> resources.getString(R.string.have)
                false -> resources.getString(R.string.dont_have)
            }
        } ?: ""
    }

    fun isShowStoreRoom(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowStoreRoomField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun storeRoomText(): String {
        return viewModel.fileAllData.value?.data?.storeRoom?.let {
            when (it) {
                true -> resources.getString(R.string.have)
                false -> resources.getString(R.string.dont_have)
            }
        } ?: ""
    }

    fun isShowBalcony(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowBalconyField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun balconyText(): String {
        return viewModel.fileAllData.value?.data?.balcony?.let {
            when (it) {
                true -> resources.getString(R.string.have)
                false -> resources.getString(R.string.dont_have)
            }
        } ?: ""
    }

    fun isShowElevator(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowElevatorField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun elevatorText(): String {
        return return viewModel.fileAllData.value?.data?.elevator?.let {
            when (it) {
                true -> resources.getString(R.string.have)
                false -> resources.getString(R.string.dont_have)
            }
        } ?: ""
    }

    fun isShowAdminDeed(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowAdminDeedField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun adminDeedText(): String {
        return return viewModel.fileAllData.value?.data?.adminDeed?.let {
            when (it) {
                true -> resources.getString(R.string.have)
                false -> resources.getString(R.string.dont_have)
            }
        } ?: ""
    }

    fun isShowDeedType(): Int {
        return viewModel.fileAllData.value?.data?.typeInfo?.let { data ->
            if (isShowDeedTypeField(data.category!!.id!!, data.subCategory!!.id!!)) View.VISIBLE
            else View.GONE
        } ?: View.GONE
    }

    fun deedTypeText(): String {
        return resources
            .getStringArray(
                R.array.deed_type_options
            )[viewModel.fileAllData.value?.data?.deedType ?: 0]
    }

    fun isShowDescriptions(): Boolean {
        return !viewModel.fileAllData.value?.data?.description.isNullOrEmpty()
    }

    fun descriptionsText(): String {
        return viewModel.fileAllData.value?.data?.description ?: ""
    }
}