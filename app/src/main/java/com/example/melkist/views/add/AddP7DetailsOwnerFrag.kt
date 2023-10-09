package com.example.melkist.views.add

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.melkist.AddActivity
import com.example.melkist.R
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.databinding.FragAddP7DetailsOwnerBinding
import com.example.melkist.utils.ADMIN_DEED_TAG
import com.example.melkist.utils.BALCONY_TAG
import com.example.melkist.utils.DATA
import com.example.melkist.utils.DEED_TYPE_TAG
import com.example.melkist.utils.ELEVATOR_TAG
import com.example.melkist.utils.FLOOR_TAG
import com.example.melkist.utils.MAX_IMAGE_NUMBER_FOR_SAVING_FIVE
import com.example.melkist.utils.PARKING_TAG
import com.example.melkist.utils.STORE_ROOM_TAG
import com.example.melkist.utils.SUITABLE_FOR_TAG
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.formatNumber
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
import com.example.melkist.utils.isShowStoreRoomField
import com.example.melkist.utils.isShowSuitableForField
import com.example.melkist.utils.isShowTotalPriceField
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.utils.showAgeDialog
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showInputDialog
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.AddItemViewModel
import com.example.melkist.views.universal.dialog.BottomSheetUniversalList
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.net.URL


class AddP7DetailsOwnerFrag : Fragment() {
    private lateinit var binding: FragAddP7DetailsOwnerBinding
    private val viewModel: AddItemViewModel by activityViewModels()
    private lateinit var imageViews: List<ImageView>
    private lateinit var imageButtons: List<ImageButton>

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) startCrop(uri) else Log.e("TAG", "getContent URI is NULL: ")
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragAddP7DetailsOwnerBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@AddP7DetailsOwnerFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            imageViews = listOf(
                binding.img2,
                binding.img3,
                binding.img4,
                binding.img5,
                binding.img6,
                binding.img1
            )
            imageButtons = listOf(
                binding.btnDeleteImg2,
                binding.btnDeleteImg3,
                binding.btnDeleteImg4,
                binding.btnDeleteImg5,
                binding.btnDeleteImg6,
                binding.btnDeleteImg1
            )
            viewModel.listOfImageUrls?.apply {
                Log.e("TAG", "onViewCreated: test 1 bitmap list")
                showImagesForEditing(this)
                Log.e("TAG", "onViewCreated: test 2 bitmap list")
            }
            viewModel.extra?.apply {
                binding.txtTitle.text = resources.getString(R.string.edit_detail_page_title)
            }
            viewModel.saveResponse.observe(viewLifecycleOwner) {
                when (it.result) {
                    true -> showDialogWithMessage(
                        requireContext(), it.message ?: ""
                    ) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        cancel()
                    }

                    false -> onRequestFalseResult(
                        requireActivity(), it.errors ?: UNKNOWN_ERRORS_LIST
                    ) {}

                    else -> showDialogWithMessage(
                        requireContext(), resources.getString(R.string.global_error)
                    ) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                }

            }
            viewModel.bitmapList.clear()
        } catch (e: Exception) {
            handleSystemException(
                lifecycleScope,
                "${(activity as AddActivity).user?.id}, AddP7DetailsOwnerFrag, onViewCreated",
                e
            )
        }
    }

    private fun pickFromGallery() {
        getContent.launch("image/*")
    }

    private fun startCrop(imageUri: Uri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true).setAspectRatio(16, 9).start(requireContext(), this)
    }

    private fun showRationaleDialog(source: String, posAction: (DialogInterface, Int) -> Unit) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("${resources.getString(R.string.need_permission)} $source")
            .setCancelable(true).setPositiveButton(R.string.confirm_permission, posAction)
            .setNegativeButton(resources.getString(R.string.deny_permission)) { d, _ ->
                d.dismiss()
            }.show()
    }

    private fun isDataReady(): Boolean {
        val age = if (isShowAgeField(viewModel.catId, viewModel.subCatId))
            viewModel.ageFrom != null
        else
            true
        val rooms = if (isShowRoomsField(viewModel.catId, viewModel.subCatId))
            viewModel.roomFrom != null
        else
            true
        val totalPrice = if (isShowTotalPriceField(viewModel.catId, viewModel.subCatId))
            viewModel.totalPriceFrom != null
        else
            true
        val mortgage = if (isShowMortgageField(viewModel.catId, viewModel.subCatId))
            viewModel.mortgageFrom != null
        else
            true
        val rent = if (isShowRentField(viewModel.catId, viewModel.subCatId))
            viewModel.rentFrom != null
        else
            true
        val suitableFor = if (isShowSuitableForField(viewModel.catId, viewModel.subCatId))
            viewModel.suitableFor != null // TODO: check if what is backend needed
        else
            true
        val floor = if (isShowFloorField(viewModel.catId, viewModel.subCatId))
            viewModel.floorFrom != null
        else
            true
        val parking = if (isShowParkingField(viewModel.catId, viewModel.subCatId))
            viewModel.parking != null
        else
            true
        val storeRoom = if (isShowStoreRoomField(viewModel.catId, viewModel.subCatId))
            viewModel.storeRoom != null
        else
            true
        val balcony = if (isShowBalconyField(viewModel.catId, viewModel.subCatId))
            viewModel.balcony != null
        else
            true
        val elevator = if (isShowElevatorField(viewModel.catId, viewModel.subCatId))
            viewModel.elevator != null
        else
            true
        val adminDeed = if (isShowAdminDeedField(viewModel.catId, viewModel.subCatId))
            viewModel.administrativeDeed != null
        else
            true
        val deedType = if (isShowDeedTypeField(viewModel.catId, viewModel.subCatId))
            viewModel.deedType != null
        else
            true
        return true /* TODO: must uncomment this after completing database
         age && rooms && totalPrice &&
         mortgage && rent && suitableFor && floor &&
        parking && storeRoom && balcony && elevator && adminDeed && deedType*/
    }

    private fun showImagesForEditing(imgList: List<String?>) {
        imgList.forEachIndexed { index, imgUrl ->
            imgUrl?.apply {
                bindImage(imageViews[index], imgUrl)
                imageButtons[index].visibility = View.VISIBLE
                viewModel.addImageUrlToBitmap(URL(imgUrl))
                if (index == MAX_IMAGE_NUMBER_FOR_SAVING_FIVE) {
                    binding.imgAddImgAvatar.visibility = View.GONE
                    binding.layoutImg1.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun clearImage(index: Int) {
        Log.e("TAG", "clearImage: bitmapList.size = ${viewModel.bitmapList.size}")
        viewModel.bitmapList.removeAt(index)
        Log.e("TAG", "clearImage: bitmapList.size = ${viewModel.bitmapList.size}")
        imageViews.forEach {
            it.setImageDrawable(null)
        }
        imageButtons.forEach {
            it.visibility = View.GONE
        }
        viewModel.bitmapList.forEachIndexed { i, bitmap ->
            imageViews[i].setImageBitmap(bitmap)
            imageButtons[i].visibility = View.VISIBLE
        }
        if (viewModel.bitmapList.size < MAX_IMAGE_NUMBER_FOR_SAVING_FIVE + 1) {
            binding.layoutImg1.visibility = View.INVISIBLE
            binding.imgAddImgAvatar.visibility = View.VISIBLE
        }
    }

    /************** binding commands **********************/
    fun back() {
        findNavController().navigate(R.id.action_addP7DetailsOwnerFrag_to_addP4LocationFrag)
    }

    fun cancel() {
        requireActivity().finish()
    }

    fun onCommit() {
        if (binding.etDescriptions.editText!!.text.isNotEmpty() ||
            binding.etDescriptions.editText!!.text.toString() != ""
        )
            viewModel.descriptions = binding.etDescriptions.editText!!.text.toString()
        if (isDataReady()) (activity as AddActivity).user.apply {
            viewModel.saveFile(requireActivity(), this)
        }
        else showDialogWithMessage(
            requireContext(), resources.getString(R.string.complete_all_fields)
        ) { d, _ ->
            d.dismiss()
        }
    }

    fun onPickImage() {
        //showActionDialogForChoosingCameraOrStorage() // : pic image from camera
        pickFromGallery()
    }

    fun onImg1DeleteClick() {
        clearImage(5)
    }

    fun onImg2DeleteClick() {
        clearImage(0)
    }

    fun onImg3DeleteClick() {
        clearImage(1)
    }

    fun onImg4DeleteClick() {
        clearImage(2)
    }

    fun onImg5DeleteClick() {
        clearImage(3)
    }

    fun onImg6DeleteClick() {
        clearImage(4)
    }

    fun isShowAge(): Int {
        return if (viewModel.isShowAgeField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingAge() {
        showAgeDialog(
            requireContext(), viewLifecycleOwner, resources.getString(R.string.age_title)
        ) {
            if (!it.isNullOrEmpty()) {
                viewModel.ageFrom = it.toInt()
                binding.txtChooseAge.text = it
            }
        }
    }

    fun showAgeText(): String {
        return if (viewModel.ageFrom == null) {
            resources.getString(R.string.enter)
        } else {
            viewModel.sizeFrom.toString()
        }
    }

    fun isShowSize(): Int {
        return if (viewModel.isShowSizeField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingSize() {
        showInputDialog(
            requireContext(),
            viewLifecycleOwner,
            resources.getString(R.string.measurement),
            resources.getString(R.string.meter_squere),
            6
        ) {
            if (!it.isNullOrEmpty()) {
                viewModel.sizeFrom = it.toInt()
                binding.txtChooseMeasurement.text = it
            }
        }
    }

    fun showMeasureText(): String {
        return if (viewModel.sizeFrom == null) {
            resources.getString(R.string.enter)
        } else {
            viewModel.sizeFrom.toString()
        }
    }

    fun isShowRooms(): Int {
        return if (viewModel.isShowRoomsField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingRoomCount() {
        showInputDialog(
            requireContext(),
            viewLifecycleOwner,
            resources.getString(R.string.room_no),
            resources.getString(R.string.number),
            1
        ) {
            if (!it.isNullOrEmpty()) {
                viewModel.roomFrom = it.toInt()
                binding.txtChooseRoomCo.text = it
            }
        }
    }

    fun showRoomCoText(): String {
        return if (viewModel.roomFrom == null) {
            resources.getString(R.string.enter)
        } else {
            viewModel.roomFrom.toString()
        }
    }

    fun isShowTotalPrice(): Int {
        return if (viewModel.isShowTotalPriceField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingPrice() {
        showInputDialog(
            requireContext(),
            viewLifecycleOwner,
            resources.getString(R.string.total_price),
            resources.getString(R.string.tooman),
            null
        ) {
            if (!it.isNullOrEmpty()) {
                viewModel.totalPriceFrom = it.toLong()
                binding.txtChoosePrice.text = formatNumber(it.toLong())
            }
        }
    }

    fun showPriceText(): String {
        return if (viewModel.totalPriceFrom == null) {
            resources.getString(R.string.enter)
        } else {
            formatNumber(viewModel.totalPriceFrom!!)
        }
    }

    ///////////////////////////////////////////////////////////////
    fun isShowMortgage(): Int {
        return if (viewModel.isShowMortgageField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingMortgage() {
        showInputDialog(
            requireContext(),
            viewLifecycleOwner,
            resources.getString(R.string.mortgage_amount),
            resources.getString(R.string.tooman),
            17
        ) {
            if (!it.isNullOrEmpty()) {
                viewModel.mortgageFrom = it.toLong()
                binding.txtChooseMortgage.text = formatNumber(it.toLong())
            }
        }
    }

    fun showMortgageText(): String {
        return if (viewModel.mortgageFrom == null) {
            resources.getString(R.string.enter)
        } else {
            formatNumber(viewModel.mortgageFrom!!)
        }
    }

    fun isShowRent(): Int {
        return if (viewModel.isShowRentField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingRent() {
        showInputDialog(
            requireContext(),
            viewLifecycleOwner,
            resources.getString(R.string.rent_amount),
            resources.getString(R.string.tooman),
            15
        ) {
            if (!it.isNullOrEmpty()) {
                viewModel.rentFrom = it.toLong()
                binding.txtChooseRent.text = formatNumber(it.toLong())
            }
        }
    }

    fun showRentText(): String {
        return if (viewModel.rentFrom == null) {
            resources.getString(R.string.enter)
        } else {
            formatNumber(viewModel.rentFrom!!)
        }
    }

    fun isShowSuitableFor(): Int {
        return if (viewModel.isShowSuitableForField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingSuitableFor() {
        val bottomFrag = BottomSheetUniversalList(
            resources.getStringArray(R.array.suitable_for_options).toList()
        )
        bottomFrag.show(childFragmentManager, SUITABLE_FOR_TAG)
        bottomFrag.setFragmentResultListener(SUITABLE_FOR_TAG) { _, positionBundle ->
            viewModel.suitableFor = positionBundle.getInt(DATA)
            viewModel.suitableForText = resources.getStringArray(
                R.array.suitable_for_options
            )[positionBundle.getInt(DATA)]
            binding.txtChooseSuitableFor.text = viewModel.suitableForText
        }
    }

    fun showSuitableForText(): String {
        return if (viewModel.suitableForText == null) resources.getString(R.string.choose)
        else viewModel.suitableForText!!
    }

    fun isShowFloor(): Int {
        return if (viewModel.isShowFloorField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingFloor() {
        val bottomFrag =
            BottomSheetUniversalList(resources.getStringArray(R.array.floor_list).toList())
        bottomFrag.show(childFragmentManager, FLOOR_TAG)
        bottomFrag.setFragmentResultListener(FLOOR_TAG) { _, positionBundle ->
            viewModel.floorFromText =
                resources.getStringArray(R.array.floor_list)[positionBundle.getInt(DATA)]
            viewModel.floorFrom = positionBundle.getInt(DATA)
            binding.txtChooseFloor.text = showFloorText()
        }
    }

    fun showFloorText(): String {
        return if (viewModel.floorFromText == null) resources.getString(R.string.choose)
        else viewModel.floorFromText!!
    }

    fun isShowParking(): Int {
        return if (viewModel.isShowParkingField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingParking() {
        val bottomFrag = BottomSheetUniversalList(
            resources.getStringArray(R.array.have_dont_have_options).toList()
        )
        bottomFrag.show(childFragmentManager, PARKING_TAG)
        bottomFrag.setFragmentResultListener(PARKING_TAG) { _, positionBundle ->
            viewModel.parking = positionBundle.getInt(DATA) != 0
            binding.txtChooseParking.text = showParkingText()
        }
    }

    fun showParkingText(): String {
        return when (viewModel.parking) {
            null -> resources.getString(R.string.choose)
            true -> resources.getString(R.string.have)
            else -> resources.getString(R.string.dont_have)
        }
    }

    fun isShowStoreRoom(): Int {
        return if (viewModel.isShowStoreRoomField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingStoreRoom() {
        val bottomFrag = BottomSheetUniversalList(
            resources.getStringArray(R.array.have_dont_have_options).toList()
        )
        bottomFrag.show(childFragmentManager, STORE_ROOM_TAG)
        bottomFrag.setFragmentResultListener(STORE_ROOM_TAG) { _, positionBundle ->
            viewModel.storeRoom = positionBundle.getInt(DATA) != 0
            binding.txtChooseStoreRoom.text = showStoreRoomText()
        }
    }

    fun showStoreRoomText(): String {
        return when (viewModel.storeRoom) {
            null -> resources.getString(R.string.choose)
            true -> resources.getString(R.string.have)
            else -> resources.getString(R.string.dont_have)
        }
    }

    fun isShowBalcony(): Int {
        return if (viewModel.isShowBalconyField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingBalcony() {
        val bottomFrag = BottomSheetUniversalList(
            resources.getStringArray(R.array.have_dont_have_options).toList()
        )
        bottomFrag.show(childFragmentManager, BALCONY_TAG)
        bottomFrag.setFragmentResultListener(BALCONY_TAG) { _, positionBundle ->
            viewModel.balcony = positionBundle.getInt(DATA) != 0
            binding.txtChooseBalcony.text = showBalconyText()
        }
    }

    fun showBalconyText(): String {
        return when (viewModel.balcony) {
            null -> resources.getString(R.string.choose)
            true -> resources.getString(R.string.have)
            else -> resources.getString(R.string.dont_have)
        }
    }

    fun isShowElevator(): Int {
        return if (viewModel.isShowElevatorField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingElevator() {
        val bottomFrag = BottomSheetUniversalList(
            resources.getStringArray(R.array.have_dont_have_options).toList()
        )
        bottomFrag.show(childFragmentManager, ELEVATOR_TAG)
        bottomFrag.setFragmentResultListener(ELEVATOR_TAG) { _, positionBundle ->
            viewModel.elevator = positionBundle.getInt(DATA) != 0
            binding.txtChooseElevator.text = showElevatorText()
        }
    }

    fun showElevatorText(): String {
        return when (viewModel.elevator) {
            null -> resources.getString(R.string.choose)
            true -> resources.getString(R.string.have)
            else -> resources.getString(R.string.dont_have)
        }
    }

    fun isShowAdminDeed(): Int {
        return if (viewModel.isShowAdminDeedField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingAdministrativeDeed() {
        val bottomFrag = BottomSheetUniversalList(
            resources.getStringArray(R.array.have_dont_have_options).toList()
        )
        bottomFrag.show(childFragmentManager, ADMIN_DEED_TAG)
        bottomFrag.setFragmentResultListener(ADMIN_DEED_TAG) { _, positionBundle ->
            viewModel.administrativeDeed = positionBundle.getInt(DATA) != 0
            binding.txtChooseAdministrativeDeed.text = showAdministrativeDeedText()
        }
    }

    fun showAdministrativeDeedText(): String {
        return when (viewModel.administrativeDeed) {
            null -> resources.getString(R.string.choose)
            true -> resources.getString(R.string.have)
            else -> resources.getString(R.string.dont_have)
        }
    }

    fun isShowDeedType(): Int {
        return if (viewModel.isShowDeedTypeField())
            View.VISIBLE
        else
            View.GONE
    }

    fun onChoosingDeedType() {
        val bottomFrag = BottomSheetUniversalList(
            resources.getStringArray(R.array.deed_type_options).toList()
        )
        bottomFrag.show(childFragmentManager, DEED_TYPE_TAG)
        bottomFrag.setFragmentResultListener(DEED_TYPE_TAG) { _, positionBundle ->
            viewModel.deedTypeText = resources.getStringArray(
                R.array.deed_type_options
            )[positionBundle.getInt(DATA)]
            viewModel.deedType = positionBundle.getInt(DATA)
            binding.txtChooseDeedType.text = showDeedTypeText()
        }
    }

    fun showDeedTypeText(): String {
        return if (viewModel.deedTypeText == null)
            resources.getString(R.string.choose)
        else
            viewModel.deedTypeText!!
    }

//////////////////////////////////////////////////////////////////
    /*****************************************************/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            showToast(requireContext(), getString(R.string.somthing_goes_wrong))
            return
        }
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val imgUri: Uri = result.uri
                    viewModel.bitmapList.add(
                        MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver, imgUri
                        )
                    )
                    imageViews[viewModel.bitmapList.size - 1].load(imgUri) {
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_broken_image)
                    }
                    imageButtons[viewModel.bitmapList.size - 1].visibility = View.VISIBLE
                    if (viewModel.bitmapList.size - 1 == MAX_IMAGE_NUMBER_FOR_SAVING_FIVE) {
                        binding.layoutImg1.visibility = View.VISIBLE
                        binding.imgAddImgAvatar.visibility = View.GONE

                    }
                } else
                    showToast(requireContext(), getString(R.string.no_data))
            }

            else -> Log.e("TAG", "onActivityResult: else")
        }
    }
}