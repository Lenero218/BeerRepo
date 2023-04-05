package com.example.let_me_have_one.Beers.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.let_me_have_one.Beers.Network.models.BeerModel
import com.example.let_me_have_one.Beers.db.model
import com.example.let_me_have_one.Beers.presentation.ui.BeerListViewModel
import com.example.let_me_have_one.R
import com.example.let_me_have_one.databinding.CardViewBinding
import com.example.let_me_have_one.databinding.RecomendationscardviewBinding

class recomendationListAdapter(beerListViewModel: BeerListViewModel, context : Context) : RecyclerView.Adapter<recomendationListAdapter.recomendationViewHolder>() {


    lateinit var binding : RecomendationscardviewBinding
    private val beerListViewModel = beerListViewModel
    lateinit var context: Context
    inner class recomendationViewHolder(itemView : RecomendationscardviewBinding) : RecyclerView.ViewHolder(binding.root){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): recomendationViewHolder {
        binding = RecomendationscardviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return recomendationViewHolder(binding)
    }

    val differCallBack = object : DiffUtil.ItemCallback<BeerModel>(){
        override fun areItemsTheSame(oldItem: BeerModel, newItem: BeerModel): Boolean {
            return oldItem.image_url == newItem.image_url
        }

        override fun areContentsTheSame(oldItem: BeerModel, newItem: BeerModel): Boolean {
            return oldItem==newItem
        }

    }

    val differ= AsyncListDiffer(this,differCallBack)

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun submitList(list : List<BeerModel>) = differ.submitList(list)



    override fun getItemCount(): Int = differ.currentList.size



    override fun onBindViewHolder(holder: recomendationViewHolder, position: Int) {
        val model = differ.currentList[position]

        holder.itemView.apply {

            binding.recomendedName.setText(model.name)
            Glide.with(this).load(model.image_url).placeholder(R.drawable.beer) .into(binding.recomendedImg)
            binding.recomendedTagline.setText(model.tagline)
            binding.recomendedAmount.setText(model.amount.toString())
            binding.recomendedOffer.setText(model.currentOffer.toString())
            binding.recomendedRating.setText(model.rating.toString())

        }





    }


}