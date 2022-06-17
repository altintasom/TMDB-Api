package com.altintasomer.etscase.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.altintasomer.etscase.R
import com.altintasomer.etscase.databinding.FragmentMainBinding
import com.altintasomer.etscase.onQueryTextChanged
import com.altintasomer.etscase.utils.Status
import com.altintasomer.etscase.view.adapters.FilmAdapter
import com.altintasomer.etscase.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainFragment"
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var binding : FragmentMainBinding
    private lateinit var adapter : FilmAdapter
    private val viewModel : MainViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        binding = FragmentMainBinding.bind(view)
        setHasOptionsMenu(true)
        viewModel.getFilm("jack")

        adapter = FilmAdapter(onItemClick = {
            val bundle = bundleOf("id" to it.id)
            findNavController().navigate(R.id.action_mainFragment_to_detailFragment,bundle)
        })

        binding.rvMain.also {
            it.layoutManager = GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false)
            it.adapter =adapter
        }
        
        viewModel.results.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                when(it.status){
                    Status.LOADING -> {
                        Log.d(TAG, "init: loading")
                    }
                    Status.SUCCESS -> {
                        Log.d(TAG, "init: success")
                        val result = it.data?.get(0)
                        Log.d(TAG, "init: data: ${result}")
                        adapter.differ.submitList(it.data)

                    }
                    
                    Status.ERROR ->{
                        Log.d(TAG, "init: error: ${it.message}")
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu,menu)

        val searchItem = menu.findItem(R.id.actionSearch)
        val searchView = searchItem.actionView as SearchView
        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return true
    }

}