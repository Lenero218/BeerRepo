package com.example.let_me_have_one.Beers.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.let_me_have_one.Beers.Network.models.BeerModel
import com.example.let_me_have_one.Beers.db.model
import com.example.let_me_have_one.Beers.presentation.ui.beerList.BeerListViewModel

import com.example.let_me_have_one.R
import com.example.let_me_have_one.databinding.CardViewBinding

import kotlin.random.Random

class beerAdapter(private val beerListViewModel: BeerListViewModel) : RecyclerView.Adapter<beerAdapter.BeerViewHolder>() {

    lateinit var binding : CardViewBinding


    inner class BeerViewHolder(itemView: CardViewBinding) : ViewHolder(binding.root){
        val favImg = itemView.favorite
        val amount = itemView.amount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        binding = CardViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BeerViewHolder(binding)
    }

    val differCallBack = object :DiffUtil.ItemCallback<BeerModel>(){
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

    override fun onBindViewHolder(holder: BeerViewHolder, @SuppressLint("RecyclerView") position: Int) {

        var model = differ.currentList[position]
        holder.itemView.apply {

            Glide.with(this)
                .load(model.image_url)
                .into(binding.beerImage)



                binding.tagLine.setText(model.tagline)
                binding.title.setText(model.name)
                binding.amount.setText(model.amount.toString())
                 setOnClickListener{
                onItemClickListener?.let{
                    it(model)
                }
            }
        }

        if(model.isFavorite){
            holder.favImg.setImageResource(R.drawable.baseline_favorite_24)
        }else{
            holder.favImg.setImageResource(R.drawable.baseline_favorite_border_24)
        }


        holder.favImg.setOnClickListener {

            model.isFavorite = !model.isFavorite

            if(model.isFavorite){

                Toast.makeText( it.context,"Added to Favorites",Toast.LENGTH_SHORT).show()

                holder.favImg.setImageResource(R.drawable.baseline_favorite_24)
                Glide.with(it).asBitmap().load(model.image_url)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            beerListViewModel.insertBeer(
                                model(model.pk,resource,model.name,model.tagline,model.abv,
                                model.description,model.food_pairing,model.brewers_tips,model.amount,false,model.isFavorite)
                            )
                            Log.d("favorite","Inserted into Room db as favorite with ${position}")
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            //
                        }

                    })


            }else{
                holder.favImg.setImageResource(R.drawable.baseline_favorite_border_24)
                Toast.makeText( it.context,"Removed from Favorites",Toast.LENGTH_SHORT).show()
                //Write a query to remove from database

            }


        }

    }

    private var onItemClickListener : ((BeerModel) -> Unit)? = null

    fun setOnItemClickListener(listener : (BeerModel)->Unit){
        onItemClickListener = listener
    }



}