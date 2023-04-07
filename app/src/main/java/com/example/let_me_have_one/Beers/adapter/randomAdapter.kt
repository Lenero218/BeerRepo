package com.example.let_me_have_one.Beers.adapter

import android.annotation.SuppressLint
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
import com.example.let_me_have_one.databinding.RandomcardviewBinding
import dagger.hilt.android.AndroidEntryPoint


class randomAdapter(beerListViewModel: BeerListViewModel, context: Context) : RecyclerView.Adapter<randomAdapter.randomViewHolder>() {

    lateinit var binding : RandomcardviewBinding
    var context = context
    private val beerListViewModel = beerListViewModel

    inner class randomViewHolder(itemView: RandomcardviewBinding) : RecyclerView.ViewHolder(binding.root){

        val fav = itemView.favorite

    }

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


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int = differ.currentList.size

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: randomViewHolder, position: Int) {

        var beerModel = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(beerModel.image_url).placeholder(R.drawable.beer).into(binding.beerImage)
            binding.rating.setText(beerModel.rating.toString())
            binding.beerName.setText(beerModel.name.toString())

            binding.favorite.setImageResource(R.drawable.favourite_border)


        }

        holder.fav.setOnClickListener {

            if(beerModel.isFavorite == false){

                binding.favorite.setImageResource(R.drawable.baseline_favorite_24)

                Glide.with(it).asBitmap().load(beerModel.image_url)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {

                            beerListViewModel.insertBeer(
                                model(beerModel.pk,resource,beerModel.name,beerModel.tagline,beerModel.abv,
                                    beerModel.description,beerModel.food_pairing,beerModel.brewers_tips,beerModel.amount,false, true,beerModel.rating, currentOffer = 0,beerModel.amount,beerModel.no_of_reviews)
                            )
                            Log.d("favorite","Inserted into Room db as favorite with ${position}")

                        }
                        override fun onLoadCleared(placeholder: Drawable?) {
                            //
                        }

                    })

                    Toast.makeText(context,"Added to Favorites!!",Toast.LENGTH_SHORT).show()

            }else{

                binding.favorite.setImageResource(R.drawable.baseline_favorite_border_24)

                Toast.makeText(context,"Removed from Favorites",Toast.LENGTH_SHORT).show()



            }

        }


    }
}