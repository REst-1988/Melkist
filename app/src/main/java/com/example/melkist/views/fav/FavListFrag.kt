package com.example.melkist.views.fav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.FavListAdapter
import com.example.melkist.databinding.FragFavListBinding
import com.example.melkist.viewmodels.FavViewModel

class FavListFrag : Fragment() {

    private val viewModel: FavViewModel by viewModels()
    private lateinit var adapter: FavListAdapter
    private var _binding: FragFavListBinding? = null
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
        viewModel.getFavoritesFile(
            (activity as MainActivity).user.token!!,
            (activity as MainActivity).user.id!!
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /******************** binding stuff ***********************/
    fun onSearchClick() {
        val animation1 = AnimationUtils.loadAnimation(context, R.anim.search_btn_start_anim)
        val animation2 = AnimationUtils.loadAnimation(
            context, R.anim.search_field_start_anim
        )
        binding.ibtnSearch.startAnimation(animation1)
        binding.ibtnFilter.startAnimation(animation1)
        binding.llSearch.startAnimation(animation2)
        animation1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                binding.llSearch.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                binding.ibtnSearch.visibility = View.GONE
                binding.ibtnFilter.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    fun onCloseSearchClick() {
        val animation1 = AnimationUtils.loadAnimation(context, R.anim.search_btn_end_anim)
        val animation2 = AnimationUtils.loadAnimation(
            context, R.anim.search_field_end_anim
        )
        binding.ibtnSearch.startAnimation(animation1)
        binding.ibtnFilter.startAnimation(animation1)
        binding.llSearch.startAnimation(animation2)
        animation1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                binding.ibtnSearch.visibility = View.VISIBLE
                binding.ibtnFilter.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
                binding.llSearch.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    fun onFilterClick() {
        //TODO
    }

}