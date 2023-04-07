package com.example.let_me_have_one.Beers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.let_me_have_one.Beers.db.model
import com.example.let_me_have_one.Beers.presentation.ui.BeerListViewModel
import com.example.let_me_have_one.R
import com.example.let_me_have_one.databinding.AddtocartcartviewBinding

class addToCartAdapter(viewModel : BeerListViewModel) : RecyclerView.Adapter<addToCartAdapter.atcViewHolder>() {

    lateinit var binding: AddtocartcartviewBinding
    var viewModel: BeerListViewModel = viewModel


    inner class atcViewHolder(itemView: AddtocartcartviewBinding) : ViewHolder(binding.root) {

        val buyButton = itemView.buyNow
     //   val delete = binding.deleteButton

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): atcViewHolder {
        binding =
            AddtocartcartviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return atcViewHolder(binding)
    }

    val differCallBack = object : DiffUtil.ItemCallback<model>() {
        override fun areItemsTheSame(oldItem: model, newItem: model): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: model, newItem: model): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    fun submitList(list: List<model>) = differ.submitList(list)

    val differ = AsyncListDiffer(this, differCallBack)

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: atcViewHolder, position: Int) {

        var model = differ.currentList[position]




        holder.itemView.apply {
            Glide.with(this).load(model.image).placeholder(R.drawable.beer).into(binding.beerImage)

            binding.amount.setText(model.amount.toString())
            binding.tagline.setText(model.tagLine.toString())
            binding.finalAmount.setText(model.finalAmount.toString())
            binding.currentOffer.setText(model.currentOffer.toString())
            binding.noOfReviews.setText(model.no_of_reviews.toString())
            binding.title.setText(model.name.toString())

            binding.buyNow.setOnClickListener {
                    Toast.makeText(context,"Payment Succesfull",Toast.LENGTH_SHORT).show()
            }







        }


    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface ModelClickDeleteInterface{

        fun onDeleteIconClick(model : String)
    }



}