package com.example.melkist.views.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.melkist.MainActivity
import com.example.melkist.R
import com.example.melkist.adapters.MyFilesAdapter
import com.example.melkist.databinding.FragProfileMyfilesBinding
import com.example.melkist.models.FileData
import com.example.melkist.utils.showToast
import com.example.melkist.viewmodels.MainViewModel

class ProfileMyFilesFrag : Fragment() {

    private lateinit var binding: FragProfileMyfilesBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragProfileMyfilesBinding.inflate(inflater)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
            fragment = this@ProfileMyFilesFrag
            rvMyFiles.adapter = MyFilesAdapter(this@ProfileMyFilesFrag)
        }
        val user = (activity as MainActivity).user
        user?.apply {
            Log.e("TAG", "onCreateView: ${user.id}", )
            viewModel.getMyFiles(
                token = user.token!!,
                userId = user.id!!
            )
        }
        return binding.root
    }

    fun choosingItemAction(file: FileData){
        // TODO
        showToast(requireContext(), file.toString())
    }

    /******************** binding methods ***************************/
    fun back() {
        findNavController().popBackStack()
    }

    fun onAddFileClick() {
        // TODO
    }

}