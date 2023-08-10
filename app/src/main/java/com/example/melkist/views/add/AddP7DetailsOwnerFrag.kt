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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.melkist.AddActivity
import com.example.melkist.R
import com.example.melkist.databinding.FragAddP7DetailsOwnerBinding
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.handleSystemException
import com.example.melkist.utils.showAgeDialog
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showInputDialog
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.AddItemViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class AddP7DetailsOwnerFrag : Fragment() {
    private lateinit var binding: FragAddP7DetailsOwnerBinding
    private val viewModel: AddItemViewModel by activityViewModels()

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
            viewModel.saveResponse.observe(viewLifecycleOwner) {
                when (it.result) {
                    true -> showDialogWithMessage(
                        requireContext(), it.message ?: ""
                    ) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        cancel()
                    }

                    false -> showDialogWithMessage(
                        requireContext(), concatenateText(it.errors)
                    ) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }

                    else -> showDialogWithMessage(
                        requireContext(), resources.getString(R.string.global_error)
                    ) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                }

            }
            viewModel.resetImages()
        }catch (e: Exception){
            handleSystemException(lifecycleScope, "AddP7DetailsOwnerFrag, onViewCreated", e)
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
        return viewModel.ageFrom != null
                && viewModel.sizeFrom != null
                && viewModel.roomFrom != null
                && viewModel.priceFrom != null
    }

    /************** binding commands **********************/
    fun back() {
        findNavController().navigate(R.id.action_addP7DetailsOwnerFrag_to_addP4LocationFrag)
    }

    fun cancel() {
        requireActivity().finish()
    }

    fun onCommit() {
        if (binding.etDescriptions.editText!!.text.isNotEmpty()
            || binding.etDescriptions.editText!!.text.toString() != ""
        ) viewModel.descriptions =
            binding.etDescriptions.editText!!.text.toString()
        if (isDataReady())
            viewModel.saveFile(requireActivity(), (activity as AddActivity).user.id!!)
        else
            showDialogWithMessage(
                requireContext(),
                resources.getString(R.string.complete_all_fields)
            ) { d, _ ->
                d.dismiss()
            }
    }

    fun onPickImage() {
        //showActionDialogForChoosingCameraOrStorage() // : pic image from camera
        pickFromGallery()
    }

    fun onChoosingAge() {
        showAgeDialog(
            requireContext(),
            viewLifecycleOwner,
            resources.getString(R.string.age_title)
        ) {
            if (!it.isNullOrEmpty()) {
                viewModel.ageFrom = it.toInt()
                binding.txtChooseAge.text = it
            }
        }
    }

    fun showAgeText(): String {
        return if (viewModel.ageFrom == null) {
            resources.getString(R.string.choose)
        } else {
            viewModel.sizeFrom.toString()
        }
    }

    fun onChoosingMeasure() {
        showInputDialog(
            requireContext(),
            viewLifecycleOwner,
            resources.getString(R.string.measurement), resources.getString(R.string.meter_squere)
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

    fun onChoosingRoomCount() {
        showInputDialog(
            requireContext(),
            viewLifecycleOwner,
            resources.getString(R.string.room_no), resources.getString(R.string.number)
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

    fun onChoosingPrice() {
        showInputDialog(
            requireContext(),
            viewLifecycleOwner,
            resources.getString(R.string.price),
            resources.getString(R.string.tooman)) {
            if (!it.isNullOrEmpty()) {
                viewModel.priceFrom =  it.toLong()
                binding.txtChoosePrice.text = formatNumber(it.toDouble())
            }
        }
    }

    fun showPriceText(): String {
        return if (viewModel.priceFrom == null) {
            resources.getString(R.string.enter)
        } else {
            formatNumber(viewModel.priceFrom!!.toDouble())
        }
    }

    /*****************************************************/
    var counter = 0
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
                    when (counter) {
                        0 -> {
                            binding.img2.load(imgUri) {
                                placeholder(R.drawable.loading_animation)
                                error(R.drawable.ic_broken_image)
                            }
                            viewModel.image2 = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver, imgUri
                            )
                        }

                        1 -> {
                            binding.img3.load(imgUri) {
                                placeholder(R.drawable.loading_animation)
                                error(R.drawable.ic_broken_image)
                            }
                            viewModel.image3 = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver, imgUri
                            )
                        }

                        2 -> {
                            binding.img4.load(imgUri) {
                                placeholder(R.drawable.loading_animation)
                                error(R.drawable.ic_broken_image)
                            }
                            viewModel.image4 = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver, imgUri
                            )
                        }

                        3 -> {
                            binding.img5.load(imgUri) {
                                placeholder(R.drawable.loading_animation)
                                error(R.drawable.ic_broken_image)
                            }
                            viewModel.image5 = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver, imgUri
                            )
                        }

                        4 -> {
                            binding.img6.load(imgUri) {
                                placeholder(R.drawable.loading_animation)
                                error(R.drawable.ic_broken_image)
                            }
                            viewModel.image6 = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver, imgUri
                            )
                        }

                        5 -> {
                            binding.img1.load(imgUri) {
                                placeholder(R.drawable.loading_animation)
                                error(R.drawable.ic_broken_image)
                            }
                            binding.imgAddImgAvatar.visibility = View.GONE
                            viewModel.image1 = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver, imgUri
                            )
                        }
                    }
                    counter++
                } else {
                    showToast(requireContext(), getString(R.string.somthing_goes_wrong))
                }

            }

            else -> {
                Log.e("TAG", "onActivityResult: else")
            }
        }
    }
}