package com.altintasomer.tmdbapi.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.altintasomer.tmdbapi.databinding.LayoutFilmItemBinding
import com.altintasomer.tmdbapi.domain.model.Film

class FilmAdapter (private val onItemClick : (filmDto : Film) -> Unit) : RecyclerView.Adapter<FilmAdapter.FilmItemViewHolder>() {


    class FilmItemViewHolder(val binding : LayoutFilmItemBinding) : RecyclerView.ViewHolder(binding.root)


    private val differCallBak = object : DiffUtil.ItemCallback<Film>(){
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
          return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
           return oldItem == newItem
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<Film>, fromSearchingFirst : Boolean){
        differ.submitList(list)
        if (fromSearchingFirst) notifyDataSetChanged()
    }
   private val differ = AsyncListDiffer(this,differCallBak)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmItemViewHolder {
        val binding = LayoutFilmItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FilmItemViewHolder(binding).apply {
            binding.root.setOnClickListener {
                adapterPosition.also {
                    if (it != Adapter.NO_SELECTION){
                        differ.currentList[it]?.let { result ->
                            onItemClick(result)
                        }
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: FilmItemViewHolder, position: Int) {
        with(holder.binding){
            film = differ.currentList[position]
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}