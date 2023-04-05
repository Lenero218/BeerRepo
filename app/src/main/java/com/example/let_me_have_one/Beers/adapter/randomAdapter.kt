package com.example.let_me_have_one.Beers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.let_me_have_one.Beers.Network.models.BeerModel
import com.example.let_me_have_one.Beers.db.model
import com.example.let_me_have_one.R
import com.example.let_me_have_one.databinding.FavoritecardviewBinding
import com.example.let_me_have_one.databinding.RandomcardviewBinding

class randomAdapter : RecyclerView.Adapter<randomAdapter.randomViewHolder>() {

    lateinit var binding : RandomcardviewBinding

    inner class randomViewHolder(itemView: RandomcardviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): randomViewHolder {
        binding = RandomcardviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return randomViewHolder(binding)
    }

    val differCallBack = object : DiffUtil.ItemCallback<BeerModel>(){
        override fun areItemsTheSame(oldItem: BeerModel, newItem: BeerModel): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: BeerModel, newItem: BeerModel): Boolean {
            return oldItem==newItem
        }

    }

    val differ= AsyncListDiffer(this,differCallBack)

    fun submitList(list:List<BeerModel>) = differ.submitList(list)



    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: randomViewHolder, position: Int) {

        var beerModel = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(beerModel.image_url).placeholder(R.drawable.beer).into(binding.beerImage)
            binding.rating.setText(beerModel.rating.toString())
            binding.beerName.setText(beerModel.name.toString())
        }
    }
}