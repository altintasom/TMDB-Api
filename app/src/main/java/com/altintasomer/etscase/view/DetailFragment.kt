package com.altintasomer.etscase.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.altintasomer.etscase.R
import com.altintasomer.etscase.databinding.FragmentDetailBinding
import com.altintasomer.etscase.utils.Status
import com.altintasomer.etscase.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "DetailFragment"
@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel : DetailViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    private fun init(view: View) {
        binding = FragmentDetailBinding.bind(view)

        val id = arguments?.getInt("id",0)

        id?.let {
            viewModel.getFilmDetail(it.toString())
        }

        viewModel.filmDetail.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                when(it.status){
                    Status.LOADING ->{
                        Log.d(TAG, "init: loading...")
                    }
                    Status.SUCCESS -> {
                        binding.filmDetail = it.data
                    }
                    Status.ERROR->{
                        Log.d(TAG, "init: error")
                    }
                }
            }
        }

    }
}