package com.example.melkist.views.universal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.ImagePagerAdapter
import com.example.melkist.databinding.FragFileDetailBinding
import com.example.melkist.models.FileTypes
import com.example.melkist.models.Location
import com.example.melkist.utils.calculatePricePerMeter
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.getPropertyPeriodsPriceText
import com.example.melkist.utils.getPropertyPeriodsText
import com.example.melkist.utils.handleSystemException
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
                if (this.user.id == user.id) {
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
            isFav?.apply {
                binding.ibtnBookmark.showFav(this)
            }
        }
    }

    private fun initListener() {
        binding.ibtnBookmark.setOnClickListener {
            viewModel.fileAllData.value?.data?.isFav?.apply {
                if (this) {
                    (activity as MainActivity).user?.apply {
                        viewModel.deleteFavFile(
                            requireActivity(),
                            token!!,
                            id!!,
                            viewModel.fileAllData.value!!.data!!.id
                        )
                    }
                } else {
                    (activity as MainActivity).user?.apply {
                        viewModel.saveFavFile(
                            requireActivity(),
                            token!!,
                            id!!,
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
                    showToast(
                        requireContext(), concatenateText(response.errors)
                    )
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
                    showToast(
                        requireContext(), concatenateText(response.errors)
                    )
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
                    showToast(
                        requireContext(), concatenateText(response.errors)
                    )
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
                    showToast(requireContext(), concatenateText(response.errors))
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
            (activity as MainActivity).user?.apply {
                viewModel.sendCooperationRequest(
                    requireActivity(),
                    token!!,
                    id!!,
                    fileId
                )
            }
        }
    }

    fun funOnDeleteFileClick() {
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
        val a = viewModel.fileAllData.value?.data?.let {
            String.format("%s %s", it.user.firstName, it.user.lastName)
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
                handleSystemException(lifecycleScope, "${this.javaClass.name}, typeText, ", e)
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
            if (isShowPricePerMeter())
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

    fun isShowPricePerMeter(): Boolean {
        val a = viewModel.fileAllData.value?.data?.let {
            it.typeInfo!!.subCategory!!.id == 1 || it.typeInfo.subCategory!!.id == 2
        }
        return a ?: false
    }

    fun descriptionsText(): String {
        return viewModel.fileAllData.value?.data?.description ?: ""
    }
}