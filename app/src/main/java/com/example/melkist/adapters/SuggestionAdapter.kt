package com.example.melkist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.melkist.R
import com.example.melkist.databinding.ItemListPropertySuggestionBinding
import com.example.melkist.models.FileTypes
import com.example.melkist.models.Period
import com.example.melkist.models.SuggestionItemListModel
import com.example.melkist.utils.getPropertyPeriodsPriceText
import com.example.melkist.utils.isShowMortgageField
import com.example.melkist.utils.isShowTotalPriceField
import com.example.melkist.views.profile.ai.ProfileAiSuggestionsFrag

class SuggestionAdapter(private val fragment: Fragment) :
    ListAdapter<SuggestionItemListModel, SuggestionAdapter.SuggestionViewHolder>(DiffUtilCallBack) {

    companion object DiffUtilCallBack : DiffUtil.ItemCallback<SuggestionItemListModel>() {
        override fun areItemsTheSame(
            oldItem: SuggestionItemListModel, newItem: SuggestionItemListModel
        ): Boolean {
            return oldItem.myFile!!.id == newItem.myFile!!.id
        }

        override fun areContentsTheSame(
            oldItem: SuggestionItemListModel, newItem: SuggestionItemListModel
        ): Boolean {
            return oldItem.myFile!!.created_at == newItem.myFile!!.created_at
        }
    }

    class SuggestionViewHolder(
        private val fragment: Fragment, private val binding: ItemListPropertySuggestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SuggestionItemListModel) {
            binding.apply {
                data.myFile?.let { myFile ->
                    txtTypeMyFile.text = myFile.typeInfo?.fileType?.title ?: ""
                    if (myFile.typeInfo?.fileType?.id == FileTypes().seeker.id) curtainMyFile.setBackgroundColor(
                        fragment.resources.getColor(R.color.main_seeker_color)
                    )
                    else curtainMyFile.setBackgroundColor(fragment.resources.getColor(R.color.main_owner_color))
                    txtSubCatMyFile.text = myFile.typeInfo?.subCategory?.title ?: ""
                    txtCatMyFile.text = myFile.typeInfo?.category?.title ?: ""
                    txtRegionMyFile.text = myFile.locations[0].region.title

                    if (isShowTotalPriceField(
                            myFile.typeInfo?.category?.id ?: 0,
                            myFile.typeInfo?.subCategory?.id ?: 0
                        )
                    ) {
                        txtPriceMyFile.text = getPropertyPeriodsPriceText(
                            fragment.requireContext(),
                            myFile.price ?: Period(null, null),
                            R.string.price,
                            R.string.empty
                        )
                        txtRentMyFile.visibility = View.GONE
                    }
                    if (isShowMortgageField(
                            myFile.typeInfo?.category?.id ?: 0,
                            myFile.typeInfo?.subCategory?.id ?: 0
                        )
                    ) {

                        txtRentMyFile.visibility = View.VISIBLE
                        txtPriceMyFile.text = getPropertyPeriodsPriceText(
                            fragment.requireContext(),
                            myFile.mortgage ?: Period(null, null),
                            R.string.mortgage_amount,
                            R.string.empty
                        )
                        txtRentMyFile.text = getPropertyPeriodsPriceText(
                            fragment.requireContext(),
                            myFile.rent ?: Period(null, null),
                            R.string.rent_amount,
                            R.string.empty
                        )

                    }
                    cardTypeMyFile.setOnClickListener {
                        (fragment as ProfileAiSuggestionsFrag).onCardMyFileClick(myFile.id)
                    }
                }
                data.similarFile?.let { similarFile ->
                    similarFile.typeInfo?.apply {
                        txtTypeSimilarFile.text = fileType?.title ?: ""
                        if (this.fileType!!.id == FileTypes().seeker.id) curtainSimilarFile.setBackgroundColor(
                            fragment.resources.getColor(R.color.main_seeker_color)
                        )
                        else curtainSimilarFile.setBackgroundColor(fragment.resources.getColor(R.color.main_owner_color))
                        txtSubCatSimilarFile.text = subCategory?.title ?: ""
                        txtCatSimilarFile.text = category?.title ?: ""
                    }
                    txtRegionSimilarFile.text = similarFile.locations[0].region.title
                    if (isShowTotalPriceField(
                            similarFile.typeInfo?.category?.id ?: 0,
                            similarFile.typeInfo?.subCategory?.id ?: 0
                        )
                    ) {
                        txtPriceSimilarFile.text = getPropertyPeriodsPriceText(
                            fragment.requireContext(),
                            similarFile.price ?: Period(null, null),
                            R.string.price,
                            R.string.empty
                        )
                        txtRentSimilarFile.visibility = View.GONE
                    }
                    if (isShowMortgageField(
                            similarFile.typeInfo?.category?.id ?: 0,
                            similarFile.typeInfo?.subCategory?.id ?: 0
                        )
                    ) {

                        txtRentSimilarFile.visibility = View.VISIBLE
                        txtPriceSimilarFile.text = getPropertyPeriodsPriceText(
                            fragment.requireContext(),
                            similarFile.mortgage ?: Period(null, null),
                            R.string.mortgage_amount,
                            R.string.empty
                        )
                        txtRentSimilarFile.text = getPropertyPeriodsPriceText(
                            fragment.requireContext(),
                            similarFile.rent ?: Period(null, null),
                            R.string.rent_amount,
                            R.string.empty
                        )

                    }
                    cardTypeSimilarFile.setOnClickListener {
                        (fragment as ProfileAiSuggestionsFrag).inCardSimilarFileClick(similarFile.id)
                    }
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        return SuggestionViewHolder(
            fragment, ItemListPropertySuggestionBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val suggestion = getItem(position)
        holder.bind(suggestion)
    }
}