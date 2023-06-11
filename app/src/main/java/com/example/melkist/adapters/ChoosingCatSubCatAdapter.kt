package com.example.melkist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melkist.R
import com.example.melkist.databinding.ItemListCatSubCatBinding
import com.example.melkist.models.CatSubCatModel
import com.example.melkist.utils.isSystemDarkMode
import com.example.melkist.viewmodels.AddItemViewModel
import com.example.melkist.viewmodels.ChooseCatSubCatViewModel
import com.example.melkist.viewmodels.MapViewModel
import com.example.melkist.views.universal.ChooseCatSubcatFrag

class ChoosingCatSubCatAdapter(val viewModel: ChooseCatSubCatViewModel, val fragment: Fragment):
    ListAdapter<CatSubCatModel, ChoosingCatSubCatAdapter.CatSubCatViewHolder>(DiffUtilCallBack){
    companion object DiffUtilCallBack : DiffUtil.ItemCallback<CatSubCatModel>() {
        override fun areItemsTheSame(oldItem: CatSubCatModel, newItem: CatSubCatModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CatSubCatModel, newItem: CatSubCatModel): Boolean {
            return oldItem.title == newItem.title
        }
    }

    class CatSubCatViewHolder(private val context: Context, private var binding: ItemListCatSubCatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CatSubCatModel){
            binding.txt.text = data.title
            loadImg(data)
            binding.executePendingBindings()
        }

        private fun loadImg(data: CatSubCatModel){
            val imgUrl = if (isSystemDarkMode(context)) data.nightIconUrl else data.dayIconUrl
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            binding.img.load(imgUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatSubCatViewHolder {
        return CatSubCatViewHolder(
            fragment.requireContext(),
            ItemListCatSubCatBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CatSubCatViewHolder, position: Int) {
        val catSubCat = getItem(position)
        holder.bind(catSubCat)
        holder.itemView.setOnClickListener{
            choosingItemAction(catSubCat)
            //TODO: according to changes view model should be chooseCatSubCatViewModel
            // also chooseCatSubCatViewModel must be change
            if ((viewModel as AddItemViewModel).getReqSource() == AddItemViewModel.ReqSource.CATEGORY)
                viewModel.resetAddItemFieldsByChoosingCategory()
            (fragment as ChooseCatSubcatFrag).back()
        }
    }


    private fun choosingItemAction(catSubCat: CatSubCatModel){
        //TODO: according to changes view model should be chooseCatSubCatViewModel
        // also chooseCatSubCatViewModel must be change
        when ((viewModel as AddItemViewModel).getReqSource()) {
            AddItemViewModel.ReqSource.CATEGORY -> {
                viewModel.catId = catSubCat.id
                viewModel.catTitle = catSubCat.title
            }
            AddItemViewModel.ReqSource.SUB_CATEGORY -> {
                viewModel.subCatId = catSubCat.id
                viewModel.subCatTitle = catSubCat.title
            }
        }
    }
}
