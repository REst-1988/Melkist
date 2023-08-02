package com.example.melkist.views.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.bindingadapter.bindImage
import com.example.melkist.data.Ds
import com.example.melkist.data.UserDataStore
import com.example.melkist.databinding.FragProfilePicBinding
import com.example.melkist.models.PublicResponseModel
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.ProfilePicViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.launch


class ProfilePicFrag : Fragment() {

    private val viewModel: ProfilePicViewModel by viewModels()
    lateinit var binding: FragProfilePicBinding
    private lateinit var userDataStore: UserDataStore

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) startCrop(uri) else Log.e("TAG", "getContent URI is NULL: ")
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        userDataStore = Ds.getDataStore(requireContext())
        binding = FragProfilePicBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfilePicFrag
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) { user ->
            viewModel.profileImage = user.profilePic
            viewModel.isFirstTime = user.isFirstTime
            viewModel.userId = user.id
            viewModel.token = user.token
            viewModel.userFirstName = user.firstName
            viewModel.userLastName = user.lastName
            // TODO    viewModel.txtNickname = user.nickname
            checkIfHasProfilePicAndAllocate()
        }
        listenToUploadResponse()
    }

    private fun checkIfHasProfilePicAndAllocate() {
        if (viewModel.isFirstTime == false && !viewModel.profileImage.isNullOrEmpty()){
            bindImage(binding.imgUser, viewModel.profileImage)
            readyViewForChangingProfilePic()
        }else{
            readyViewForAddingProfilePic()
        }
    }

    private fun readyViewForChangingProfilePic() {
        binding.apply {
            imgAddEdit.setImageResource(R.drawable.baseline_edit_24)
            txtFirstLastName.text = String.format(
                "%s: %s %s",
                resources.getString(R.string.first_name_last_name_title),
                viewModel.userFirstName,
                viewModel.userLastName
            )
            viewModel.txtNickname?.apply {
                txtNickName.setText(this)
            } // TODO: create nick name
            txtPlain.visibility = View.INVISIBLE
            txtFirstLastName.visibility = View.VISIBLE
            txtNickName.visibility = View.VISIBLE // TODO: create nick name
            btnCommit.text = resources.getString(R.string.commit_changes)
        }
    }

    private fun readyViewForAddingProfilePic() {
        binding.apply {
            imgAddEdit.setImageResource(R.drawable.ic_baseline_add_box_24)
            txtFirstLastName.visibility = View.INVISIBLE
            txtNickName.visibility = View.INVISIBLE // TODO: create nick name
            txtPlain.visibility = View.VISIBLE
            btnCommit.text = resources.getString(R.string.commit)
        }
    }

    private fun listenToUploadResponse() {
        viewModel.uploadResponse.observe(viewLifecycleOwner){ response ->
            Log.e("TAG", "listenToUploadResponse: $response , ${response.result} , ${response.message}", )
            when (response.result) {
                true -> {
                    saveImage(response)
                    startNextMovement()
                }
                false -> {
                    showToast(requireContext(), concatenateText(response.errors))
                }
                else -> {
                    Log.e("TAG", "listenToCheckVerificationResult: ${resources.getString(R.string.somthing_goes_wrong)}", )
                }
            }
        }
    }

    private fun saveImage (response: PublicResponseModel) {
        lifecycleScope.launch {
            userDataStore.saveImage(
                requireContext(),
                response
            )
        }
    }

    private fun startNextMovement() {
        if (activity is MainActivity){
            back()
        }else{
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun pickFromGallery() {
        getContent.launch("image/*")
    }

    private fun startCrop(imageUri: Uri) {
        CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setFixAspectRatio(true)
            .start(requireContext(), this)
    }

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
                    binding.imgUser.load(imgUri) {
                        placeholder(R.drawable.loading_animation)
                        error(R.drawable.ic_broken_image)
                    }
                    viewModel.imgUser = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver, imgUri
                    )
                } else {
                    showToast(requireContext(), getString(R.string.somthing_goes_wrong))
                }

            }
            else -> {
                Log.e("TAG", "onActivityResult: else")
            }
        }
    }

    /*************************** binding **********************************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onSend() {
        if (viewModel.imgUser != null){ // TODO: after adding nick name, changing nickname may okay for saving
            binding.txtPlain.setTextColor(resources.getColor(R.color.normal_text_color))
            viewModel.uploadProfilePic(requireActivity())
        } else {
            binding.txtPlain.setTextColor(Color.RED)
            showToast(requireContext(), resources.getString(R.string.make_sure_choose_profile_pic))
        }
    }

    fun onProfilePicClick() {
        pickFromGallery()
    }
}