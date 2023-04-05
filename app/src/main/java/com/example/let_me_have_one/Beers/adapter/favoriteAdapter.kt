package com.example.let_me_have_one.Beers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.let_me_have_one.Beers.db.model
import com.example.let_me_have_one.Beers.presentation.ui.BeerListViewModel
import com.example.let_me_have_one.R
import com.example.let_me_have_one.databinding.FavoritecardviewBinding

class favoriteAdapter(viewModel : BeerListViewModel) : RecyclerView.Adapter<favoriteAdapter.favViewHolder>() {

    lateinit var binding : FavoritecardviewBinding
    var viewModel: BeerListViewModel = viewModel
    lateinit  var listm : List<model>


    inner class favViewHolder(itemView : FavoritecardviewBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favViewHolder {
        binding = FavoritecardviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return favViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    val differCallBack = object : DiffUtil.ItemCallback<model>(){
        override fun areItemsTheSame(oldItem: model, newItem: model): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: model, newItem: model): Boolean {
            return oldItem==newItem
        }

    }

    val differ= AsyncListDiffer(this,differCallBack)



    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun submitList(list:List<model>){
       listm = list
        differ.submitList(listm)

    }

    override fun onBindViewHolder(holder: favViewHolder, position: Int) {

        var model = differ.currentList[position]

        holder.itemView.apply {
            binding.buyNow.setOnClickListener {
                Toast.makeText(context,"Payment Succesfull",Toast.LENGTH_SHORT).show()
            }
            binding.amount.setText(model.amount.toString())
            binding.tagline.setText(model.tagLine.toString())
            binding.noOfReviews.setText(model.no_of_reviews.toString())
            binding.title.setText(model.name.toString())
            Glide.with(this).load(model.image).placeholder(R.drawable.beer) .into(binding.beerImage)

            binding.delete.setOnClickListener {
                model.name?.let { it1 -> viewModel.delete(it1,true,false) }
            }



        }

    }


}