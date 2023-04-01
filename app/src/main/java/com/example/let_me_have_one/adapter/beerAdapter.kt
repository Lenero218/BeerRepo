package com.example.let_me_have_one.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.let_me_have_one.Network.models.BeerModel
import com.example.let_me_have_one.databinding.CardViewBinding
import com.example.let_me_have_one.db.model

class beerAdapter : RecyclerView.Adapter<beerAdapter.BeerViewHolder>() {

    lateinit var binding : CardViewBinding


    inner class BeerViewHolder(itemView: CardViewBinding) : ViewHolder(binding.root)

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

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {

        val model = differ.currentList[position]
        holder.itemView.apply {

            Glide.with(this)
                .load(model.image_url)
                .into(binding.beerImage)

           // binding.beerImage.setImageBitmap(model.image)
                binding.tagLine.setText(model.tagline)
                binding.title.setText(model.name)

            setOnClickListener{
                onItemClickListener?.let{
                    it(model)
                }
            }
        }

    }

    private var onItemClickListener : ((BeerModel) -> Unit)? = null

    fun setOnItemClickListener(listener : (BeerModel)->Unit){
        onItemClickListener = listener
    }
}