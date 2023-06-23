package com.example.melkist.views.universal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.R
import com.example.melkist.databinding.FragFileDetailBinding
import com.example.melkist.models.FileDataResponse
import com.example.melkist.models.Location
import com.example.melkist.models.Period
import com.example.melkist.utils.DATA
import com.example.melkist.viewmodels.dialogviewmodel.FileDetailViewModel

class FileDetailFrag : Fragment() {

    private lateinit var binding: FragFileDetailBinding
    private val viewModel: FileDetailViewModel by viewModels ()
    private var data: FileDataResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            data = this.getSerializable(DATA) as FileDataResponse
        }

        data?.apply {
            Log.e("TAG", "onCreate: tst ${this.data!!.price}", )
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

    private fun showOtherRegions(locations: List<Location>): String {
        var text = ""
        if (locations.size > 1) {
            binding.txtRegionOther.visibility = View.VISIBLE
            for (i in 1 until locations.size) text += locations[i].region.title
        } else {
            binding.txtRegionOther.visibility = View.GONE
        }
        return text
    }

    private fun getPropertyPeriodsText(period: Period): String {
        val conjunctions = if (period.to == period.from) ""
        else resources.getString(R.string.to) + " " + period.to
        return String.format("%s %s", period.from, conjunctions)
    }

    private fun calculatePricePerMeter(price: Period, size: Period): String {
        return if (price.to == price.from) (price.from!! / size.from!!).toString()
        else (price.from!! / size.from!!).toString() + " " + resources.getString(R.string.to) + " " + price.to!! / size.to!!
    }

    /************** binding methods ************************/
    fun back () {
        findNavController().popBackStack()
    }

    fun onSendCooperationRequest() {
        TODO()
    }

    fun advertiserText(): String {
        return String.format("%s %s", data?.data!!.user.firstName, data?.data!!.user.lastName)
    }

    fun realEstateText(): String {
         TODO(": binding.txtRealEstate.text = TODO(\"getting user real estate data from Amir\")")
    }

    fun reginText(): String {
        return data?.data!!.locations[0].region.title
    }

    fun otherRegionText(): String {
        return showOtherRegions(data?.data!!.locations)
    }
    ////////////////////////////////////////
    fun ageText(): String {
        // TODO: Get age from server
        TODO()
    }
    fun sizeText(): String {
        return getPropertyPeriodsText(data?.data!!.size)
    }
    fun roomNoText(): String {
        return getPropertyPeriodsText(data?.data!!.roomNo)
    }
    fun priceText(): String {
        return getPropertyPeriodsText(data?.data!!.price)
    }
    fun pricePerMeterText(): String {
        return calculatePricePerMeter(data?.data!!.price, data?.data!!.size)
    }
    fun descriptionsText(): String {
        return data?.data!!.description?: ""
    }
}