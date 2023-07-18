package com.example.melkist.views.universal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.ImagePagerAdapter
import com.example.melkist.databinding.FragFileDetailBinding
import com.example.melkist.models.FileDataResponse
import com.example.melkist.models.FileTypes
import com.example.melkist.models.Location
import com.example.melkist.models.Period
import com.example.melkist.utils.concatenateText
import com.example.melkist.utils.formatNumber
import com.example.melkist.utils.getPropertyPeriodsPriceText
import com.example.melkist.utils.getPropertyPeriodsText
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel

class FileDetailFrag : Fragment() {

    private lateinit var binding: FragFileDetailBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var data: FileDataResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("TAG", "onCreate test 1: ${viewModel.fileAllData.value}")
        if (viewModel.fileAllData.value == null) {
            showToast(requireContext(), resources.getString(R.string.error_getting_data))
            back()
        } else {
            data = viewModel.fileAllData.value!!
        }
    }

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
        data.data?.typeInfo?.fileType?.apply {
            if (id == FileTypes().owner.id) {
                binding.viewPager.visibility = View.VISIBLE
                binding.indicator.visibility = View.VISIBLE
                binding.viewPager.adapter = ImagePagerAdapter(null, data)
                binding.indicator.setViewPager(binding.viewPager)
            } else {
                binding.viewPager.visibility = View.GONE
                binding.indicator.visibility = View.GONE
            }
        }
        listenToCooperationResponse()
    }

    private fun listenToCooperationResponse() {
        viewModel.publicResponse.observe(viewLifecycleOwner) {
            //TODO: finish this
            Log.e("TAG", "listenToCooperationResponse: ${it.toString()}")
        }
    }

    private fun showOtherRegions(locations: List<Location>): String {
        var text = ""
        if (locations.size > 1) {
            binding.txtRegionOther.visibility = View.VISIBLE
            binding.txtRegionOtherTitle.visibility = View.VISIBLE
            val a = mutableListOf<String>()
            for (i in 1 until locations.size)
                a.add(locations[i].region.title)
            text = concatenateText(a)
        } else {
            binding.txtRegionOther.visibility = View.GONE
            binding.txtRegionOtherTitle.visibility = View.GONE
        }
        return text
    }

    private fun calculatePricePerMeter(price: Period, size: Period): String {
        val priceTo = (price.to ?: 0).toDouble()
        val priceFrom = (price.from ?: 0).toDouble()
        val sizeTo = (size.to ?: 1).toDouble()
        val sizeFrom = (size.from ?: 1).toDouble()
        return if (price.to == price.from)
            formatNumber(priceFrom / sizeFrom)
        else
            formatNumber(priceFrom / sizeFrom) + " " + resources.getString(R.string.to) + " " + formatNumber(
                priceTo / sizeTo
            )
    }

    /************** binding methods ************************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onSendCooperationRequest() {
        (activity as MainActivity).user?.apply {
            viewModel.sendCooperationRequest( // TODO: finish this part
                token!!,
                id!!,
                data.data!!.id
            )
        }
    }

    fun getUserImage(): String? {
        return data.data?.user?.profilePic
    }

    fun advertiserText(): String {
        return String.format("%s %s", data.data!!.user.firstName, data.data!!.user.lastName)
    }

    fun realEstateText(): String {
        return data.data!!.user.realEstate ?: resources.getString(R.string.freelancer)
    }

    fun reginText(): String {
        return data.data!!.locations[0].region.title
    }

    fun otherRegionText(): String {
        return showOtherRegions(data.data!!.locations)
    }

    ////////////////////////////////////////
    fun ageText(): String {
        return getPropertyPeriodsText(
            requireContext(),
            data.data!!.age,
            R.string.age_title,
            R.string.year
        )
    }

    fun sizeText(): String {
        return getPropertyPeriodsText(
            requireContext(),
            data.data!!.size,
            R.string.meterage,
            R.string.squere_meter
        )
    }

    fun roomNoText(): String {
        return getPropertyPeriodsText(
            requireContext(),
            data.data!!.roomNo,
            R.string.room,
            R.string.room
        )
    }

    fun priceText(): String {
        return getPropertyPeriodsPriceText(requireContext(), data.data!!.price)
    }

    fun pricePerMeterText(): String {
        return if (isShowPricePerMeter())
            String.format(
                "%s %s",
                calculatePricePerMeter(
                    data.data!!.price,
                    data.data!!.size
                ),
                resources.getString(R.string.tooman)
            )
        else
            ""
    }

    fun isShowPricePerMeter(): Boolean {
        return data.data!!.typeInfo!!.subCategory!!.id == 1 || data.data!!.typeInfo!!.subCategory!!.id == 2
    }

    fun descriptionsText(): String {
        return data.data!!.description ?: ""
    }
}