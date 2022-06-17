package com.altintasomer.etscase.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.altintasomer.etscase.databinding.LayoutFilmItemBinding
import com.altintasomer.etscase.model.network.PopularFilm
import com.altintasomer.etscase.model.network.Result

class FilmAdapter (private val onItemClick : (result : Result) -> Unit) : RecyclerView.Adapter<FilmAdapter.FilmItemViewHolder>() {


    class FilmItemViewHolder(val binding : LayoutFilmItemBinding) : RecyclerView.ViewHolder(binding.root)


    private val differCallBak = object : DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
          return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
           return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallBak)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmItemViewHolder {
        val binding = LayoutFilmItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FilmItemViewHolder(binding).apply {
            binding.root.setOnClickListener {
                adapterPosition.also {
                    if (it != Adapter.NO_SELECTION){
                        differ.currentList.get(it)?.let { result ->
                            onItemClick(result)
                        }
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: FilmItemViewHolder, position: Int) {
        with(holder.binding){
            result = differ.currentList[position]
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}