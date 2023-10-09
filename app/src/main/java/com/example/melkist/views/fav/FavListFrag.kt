package com.example.melkist.views.fav

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.FavListAdapter
import com.example.melkist.databinding.FragFavListBinding
import com.example.melkist.interfaces.Interaction
import com.example.melkist.models.Fav
import com.example.melkist.utils.CONDITION_FILTER_FAV_LIST
import com.example.melkist.utils.IS_IN_FAV_LIST_KEY
import com.example.melkist.utils.UNKNOWN_ERRORS_LIST
import com.example.melkist.utils.onRequestFalseResult
import com.example.melkist.viewmodels.MainViewModel

class FavListFrag : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: FavListAdapter
    private var _binding: FragFavListBinding? = null
    private var interaction: Interaction? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragFavListBinding.inflate(inflater)
        adapter = FavListAdapter(viewModel, this)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@FavListFrag
            rvFavList.adapter = adapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavList.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        binding.pullToRefreshMainList.setOnRefreshListener {
            getFavList()
        }
        viewModel.favList.observe(viewLifecycleOwner) { response ->
            binding.pullToRefreshMainList.isRefreshing = false
            when (response.result) {
                true -> {/*handled on data binding*/
                }

                false -> {
                    viewModel.setNoDataStatus()
                    onRequestFalseResult(
                        requireActivity(),
                        response.errors ?: UNKNOWN_ERRORS_LIST
                    ) {}
                }

                else -> {}
            }
        }
        activeSearchableFav()
        viewModel.fileAllData.removeObservers(viewLifecycleOwner)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        interaction?.changBottomNavViewVisibility(View.VISIBLE)
        adapter.submitList(listOf())
        readyViewsOnFilter()
        if (viewModel.filterFavData == null)
            getFavList()
        else {
            viewModel.resetLocationResponse()
            (activity as MainActivity).user.apply {
                viewModel.filterFavData!!.userId = this?.id
                viewModel.getFilterFiles(
                    requireActivity(), this?.token, viewModel.filterFavData!!
                )
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Interaction) {
            interaction = context
        } else {
            throw RuntimeException(
                context.toString() + " must implement OnFragmentInteractionListener"
            )
        }
    }

    // This is for hide and unhiding bottom nav bar
    override fun onDetach() {
        super.onDetach()
        interaction = null
    }

    private fun getFavList() = (activity as MainActivity).user.apply {
        viewModel.getFavoritesFile(
            requireActivity(),
            this?.token,
            this?.id
        )
    }

    private fun activeSearchableFav() {
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun choosingItemAction(fav: Fav) {
        viewModel.resetFileAllData()
        listenToFileDetailData()
        (activity as MainActivity).user.apply {
            viewModel.getFileInfoById(
                requireActivity(),
                this?.token,
                fileId = fav.id,
                this?.id
            )
        }
    }

    private fun listenToFileDetailData() {
        viewModel.fileAllData.observe(viewLifecycleOwner) { response ->
            when (response.result) {
                true -> {
                    findNavController().navigate(R.id.action_navigation_fav_to_fileDetailFrag)
                    interaction?.changBottomNavViewVisibility(View.GONE)
                }

                false -> onRequestFalseResult(
                    requireActivity(),
                    response.errors ?: UNKNOWN_ERRORS_LIST
                ) {}

                else -> {}
            }
        }
    }

    private fun readyViewsOnFilter() {
        if (viewModel.filterFavData != null) {
            binding.ibtnFilter.setBackgroundResource(R.drawable.background_rounded_btns_sharp)
        } else {
            binding.ibtnFilter.setBackgroundResource(R.drawable.background_rounded_btns)
        }
    }

    /******************** binding stuff ***********************/
    fun onSearchClick() {
        val animation1 = AnimationUtils.loadAnimation(context, R.anim.search_btn_start_anim)
        val animation2 = AnimationUtils.loadAnimation(
            context, R.anim.search_field_start_anim
        )
        binding.ibtnSearch.startAnimation(animation1)
        binding.ibtnFilter.startAnimation(animation1)
        binding.searchView.startAnimation(animation2)
        binding.ibtnCloseSearchview.startAnimation(animation2)
        animation1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                binding.searchView.visibility = View.VISIBLE
                binding.ibtnCloseSearchview.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                binding.ibtnSearch.visibility = View.GONE
                binding.ibtnFilter.visibility = View.GONE
                binding.searchView.requestFocus()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    fun onCloseSearchClick() {
        val animation1 = AnimationUtils.loadAnimation(context, R.anim.search_btn_end_anim)
        val animation2 = AnimationUtils.loadAnimation(
            context, R.anim.search_field_end_anim
        )
        binding.apply {
            ibtnSearch.startAnimation(animation1)
            ibtnFilter.startAnimation(animation1)
            searchView.startAnimation(animation2)
            ibtnCloseSearchview.startAnimation(animation2)
            searchView.setText("")
            searchView.clearFocus()
        }
        animation1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                binding.ibtnSearch.visibility = View.VISIBLE
                binding.ibtnFilter.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                binding.searchView.visibility = View.GONE
                binding.ibtnCloseSearchview.visibility = View.GONE
                binding.searchView.clearFocus()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    fun onFilterClick() {
        findNavController().navigate(
            R.id.action_navigation_fav_to_FilterFilesFrag, bundleOf(
                IS_IN_FAV_LIST_KEY to CONDITION_FILTER_FAV_LIST
            )
        )
        interaction?.changBottomNavViewVisibility(View.GONE)
    }
}