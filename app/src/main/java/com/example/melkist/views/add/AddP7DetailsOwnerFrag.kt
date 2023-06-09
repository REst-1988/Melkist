package com.example.melkist.views.add

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.melkist.AddActivity
import com.example.melkist.R
import com.example.melkist.databinding.DialogLayoutGetAddDetailsBinding
import com.example.melkist.databinding.FragAddP7DetailsOwnerBinding
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.getPersianYear
import com.example.melkist.utils.numInLetter
import com.example.melkist.utils.showDialogWithMessage
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.AddItemViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.math.BigDecimal


class AddP7DetailsOwnerFrag : Fragment() {
    private lateinit var binding: FragAddP7DetailsOwnerBinding
    private val viewModel: AddItemViewModel by activityViewModels()

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) startCrop(uri) else Log.e("TAG", "getContent URI is NULL: ")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("TAG", "onCreate: test 1")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        Log.e("TAG", "onCreate: test 2")
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
        Log.e("TAG", "onCreate: test 3")
        viewModel.saveResponse.observe(viewLifecycleOwner) {
            Log.e("TAG", "onViewCreated: ${it.result}")
            Log.e("TAG", "onViewCreated: ${it.message}")
            Log.e("TAG", "onViewCreated: ${concatenateText(it.errors)}")
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
    }

    private fun pickFromGallery() {
        getContent.launch("image/*")
    }

    private fun startCrop(imageUri: Uri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true).setAspectRatio(16, 9).start(requireContext(), this)
    }

    private fun showAgeDialog(title: String, observer: Observer<in String?>) {
        val result = MutableLiveData("")
        val binding =
            DialogLayoutGetAddDetailsBinding.inflate(LayoutInflater.from(requireContext()))
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(true)
        alertDialog.show()
        binding.txtTitle.text = title
        binding.etInput.filters = arrayOf(InputFilter.LengthFilter(4))
        binding.txtInLetters.visibility = View.GONE
        binding.btnConfirm.setOnClickListener {
            if (binding.etInput.text.isNotEmpty()) {
                if (isConditionsOk(binding.etInput.text.toString())) {
                    result.value = binding.etInput.text.toString()
                    alertDialog.cancel()
                } else {
                    showToast(
                        requireContext(), resources.getString(R.string.input_right_number)
                    )
                }
            } else {
                showToast(
                    requireContext(), resources.getString(R.string.on_empty_dialog_edittext_feild)
                )
            }
        }
        result.observe(viewLifecycleOwner, observer)
    }

    private fun isConditionsOk(input: String): Boolean {
        return input.toInt() <= getPersianYear() && input.toInt() >= 1150
    }

    private fun showInputDialog(title: String, unit: String?, observer: Observer<in String?>) {
        val result = MutableLiveData("")
        val binding =
            DialogLayoutGetAddDetailsBinding.inflate(LayoutInflater.from(requireContext()))
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(true)
        alertDialog.show()
        binding.txtTitle.text = String.format("%s (%s)", title, unit)
        binding.etInput.showSoftInputOnFocus
        binding.etInput.requestFocus()
        binding.etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                binding.etInput.removeTextChangedListener(this)
                val text: String = p0.toString()
                if (!TextUtils.isEmpty(text)) {
                    val value: Long = BigDecimal(text.replace(",", "")).toLong()
                    val format: String = formatNumber(BigDecimal(text.replace(",", "")).toDouble())
                    binding.etInput.setText(format)
                    binding.etInput.setSelection(format.length)
                    binding.txtInLetters.text = String.format(
                        "%s %s", numInLetter(requireContext(), value), unit
                    )
                }
                binding.etInput.addTextChangedListener(this)
            }
        })
        binding.btnConfirm.setOnClickListener {
            if (binding.etInput.text.isNotEmpty()) {
                result.value = binding.etInput.text.toString().replace(",", "")
                alertDialog.dismiss()
            } else {
                showToast(
                    requireContext(), resources.getString(R.string.on_empty_dialog_edittext_feild)
                )
            }
        }
        result.observe(viewLifecycleOwner, observer)
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
            viewModel.saveFile((activity as AddActivity).user.id!!)
        else
            showDialogWithMessage(
                requireContext(),
                resources.getString(R.string.complete_all_fields)
            ) { d, _ ->
                d.dismiss()
            }
    }

    fun onPickImage() {
        //showActionDialogForChoosingCameraOrStorage() // TODO: pic image from camera
        pickFromGallery()
    }

    fun onChoosingAge() {
        showAgeDialog(
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
        showInputDialog(resources.getString(R.string.price), resources.getString(R.string.tooman)) {
            if (!it.isNullOrEmpty()) {
                viewModel.priceFrom = it.toLong()
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

/** camera part UNSUCCESSFULL/////////////////////////////////////////////////**/
/*
in on viewCreated
cropActivityResultLauncher = registerForActivityResult(
ActivityResultContracts.StartActivityForResult()){ result ->
Log.e(TAG, "onViewCreated: check uri 1", )
if (result.resultCode == AppCompatActivity.RESULT_OK) {
//startCrop(result.data?.data!!)
Log.e(TAG, "onViewCreated: ${result.data?.data}",)
Log.e(TAG, "onViewCreated: ${result.data}",)
Log.e(TAG, "onViewCreated: ${result.data?.extras}",)
Log.e(TAG, "onViewCreated: ${result.data?.extras!!.get("data")}",)

val a = CropImage.getPickImageResultUri(requireContext(), result.data)

Log.e(TAG, "onViewCreated: ${a}",)
startCrop(a)
*/
/*                val bundle = result.data!!.extras
val bitmap = bundle!!.get("data") as Bitmap
//Log.e("TAG", "onCreate: $uri", )
binding.img3.setImageBitmap(bitmap)*//*
}
}

private fun showActionDialogForChoosingCameraOrStorage() {
val binding =
DialogLayoutCameraGaleryChoosingBinding
.inflate(LayoutInflater.from(requireContext()))
val alertDialog = AlertDialog.Builder(requireContext()).create()
alertDialog.setView(binding.root)
alertDialog.setCancelable(true)
alertDialog.show()
binding.chooseFromCamera.setOnClickListener{
checkCameraPermissionOrProceed()
alertDialog.cancel()
}
binding.chooseFromGalery.setOnClickListener{
pickFromGallery()
alertDialog.cancel()
}
}

private fun takePicture() {
val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
cropActivityResultLauncher.launch(intent)
}

private fun checkCameraPermission(): Boolean { // TODO: need to repair for getting permissions
return ContextCompat.checkSelfPermission(
requireContext(),
android.Manifest.permission.CAMERA
) == PackageManager.PERMISSION_GRANTED
}

private val requestCameraPermissionLauncher =
registerForActivityResult(
ActivityResultContracts.RequestPermission()
) { isGranted: Boolean ->
if (isGranted) {
takePicture()
} else {
Log.e("TAG", "TESSSST: 2")
}
}

private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Intent>

*/
/*

private fun checkCameraPermissionOrProceed() {
when {
checkCameraPermission() -> {
takePicture()
}
shouldShowRequestPermissionRationale(
android.Manifest.permission.CAMERA
) -> {
showRationaleDialog(resources.getString(R.string.camera)){ d, _ ->
requestCameraPermissionLauncher.launch(
android.Manifest.permission.CAMERA
)
d.dismiss()
}
}
else -> {
requestCameraPermissionLauncher.launch(
android.Manifest.permission.CAMERA
)
}
}
}*/
 ***/