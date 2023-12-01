package com.example.melkist.adapters.bindingadapter

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.melkist.R
import com.example.melkist.adapters.ChoosingCatSubCatAdapter
import com.example.melkist.adapters.ChoosingPcrsAdapter
import com.example.melkist.adapters.FavListAdapter
import com.example.melkist.adapters.MyFilesAdapter
import com.example.melkist.adapters.SuggestionAdapter
import com.example.melkist.adapters.TeamMemberAdapter
import com.example.melkist.models.CatSubCatModel
import com.example.melkist.models.Fav
import com.example.melkist.models.FileData
import com.example.melkist.models.PcrsData
import com.example.melkist.models.SuggestionItemListModel
import com.example.melkist.models.User
import com.example.melkist.utils.ApiStatus
import com.example.melkist.utils.USER_AVATAR
import com.example.melkist.utils.getTimeStampForLoadImages

@BindingAdapter("imgUrl")
fun bindImage(imageView: ImageView, url: String?) {
    url?.let {
        val a = "$url?d=${getTimeStampForLoadImages()}"
        val imgUri = a.toUri().buildUpon().scheme("https").build()
        imageView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            if (url == USER_AVATAR) error(R.drawable.ic_circled_user)
            else error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(
    recyclerView: RecyclerView, data: List<PcrsData>?
) {
    val adapter = recyclerView.adapter as ChoosingPcrsAdapter
    adapter.submitList(data)
}

@BindingAdapter("favListData")
fun bindFavRecyclerView(
    recyclerView: RecyclerView, data: List<Fav>?
) {
    val adapter = recyclerView.adapter as FavListAdapter
    adapter.submitList(data)
}

@BindingAdapter("teamMemberData")
fun bindTeamMemberData(
    recyclerView: RecyclerView, data: List<User>?
) {
    val adapter = recyclerView.adapter as TeamMemberAdapter
    adapter.submitList(data)
}

@BindingAdapter("myFilesData")
fun bindMyFilesData(
    recyclerView: RecyclerView, data: List<FileData>?
) {
    val adapter = recyclerView.adapter as MyFilesAdapter
    adapter.submitList(data)
}

@BindingAdapter("listDataCatSubCat")
fun bindRecyclerViewCatSubCat(
    recyclerView: RecyclerView, data: List<CatSubCatModel>?
) {
    val adapter = recyclerView.adapter as ChoosingCatSubCatAdapter
    adapter.submitList(data)
}

@BindingAdapter("listDataAiSuggestion")
fun bindListDataAiSuggestion(
    recyclerView: RecyclerView, data: List<SuggestionItemListModel>?
) {
    val adapter = recyclerView.adapter as SuggestionAdapter
    adapter.submitList(data)
}

@BindingAdapter("bindingCrashEmptyData")
fun bindingCrashEmptyData(view: View?, status: ApiStatus) {
    view?.let {
        if (it is ImageView) when (status) {
            ApiStatus.ERROR -> {
                it.visibility = View.VISIBLE
                it.setImageResource(R.drawable.ic_broken_image)
            }

            ApiStatus.NO_DATA -> {
                it.visibility = View.VISIBLE
                it.setImageResource(R.drawable.baseline_no_data_24)

            }

            else -> it.visibility = View.GONE
        }
        if (it is TextView) when (status) {
            ApiStatus.ERROR -> {
                it.visibility = View.VISIBLE
                it.text = view.context.getString(R.string.error_getting_data)
            }

            ApiStatus.NO_DATA -> {
                it.visibility = View.VISIBLE
                it.text = view.context.getString(R.string.empty_data)
            }

            else -> it.visibility = View.GONE
        }
    }
}

@BindingAdapter("bindLoading")
fun bindLoading(view: View?, status: ApiStatus) {
    view?.let {
        when (status) {
            ApiStatus.LOADING -> it.visibility = View.INVISIBLE
            else -> it.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("bindProgressLoading")
fun bindProgressLoading(progressBar: ProgressBar?, status: ApiStatus) {
    progressBar?.let {
        when (status) {
            ApiStatus.LOADING -> it.visibility = View.VISIBLE
            else -> it.visibility = View.GONE
        }
    }
}

@BindingAdapter("setVisibility")
fun bindVisibility(view: View?, isVisible: Boolean) {
    view?.let {
        if (isVisible) it.visibility = View.VISIBLE
        else it.visibility = View.GONE
    }
}

@BindingAdapter("bindNoData")
fun bindNoData(view: View?, status: ApiStatus) {
    view?.let {
        when (status) {
            ApiStatus.ERROR -> {
                it.visibility = View.VISIBLE
                it.setBackgroundResource(R.drawable.ic_broken_image)
            }

            ApiStatus.NO_DATA -> {
                it.visibility = View.VISIBLE
                it.setBackgroundResource(R.drawable.icons8_add_administrator_100)
            }

            else -> {
                it.visibility = View.GONE
            }
        }
        if (it is TextView) when (status) {
            ApiStatus.ERROR -> {
                it.visibility = View.VISIBLE
                it.text = view.context.getString(R.string.error_getting_data)
            }

            ApiStatus.NO_DATA -> {
                it.visibility = View.VISIBLE
                it.text = view.context.getString(R.string.add_team_member)
            }

            else -> it.visibility = View.GONE
        }
    }
}

@BindingAdapter("bindNoDataFile")
fun bindNoDataFile(view: View?, status: ApiStatus) {
    view?.let {
        if (it is ImageView) when (status) {
            ApiStatus.ERROR -> {
                it.visibility = View.VISIBLE
                it.setBackgroundResource(R.drawable.ic_broken_image)
            }

            ApiStatus.NO_DATA -> {
                it.visibility = View.VISIBLE
                it.setBackgroundResource(R.drawable.baseline_upload_file_24)
            }

            else -> {
                it.visibility = View.GONE
            }
        }
        if (it is TextView) when (status) {
            ApiStatus.ERROR -> {
                it.visibility = View.VISIBLE
                it.text = view.context.getString(R.string.error_getting_data)
            }

            ApiStatus.NO_DATA -> {
                it.visibility = View.VISIBLE
                it.text = view.context.getString(R.string.add_new_file)
            }

            else -> it.visibility = View.GONE
        }
    }
}