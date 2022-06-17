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
import com.altintasomer.etscase.R
import com.altintasomer.etscase.databinding.FragmentMainBinding
import com.altintasomer.etscase.utils.Status
import com.altintasomer.etscase.utils.onQueryTextChanged
import com.altintasomer.etscase.view.adapters.FilmAdapter
import com.altintasomer.etscase.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

private const val TAG = "MainFragment"
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var binding : FragmentMainBinding
    private lateinit var adapter : FilmAdapter
    private lateinit var layoutManager: GridLayoutManager
    private val viewModel : MainViewModel by viewModels()
    private  var textChangeJob: Job? = null


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

    private fun init(view: View) {
        binding = FragmentMainBinding.bind(view)
        setHasOptionsMenu(true)
        viewModel.getFilms()

        layoutManager = GridLayoutManager(requireContext(),3,GridLayoutManager.VERTICAL,false)

        binding.rvMain.also {
            it.layoutManager = layoutManager
            it.adapter =adapter
        }.addOnScrollListener(object: PaginationScrollListener(layoutManager){
            override fun loadMoreItems() {
                viewModel.currentPage+=1
                if (viewModel.fromSearching) viewModel.searchFilms(viewModel.searchQuery.value)  else viewModel.getFilms()

            }

            override fun isLastPage(): Boolean {
                return viewModel.IS_LAST_PAGES
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoading
            }

        })

        viewModel.results.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let {
                when(it.status){
                    Status.LOADING -> {
                        Log.d(TAG, "init: loading")
                        binding.layoutProgress.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        binding.layoutProgress.visibility = View.GONE
                        Log.d(TAG, "init: success")
                        it.data?.let { results->
                            if (!results.isNullOrEmpty()){
                                val result = it.data.get(0)
                                Log.d(TAG, "init: data: ${result}")
                                adapter.differ.submitList(it.data)

                            }else{
                                adapter.differ.submitList(null)

                            }
                        }

                    }

                    Status.ERROR ->{
                        binding.layoutProgress.visibility = View.GONE
                        Log.e(TAG, "init: error: ${it.message}")
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
            textChangeJob?.cancel()
            textChangeJob = CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                viewModel.searchFilms(it)
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

}