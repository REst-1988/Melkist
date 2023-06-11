package com.example.melkist.views.fav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.melkist.databinding.FragFavListBinding
import com.example.melkist.viewmodels.FavViewModel
import com.example.melkist.R

class FavListFrag : Fragment() {

    private val viewModel: FavViewModel by viewModels()
    private var _binding: FragFavListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragFavListBinding.inflate(inflater)
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@FavListFrag
        }
        return binding.root
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
        _binding?.ibtnSearch?.startAnimation(animation1)
        _binding?.ibtnFilter?.startAnimation(animation1)
        _binding?.llSearch?.startAnimation(animation2)
        /*        animation1.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        llSearch.setVisibility(View.VISIBLE)
                    }

                    override fun onAnimationEnd(animation: Animation) {
                        ibtnSearch.setVisibility(View.GONE)
                        ibtnFilter.setVisibility(View.GONE)
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                animationTitle1.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        txtTitle.setVisibility(View.GONE)
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })*/
    }

    fun onCloseSearchClick() {
        val animation1 = AnimationUtils.loadAnimation(context, R.anim.search_btn_end_anim)
        val animation2 = AnimationUtils.loadAnimation(
            context, R.anim.search_field_end_anim
        )
        _binding?.ibtnSearch?.startAnimation(animation1)
        _binding?.ibtnFilter?.startAnimation(animation1)
        _binding?.llSearch?.startAnimation(animation2)
    }

    fun onFilterClick() {
        //TODO
    }

}