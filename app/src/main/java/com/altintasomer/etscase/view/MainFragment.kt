package com.altintasomer.etscase.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.altintasomer.etscase.R
import com.altintasomer.etscase.databinding.FragmentMainBinding
import com.altintasomer.etscase.onQueryTextChanged
import com.altintasomer.etscase.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainFragment"
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var binding : FragmentMainBinding
    private val viewModel : MainViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    private fun init(view: View) {
        binding = FragmentMainBinding.bind(view)
        setHasOptionsMenu(true)
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