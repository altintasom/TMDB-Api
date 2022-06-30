package com.altintasomer.etscase.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.altintasomer.etscase.R
import com.altintasomer.etscase.databinding.FragmentMainBinding
import com.altintasomer.etscase.utils.Status
import com.altintasomer.etscase.utils.onQueryTextChanged
import com.altintasomer.etscase.view.adapters.FilmAdapter
import com.altintasomer.etscase.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

private const val TAG = "MainFragment"
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var binding : FragmentMainBinding
    private lateinit var adapter : FilmAdapter
    private lateinit var layoutManager: GridLayoutManager
    private val viewModel : MainViewModel by viewModels()
    private  var textChangeJob: Job? = null
    private  var searchView:SearchView? = null
    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = FilmAdapter(onItemClick = {
            val bundle = bundleOf("id" to it.id)
            findNavController().navigate(R.id.action_mainFragment_to_detailFragment,bundle)
        })
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun init(view: View) {
        binding = FragmentMainBinding.bind(view)
        setHasOptionsMenu(true)

        layoutManager = GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false)

        binding.rvMain.also {
            it.layoutManager = layoutManager
            it.adapter =adapter
            it.setHasFixedSize(true)
        }.addOnScrollListener(object: PaginationScrollListener(layoutManager){
            override fun loadMoreItems() {
                viewModel.currentPage+=1
                viewModel.fromSearchingFirst = false
                if (viewModel.fromSearching)viewModel.searchFilms(viewModel.searchQuery) else viewModel.getFilms()

            }

            override fun isLastPage(): Boolean {
                return viewModel.IS_LAST_PAGES
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoading
            }

        })

       job = CoroutineScope(Dispatchers.Main).launch {
           viewModel.resultsFlow.collect {
               it.getContentIfNotHandled()?.let {

                   when(it.status){
                       Status.LOADING -> {
                           binding.layoutProgress.visibility = View.VISIBLE
                       }
                       Status.SUCCESS -> {
                           binding.layoutProgress.visibility = View.GONE
                           it.data?.let { results->
                               if (!results.isNullOrEmpty()){
                                   adapter.updateList(it.data,viewModel.fromSearchingFirst)
                               }
                           }
                       }
                       Status.ERROR -> {
                           binding.layoutProgress.visibility = View.GONE
                           Log.e(TAG, "init: error: ${it.message}")
                           Toast.makeText(requireContext(),it.message?:"Error", Toast.LENGTH_SHORT).show()
                       }
                   }

               }
           }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu,menu)

        val searchItem = menu.findItem(R.id.actionSearch)
         searchView = searchItem.actionView as SearchView
        val pendingQuery = viewModel.searchQuery

        if (!pendingQuery.isNullOrEmpty()){
            viewModel.fromSearching = true
            searchItem.expandActionView()
            searchView?.hideKeyboard()
            searchView?.setQuery(pendingQuery,false)
        }
        searchView?.onQueryTextChanged {
            textChangeJob?.cancel()
            textChangeJob = CoroutineScope(Dispatchers.Main).launch {
                if (it.length >= 3){
                    delay(500)
                    viewModel.fromSearching = true
                    viewModel.fromSearchingFirst = true
                    viewModel.clearSearchList()
                    viewModel.searchQuery = it
                    viewModel.searchFilms(it)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       return true
    }

    override fun onStop() {
        super.onStop()
        textChangeJob?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView?.setOnQueryTextListener(null)
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(getWindowToken(), 0)
    }
}