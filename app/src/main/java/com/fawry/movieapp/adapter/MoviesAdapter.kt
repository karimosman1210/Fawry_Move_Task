package com.fawry.movieapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fawry.movieapp.R
import com.fawry.movieapp.data.model.Movie
import com.fawry.movieapp.databinding.ItemMovieBinding
import com.fawry.movieapp.utils.AppUtils

class MoviesAdapter(
    private val context: Context,
    private val items: MutableList<Movie>,
    private val itemClickListener: OnMovieClickListener,
) : RecyclerView.Adapter<MoviesAdapter.ItemHolder>() {


    fun addItems(newItems: List<Movie>) {
        if (items.isEmpty()) {
            items.addAll(newItems)
            notifyDataSetChanged()
        } else {
            val lastPosition = items.size - 1

            items.addAll(newItems)

            notifyItemRangeInserted(lastPosition + 1, items.size - 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding: ItemMovieBinding =
            ItemMovieBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: ItemHolder, position: Int) {
        val item = items[position]

        Glide.with(context)
            .load(AppUtils.BASE_POSTER_URL + item.posterPath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(viewHolder.itemBinding.itemImage)

        viewHolder.itemBinding.itemTitle.text = item.title
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemHolder(val itemBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(
            itemBinding.root
        ) {

        init {
            itemBinding.root.setOnClickListener {
                itemClickListener.onMovieClick(items[bindingAdapterPosition])
            }
        }

    }

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
    }
}